package co.runed.merlin.spells;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.damage.DamageSource;
import co.runed.bolster.managers.CooldownManager;
import co.runed.bolster.util.Owned;
import co.runed.bolster.util.TimeUtil;
import co.runed.bolster.util.config.ConfigEntry;
import co.runed.bolster.util.config.ConfigUtil;
import co.runed.bolster.util.config.Configurable;
import co.runed.bolster.util.cooldown.CooldownSource;
import co.runed.bolster.util.lang.Lang;
import co.runed.bolster.util.lang.LangProvider;
import co.runed.bolster.util.task.TaskUtil;
import co.runed.dayroom.util.Describable;
import co.runed.dayroom.util.Enableable;
import co.runed.dayroom.util.Identifiable;
import co.runed.dayroom.util.Nameable;
import co.runed.merlin.costs.Cost;
import co.runed.merlin.spells.type.SpellType;
import co.runed.merlin.triggers.Trigger;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Spell implements Identifiable, Nameable, Describable, Configurable, Owned, DamageSource, Enableable, CooldownSource<Spell>, LangProvider {
    private final SpellDefinition definition;

    @ConfigEntry(key = "enabled")
    private boolean enabled = true;
    @ConfigEntry(key = "cooldown")
    private double cooldown = 0;
    @ConfigEntry(key = "charges")
    private int charges = 1;
    @ConfigEntry(key = "priority")
    private int priority = 1;
    @ConfigEntry(key = "shared-cooldown-id")
    private String cooldownId = null;
    @ConfigEntry(key = "cast-time")
    private double castTime = 0;

    private boolean initialised = false;
    private SpellType spellType;
    private boolean toggled = false;
    private boolean casting = false;
    private TaskUtil.TaskSeries castingTask = null;
    private ZonedDateTime lastCastTimestamp = null;

    private LivingEntity owner;
    private SpellProvider parent;
    private List<Cost> instanceCosts = new ArrayList<>();
    private ConfigurationSection config = ConfigUtil.create();

    public Spell(@NotNull SpellDefinition definition) {
        this.definition = definition;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isInitialised() {
        return initialised;
    }

    public void initialise() {
        this.initialised = true;
    }

    public boolean isCasting() {
        return casting;
    }

    public void setCasting(boolean casting) {
        this.casting = casting;
    }

    public void cancel() {
        setCasting(false);
        if (castingTask != null) castingTask.cancel();
    }

    public SpellDefinition getDefinition() {
        return definition;
    }

    public void setParent(SpellProvider parent) {
        this.parent = parent;
    }

    public SpellProvider getParent() {
        return parent;
    }

    public CastResult preCast(Trigger trigger) {
        instanceCosts.clear();

        if (!isInitialised() && isEnabled()) return CastResult.fail();
        if (isCasting()) return CastResult.fail();

        if (!hasOption(SpellOption.IGNORE_CANCELLED) && trigger.isCancelled()) return CastResult.fail();

        var parentResult = getParent().preCast(trigger);
        if (!parentResult.isSuccess()) return parentResult;

        if (isOnCooldown() && !hasOption(SpellOption.IGNORE_COOLDOWN)) return CastResult.fail(Lang.key("spell." + getId() + ".msg.cooldown", "spell.msg.cooldown").with(this).toString());

        var costResult = evaluateCosts(trigger);
        if (!costResult.isSuccess() && !hasOption(SpellOption.IGNORE_COSTS)) return costResult;

        lastCastTimestamp = TimeUtil.now();

        return CastResult.success();
    }

    public CastResult evaluateCosts(Trigger trigger) {
        var fetchedCosts = new ArrayList<Cost>();

        for (var costSupplier : getDefinition().getCosts()) {
            var cost = costSupplier.get();
            fetchedCosts.add(cost);

            var result = cost.evaluate(trigger);

            if (!result.isSuccess()) return result;
        }

        instanceCosts.addAll(fetchedCosts);

        return CastResult.success();
    }

    public void runCosts(Trigger trigger) {
        for (var cost : instanceCosts) {
            cost.run(trigger);
        }

        instanceCosts.clear();
    }

    public CastResult postCast(Trigger trigger) {
        getParent().postCast(trigger);

        if (!hasOption(SpellOption.IGNORE_COSTS)) runCosts(trigger);
        if (!hasOption(SpellOption.IGNORE_COOLDOWN)) setOnCooldown(true);

        return CastResult.success();
    }

    @Override
    public String getDescription() {
        return getDefinition().getDescription();
    }

    @Override
    public String getId() {
        return getDefinition().getId();
    }

    @Override
    public String getName() {
        if (getDefinition().getName() == null) return getId();

        return getDefinition().getName();
    }

    @Override
    public LivingEntity getOwner() {
        return owner;
    }

    public void setOwner(LivingEntity entity) {
        this.owner = entity;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    public void setCastTime(double castTime) {
        this.castTime = castTime;
    }

    public double getCastTime() {
        return castTime;
    }

    public void setCastingTask(TaskUtil.TaskSeries castingTask) {
        this.castingTask = castingTask;
    }

    public TaskUtil.TaskSeries getCastingTask() {
        return castingTask;
    }

    public boolean hasOption(SpellOption option) {
        return getDefinition().hasOption(option);
    }

    public void setType(SpellType spellType) {
        this.spellType = spellType;
    }

    public SpellType getType() {
        return spellType;
    }

    public ZonedDateTime getLastCastTimestamp() {
        return lastCastTimestamp;
    }

    /* Damage Source Stuff */
    @Override
    public Component getDeathMessage(LivingEntity killer, Player victim, DamageInfo damageInfo) {
        return null;
    }

    @Override
    public DamageSource next() {
        return getParent();
    }

    /* Language Provider */
    @Override
    public Map<String, String> getLangReplacements() {
        var map = new HashMap<String, String>();

        map.putAll(ConfigUtil.toStringMap(getConfig(), true));

        map.put("spell", getName());
        map.put("caster", owner.getName());
        map.put("player", owner.getName());
        map.put("cooldown-r", CooldownManager.formatCooldown(getRemainingCooldown()));

        return map;
    }

    @Override
    public Map<String, String> getLangSource() {
        var map = new HashMap<String, String>();

        if (getConfig().isConfigurationSection("lang")) {
            map.putAll(ConfigUtil.toStringMap(getConfig().getConfigurationSection("lang"), true));
        }

        return map;
    }

    /* Configuration */
    @Override
    public void setConfig(ConfigurationSection config) {
        this.config = config;
    }

    @Override
    public ConfigurationSection getConfig() {
        return config;
    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        ConfigUtil.loadAnnotatedConfig(config, this);
    }

    /* Cooldowns */
    @Override
    public Spell cooldown(double cooldown) {
        setCooldown(cooldown);

        return this;
    }

    @Override
    public boolean isGlobalCooldown() {
        return false;
    }

    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public int getLowestCooldownCharge() {
        var charge = 0;
        var lowest = this.getCooldown();

        for (var i = 0; i < this.getCharges(); i++) {
            var remaining = CooldownManager.getInstance().getRemainingTime(this.getOwner(), this, i);

            if (remaining < lowest) {
                lowest = remaining;
                charge = i;

                if (lowest <= 0) break;
            }
        }

        return charge;
    }

    @Override
    public String getCooldownId() {
        return this.cooldownId == null ? getParent().getId() + "." + getId() : cooldownId;
    }

    @Override
    public double getRemainingCooldown() {
        return CooldownManager.getInstance().getRemainingTime(getOwner(), this, getLowestCooldownCharge());
    }

    @Override
    public void setRemainingCooldown(double cooldown) {
        CooldownManager.getInstance().setCooldown(getOwner(), this, getLowestCooldownCharge(), cooldown, true, isGlobalCooldown());
    }

    @Override
    public void setOnCooldown(boolean onCooldown) {
        this.setRemainingCooldown(onCooldown ? getCooldown() : 0);
    }

    @Override
    public boolean isOnCooldown() {
        return this.getRemainingCooldown() > 0;
    }

    @Override
    public void clearCooldown() {
        CooldownManager.getInstance().clearCooldown(getOwner(), this, getLowestCooldownCharge());
    }

    public void clearAllCooldowns() {
        for (var i = 0; i < this.getCharges(); i++) {
            this.clearCooldown();
        }
    }

    @Override
    public void onToggleCooldown() {
        // todo set all slots on 0.2s cooldown after one is used
    }

    /* Lifecycle */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpellDefinition container) {
            return container.equals(getDefinition());
        }

        return super.equals(obj);
    }
}
