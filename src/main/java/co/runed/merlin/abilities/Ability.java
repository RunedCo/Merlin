package co.runed.merlin.abilities;

import co.runed.bolster.Bolster;
import co.runed.bolster.game.Cost;
import co.runed.bolster.managers.CooldownManager;
import co.runed.bolster.util.IDescribable;
import co.runed.bolster.util.TaskUtil;
import co.runed.bolster.util.TimeUtil;
import co.runed.bolster.util.config.IConfigurable;
import co.runed.bolster.util.cooldown.ICooldownSource;
import co.runed.bolster.util.properties.Properties;
import co.runed.bolster.util.registries.IRegisterable;
import co.runed.merlin.abilities.core.DynamicParameterAbility;
import co.runed.merlin.abilities.core.FunctionAbility;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.ConditionPriority;
import co.runed.merlin.conditions.IConditional;
import co.runed.merlin.conditions.cooldown.IsOffCooldownCondition;
import co.runed.merlin.core.AbilityProvider;
import co.runed.merlin.core.ManaManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Ability implements Listener, IRegisterable, IConfigurable, IDescribable, IConditional<Ability>, ICooldownSource<Ability>
{
    private static final long CAST_BAR_UPDATE_TICKS = 5L;

    String id;
    String parentId = UUID.randomUUID().toString();
    String name;
    String cooldownId;
    String description;
    boolean enabled = true;

    List<Cost> costs = new ArrayList<>();
    protected List<Condition.Data> conditions = new ArrayList<>();
    List<AbilityTrigger> triggers = new ArrayList<>();

    List<Ability> abilities = new ArrayList<>();
    List<Ability> thenAbilities = new ArrayList<>();
    List<Ability> lastAbilities = new ArrayList<>();

    int charges = 1;
    double cooldown = 0;
    double castTime = 0;
    boolean globalCooldown = false;
    Duration duration = Duration.ZERO;
    int priority = 0;

    boolean showErrors = true;
    boolean evaluateConditions = true;
    boolean useCosts = true;
    boolean inProgress = false;
    boolean showCooldown = false;
    boolean mustBeActive = true;
    boolean mustBeInInventory = true;
    boolean skipIfCancelled = false;
    boolean resetCooldownOnDeath = false;
    boolean cancelledByTakingDamage = false;
    boolean cancelledByDealingDamage = false;
    boolean cancelledByMovement = false;
    boolean cancelledByCast = false;

    long lastErrorTime = 0;
    boolean cancelled = false;
    TaskUtil.TaskSeries castingTask;
    TaskUtil.TaskSeries seriesTask;

    LivingEntity caster;
    AbilityProvider abilityProvider;

    public Ability()
    {
        super();

        Bukkit.getPluginManager().registerEvents(this, Bolster.getInstance());

        this.condition(ConditionPriority.LOWEST, new IsOffCooldownCondition());
    }

    public static Ability builder()
    {
        return new AbilityImpl();
    }

    @Override
    public void create()
    {

    }

    /* Id */
    @Override
    public String getId()
    {
        return (this.getAbilityProvider() == null ? this.parentId : this.getAbilityProvider().getId()) + "." + this.id;
    }

    public Ability id(String id)
    {
        this.id = id;

        return this;
    }

    /* Name */
    public Ability name(String name)
    {
        this.name = name;

        return this;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    public Ability description(String description)
    {
        this.description = description;

        return this;
    }

    /* Enabled */
    public Ability enabled(boolean enabled)
    {
        this.enabled = enabled;

        return this;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    /* Conditions */
    @Override
    public Ability condition(ConditionPriority priority, Condition... conditions)
    {
        for (Condition condition : conditions)
        {
            this.conditions.add(new Condition.Data(condition, priority));
        }

        return this;
    }

    /* Triggers */
    public Ability trigger(AbilityTrigger... trigger)
    {
        this.triggers.addAll(Arrays.stream(trigger).collect(Collectors.toList()));

        return this;
    }

    public Collection<AbilityTrigger> getTriggers()
    {
        return triggers;
    }

    /* Costs */
    public Ability cost(Cost... cost)
    {
        this.costs.addAll(Arrays.stream(cost).collect(Collectors.toList()));

        return this;
    }

    public Collection<Cost> getCosts()
    {
        return costs;
    }

    /* Cast Time */
    public Ability castTime(double castTime)
    {
        this.castTime = castTime;

        return this;
    }

    public double getCastTime()
    {
        return castTime;
    }

    /* Duration */
    public Ability duration(Duration duration)
    {
        this.duration = duration;

        return this;
    }

    public Duration getDuration()
    {
//        if (this.duration != Duration.ZERO) return this.duration;
        Duration duration = this.getLongestDuration(this.abilities, this.duration);

        for (Ability ability : this.thenAbilities)
        {
            duration = duration.plus(ability.getDuration());
        }

        Duration lastDuration = this.getLongestDuration(this.lastAbilities, Duration.ZERO);

        duration = duration.plus(lastDuration);

        return duration;
    }

    private Duration getLongestDuration(Collection<Ability> abilities, Duration startDuration)
    {
        Duration currentDuration = startDuration;

        for (Ability ability : abilities)
        {
            if (ability.getDuration().compareTo(currentDuration) > 0) currentDuration = ability.getDuration();
        }

        return currentDuration;
    }

    /* Charges */
    public Ability charges(int charges)
    {
        this.charges = charges;

        for (var ability : getChildren())
        {
            ability.charges(charges);
        }

        return this;
    }

    public int getCharges()
    {
        return charges;
    }

    public int getLowestCooldownCharge()
    {
        var charge = 0;
        var lowest = this.getCooldown();

        for (int i = 0; i < this.getCharges(); i++)
        {
            var remaining = CooldownManager.getInstance().getRemainingTime(this.getCaster(), this, i);

            if (remaining < lowest)
            {
                lowest = remaining;
                charge = i;

                if (lowest <= 0) break;
            }
        }

        return charge;
    }

    /* Priority */
    public Ability priority(int priority)
    {
        this.priority = priority;

        return this;
    }

    public int getPriority()
    {
        return priority;
    }

    /* Show cooldown on item */
    public Ability showCooldown(boolean show)
    {
        this.showCooldown = show;

        return this;
    }

    public boolean showCooldown()
    {
        return showCooldown;
    }

    /* Must be in equipped in an active slot */
    public Ability itemMustBeActive(boolean mustBeActive)
    {
        this.mustBeActive = mustBeActive;

        return this;
    }

    public boolean itemMustBeActive()
    {
        return mustBeActive;
    }

    /* Must be in inventory */
    public Ability itemMustBeInInventory(boolean mustBeInInventory)
    {
        this.mustBeInInventory = mustBeInInventory;

        return this;
    }

    public boolean itemMustBeInInventory()
    {
        return mustBeInInventory;
    }

    /* Reset cooldown on death */
    public Ability resetCooldownOnDeath(boolean resetCooldownOnDeath)
    {
        this.resetCooldownOnDeath = resetCooldownOnDeath;

        return this;
    }

    public boolean shouldResetCooldownOnDeath()
    {
        return resetCooldownOnDeath;
    }

    /* Skip ability if event is cancelled */
    public Ability skipIfCancelled(boolean skipIfCancelled)
    {
        this.skipIfCancelled = skipIfCancelled;

        return this;
    }

    public boolean skipIfCancelled()
    {
        return skipIfCancelled;
    }

    /* Cancel casting if another ability is cast */
    public Ability cancelledByCast(boolean cancelledByCast)
    {
        this.cancelledByCast = cancelledByCast;

        return this;
    }

    public boolean isCancelledByCast()
    {
        return cancelledByCast;
    }

    /* Cancel ability when moving */
    public Ability cancelledByMovement(boolean cancelledByMovement)
    {
        this.cancelledByMovement = cancelledByMovement;

        return this;
    }

    public boolean isCancelledByMovement()
    {
        return cancelledByMovement;
    }

    /* Cancel ability when dealing damage */
    public Ability cancelledByDealingDamage(boolean cancelledByDealingDamage)
    {
        this.cancelledByDealingDamage = cancelledByDealingDamage;

        return this;
    }

    public boolean isCancelledByDealingDamage()
    {
        return cancelledByDealingDamage;
    }

    /* Cancel ability when taking damage */
    public Ability cancelledByTakingDamage(boolean cancelledByTakingDamage)
    {
        this.cancelledByTakingDamage = cancelledByTakingDamage;

        return this;
    }

    public boolean isCancelledByTakingDamage()
    {
        return cancelledByTakingDamage;
    }

    /* Concurrent abilities */
    public Ability ability(Ability ability)
    {
        this.abilities.add(ability);

        return this;
    }

    public Ability ability(BiConsumer<LivingEntity, Properties> on)
    {
        return ability(new FunctionAbility(on));
    }

    public Ability ability(Function<Properties, Ability> on)
    {
        return ability(new DynamicParameterAbility(on));
    }

    /* Sequenced abilities */
    public Ability then(Ability ability)
    {
        this.thenAbilities.add(ability);

        return this;
    }

    public Ability then(BiConsumer<LivingEntity, Properties> on)
    {
        return then(new FunctionAbility(on));
    }

    public Ability then(Function<Properties, Ability> on)
    {
        return then(new DynamicParameterAbility(on));
    }

    public Ability thenDelay(Duration delay)
    {
        return this.then(Ability.builder().duration(delay));
    }

    /* Concurrent last abilities */
    public Ability last(Ability ability)
    {
        this.lastAbilities.add(ability);

        return this;
    }

    public Ability last(BiConsumer<LivingEntity, Properties> on)
    {
        return last(new FunctionAbility(on));
    }

    public Ability last(Function<Properties, Ability> on)
    {
        return last(new DynamicParameterAbility(on));
    }

    /* Caster */
    public LivingEntity getCaster()
    {
        return caster;
    }

    public void setCaster(LivingEntity caster)
    {
        this.caster = caster;
    }

    /* In Progress */
    public boolean isInProgress()
    {
        if (this.inProgress) return true;

        for (var ability : this.getChildren())
        {
            if (ability.isInProgress())
            {
                return true;
            }
        }

        // if ability in progress, casting, not cancelled, waiting for then

        return false;
    }

    public void setInProgress(boolean inProgress)
    {
        this.inProgress = inProgress;
    }

    /* Evaluate Conditions */
    public Ability evaluateConditions(boolean evaluateConditions)
    {
        this.evaluateConditions = evaluateConditions;

        return this;
    }

    public boolean shouldEvaluateConditions()
    {
        return evaluateConditions;
    }

    /* Use Costs */
    public Ability useCosts(boolean useCosts)
    {
        this.useCosts = useCosts;

        return this;
    }

    public boolean shouldUseCosts()
    {
        return useCosts;
    }

    /* Show Error Messages */
    @Override
    public Ability shouldShowErrorMessages(boolean showErrors)
    {
        this.showErrors = showErrors;

        return this;
    }

    @Override
    public boolean shouldShowErrorMessages()
    {
        return this.showErrors;
    }

    /* Cooldowns */
    @Override
    public String getCooldownId()
    {
        return this.cooldownId == null ? this.getId() : cooldownId;
    }

    public Ability setCooldownId(String cooldownId)
    {
        this.cooldownId = cooldownId;

        return this;
    }

    @Override
    public double getCooldown()
    {
        return this.cooldown;
    }

    @Override
    public Ability cooldown(double cooldown)
    {
        this.cooldown = cooldown;

        return this;
    }

    @Override
    public double getRemainingCooldown()
    {
        return CooldownManager.getInstance().getRemainingTime(this.getCaster(), this, this.getLowestCooldownCharge());
    }

    @Override
    public void setRemainingCooldown(double cooldown)
    {
        CooldownManager.getInstance().setCooldown(this.getCaster(), this, this.getLowestCooldownCharge(), cooldown, true, this.isGlobalCooldown());
    }

    @Override
    public void setOnCooldown(boolean onCooldown)
    {
        this.setRemainingCooldown(onCooldown ? this.getCooldown() : 0);
    }

    @Override
    public boolean isOnCooldown()
    {
        return this.getRemainingCooldown() > 0;
    }

    @Override
    public void clearCooldown()
    {
        CooldownManager.getInstance().clearCooldown(this.getCaster(), this, this.getLowestCooldownCharge());
    }

    public void clearAllCooldowns()
    {
        for (int i = 0; i < this.getCharges(); i++)
        {
            this.clearCooldown();
        }
    }

    @Override
    public void onToggleCooldown()
    {
        // todo set all slots on 0.2s cooldown after one is used

        if (this.getAbilityProvider() != null) this.getAbilityProvider().onToggleCooldown(this);
    }

    /* Is cooldown global (saved to config) */
    public Ability globalCooldown(boolean globalCooldown)
    {
        this.globalCooldown = globalCooldown;

        return this;
    }

    public boolean isGlobalCooldown()
    {
        return globalCooldown;
    }

    /* Ability Provider */
    public AbilityProvider getAbilityProvider()
    {
        return this.abilityProvider;
    }

    public void setAbilityProvider(AbilityProvider abilityProvider)
    {
        this.abilityProvider = abilityProvider;
    }

    public void cancel()
    {
        this.cancelled = true;
        this.setInProgress(false);
        if (this.castingTask != null) this.castingTask.cancel();
        if (this.seriesTask != null) this.seriesTask.cancel();
        this.setInProgress(false);
    }

    public List<Ability> getChildren()
    {
        var allAbilities = new ArrayList<Ability>();
        allAbilities.addAll(abilities);
        allAbilities.addAll(thenAbilities);
        allAbilities.addAll(lastAbilities);

        return allAbilities;
    }

    /* Ability lifecycle methods */
    public boolean evaluateConditions(Properties properties)
    {
        Collections.sort(this.conditions);

        for (var data : this.conditions)
        {
            var result = data.condition.evaluate(this, properties);

            if (!result)
            {
                data.condition.onFail(this, properties, false);
                sendErrorMessage(properties, data.condition.getErrorMessage(this, properties, false));
                return false;
            }
        }

        return true;
    }

    public boolean evaluateCosts(Properties properties)
    {
        var costs = new ArrayList<>(this.costs);

        // loop through every cost and remove
        for (var cost : costs)
        {
            boolean result = cost.evaluate(properties);

            if (!result)
            {
                sendErrorMessage(properties, cost.getErrorMessage(properties));

                return false;
            }
        }

        return true;
    }

    private void sendErrorMessage(Properties properties, String error)
    {
        if (this.shouldShowErrorMessages() && !properties.get(AbilityProperties.TRIGGER).isPassive())
        {
            if (error != null && System.currentTimeMillis() - this.lastErrorTime >= 750)
            {
                this.getCaster().sendMessage(ChatColor.RED + error);
                //BolsterEntity.from(this.getCaster()).sendActionBar(error);
                this.lastErrorTime = System.currentTimeMillis();
            }
        }
    }

    public boolean canActivate(Properties properties)
    {
        if (!properties.contains(AbilityProperties.CASTER)) return false;
        if (this.getAbilityProvider() != null && !this.getAbilityProvider().isEnabled()) return false;
        if (!this.isEnabled()) return false;

        if (this.inProgress) return false;

        if (this.shouldEvaluateConditions() && !this.evaluateConditions(properties)) return false;
        return !this.shouldUseCosts() || this.evaluateCosts(properties);
    }

    public boolean applyCosts(Properties properties)
    {
        var costs = new ArrayList<>(this.costs);

        // loop through every cost and remove
        for (var cost : costs)
        {
            var result = cost.run(properties);

            if (!result)
            {
                return false;
            }
        }

        return true;
    }

    private void updateChildren()
    {
        for (var ability : this.getChildren())
        {
            ability.setCaster(caster);
            ability.charges(this.getCharges());
        }
    }

    public boolean activate(Properties properties)
    {
        this.updateChildren();

        if (!canActivate(properties)) return false;
        if (shouldUseCosts() && !applyCosts(properties)) return false;

        setInProgress(true);

        // TODO change system for cast time
        if (getCastTime() > 0)
        {
            var castTimeTicks = (long) (getCastTime() * 20L);
            var repeats = new AtomicLong();

            castingTask = new TaskUtil.TaskSeries()
                    .addRepeating(() -> {
                        if (!(this.getCaster() instanceof Player player)) return;

                        repeats.addAndGet(CAST_BAR_UPDATE_TICKS);

                        var percent = (repeats.floatValue() / (float) castTimeTicks);

                        player.setExp(Math.min(percent, 0.999f));
                        player.setLevel(0);

                    }, castTimeTicks, CAST_BAR_UPDATE_TICKS)
                    .addAndCancel(() -> preActivate(properties));

            return true;
        }

        preActivate(properties);
        return true;
    }

    public void activateAbilityAndChildren(Properties properties)
    {
        this.onActivate(properties);

        for (var ability : this.abilities)
        {
            ability.activate(properties);
        }

        if (this.thenAbilities.size() <= 0 && this.lastAbilities.size() <= 0) return;

        Duration childrenDuration = this.getLongestDuration(this.abilities, Duration.ZERO);

        seriesTask = new TaskUtil.TaskSeries()
                .delay(TimeUtil.toTicks(childrenDuration));

        for (var ability : this.thenAbilities)
        {
            seriesTask = seriesTask.add(() -> ability.activate(properties), TimeUtil.toTicks(ability.getDuration()));
        }

        seriesTask.add(() -> {
            for (var ability : this.lastAbilities)
            {
                ability.activate(properties);
            }
        });
    }

    private void preActivate(Properties properties)
    {
        if (this.getCastTime() > 0 && this.getCaster() instanceof Player player)
        {
            ManaManager.getInstance().updateManaDisplay(player);
        }

        if (!this.cancelled)
        {
            this.setInProgress(true);

            this.activateAbilityAndChildren(properties);

            this.onPostActivate(properties);
        }

        this.cancelled = false;
        this.castingTask = null;
        this.setInProgress(false);
    }

    public abstract void onActivate(Properties properties);

    public void onPostActivate(Properties properties)
    {
        this.setOnCooldown(true);
    }

    @Override
    public String toString()
    {
        return this.getClass() + " (id: " + this.getId() + ", cooldown id: " + this.getCooldownId() + ", " + this.getName() + ")";
    }

    public void destroy()
    {
        HandlerList.unregisterAll(this);

        for (var child : this.getChildren())
        {
            child.destroy();
        }

        this.abilities.clear();
        this.thenAbilities.clear();
        this.lastAbilities.clear();
    }

    private static class AbilityImpl extends Ability
    {
        @Override
        public void loadConfig(ConfigurationSection config)
        {

        }

        @Override
        public void onActivate(Properties properties)
        {

        }
    }
}
