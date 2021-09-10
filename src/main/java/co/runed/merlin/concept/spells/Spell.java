package co.runed.merlin.concept.spells;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.damage.DamageSource;
import co.runed.bolster.managers.CooldownManager;
import co.runed.bolster.util.Owned;
import co.runed.bolster.util.config.ConfigUtil;
import co.runed.bolster.util.config.Configurable;
import co.runed.bolster.util.cooldown.CooldownSource;
import co.runed.bolster.util.lang.Lang;
import co.runed.bolster.util.lang.LangProvider;
import co.runed.dayroom.util.Describable;
import co.runed.dayroom.util.Enableable;
import co.runed.dayroom.util.Identifiable;
import co.runed.dayroom.util.Nameable;
import co.runed.merlin.Merlin;
import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.costs.Cost;
import co.runed.merlin.concept.spells.type.SpellType;
import co.runed.merlin.concept.spells.type.ToggleSpellType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Spell implements Identifiable, Nameable, Describable, Configurable, Owned, DamageSource, Enableable, CooldownSource<Spell>, LangProvider {
    private final SpellDefinition definition;

    private boolean enabled = true;
    private boolean initialised = false;
    private String cooldownId = null;

    private double cooldown = 0;
    private int charges = 1;
    private int priority = 1;
    private SpellType spellType;
    private boolean toggled = false;

    private LivingEntity owner;
    private SpellProvider parent;
    private List<Cost> instanceCosts = new ArrayList<>();

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

    public SpellDefinition getDefinition() {
        return definition;
    }

    public void setParent(SpellProvider parent) {
        this.parent = parent;
    }

    public SpellProvider getParent() {
        return parent;
    }

    public CastResult preCast(CastContext context) {
        instanceCosts.clear();

        if (!isInitialised() && isEnabled()) return CastResult.fail();

        if (!hasOption(SpellOption.IGNORE_CANCELLED) && context.isCancelled()) return CastResult.fail();

        var parentResult = getParent().preCast(context);
        if (!parentResult.isSuccess()) return parentResult;

        if (isOnCooldown() && !hasOption(SpellOption.IGNORE_COOLDOWN)) return CastResult.fail(Lang.key("spell." + getId() + ".msg.cooldown", "spell.msg.cooldown").with(this).toString());

        var costResult = evaluateCosts(context);
        if (!costResult.isSuccess() && !hasOption(SpellOption.IGNORE_COSTS)) return costResult;

        return CastResult.success();
    }

    public CastResult evaluateCosts(CastContext context) {
        var fetchedCosts = new ArrayList<Cost>();

        for (var costSupplier : getDefinition().getCosts()) {
            var cost = costSupplier.get();
            fetchedCosts.add(cost);

            var result = cost.evaluate(context);

            if (!result.isSuccess()) return result;
        }

        instanceCosts.addAll(fetchedCosts);

        return CastResult.success();
    }

    public void runCosts(CastContext context) {
        for (var cost : instanceCosts) {
            cost.run(context);
        }

        instanceCosts.clear();
    }

    public CastResult postCast(CastContext context) {
        getParent().postCast(context);

        if (!hasOption(SpellOption.IGNORE_COSTS)) runCosts(context);
        if (!hasOption(SpellOption.IGNORE_COOLDOWN)) setOnCooldown(true);

        return CastResult.success();
    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        enabled = config.getBoolean("enabled", enabled);
        cooldown = config.getDouble("cooldown", cooldown);
        cooldownId = config.getString("shared-cooldown-id", cooldownId);
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

    @Override
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

    public boolean hasOption(SpellOption option) {
        return getDefinition().hasOption(option);
    }

    public void setType(SpellType spellType) {
        this.spellType = spellType;
    }

    public SpellType getType() {
        return spellType;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        if (!(getType() instanceof ToggleSpellType)) {
            Merlin.getInstance().getLogger().warning("Tried to toggle non-toggleable spell " + getId() + " with parent " + getParent().getId());
        }

        this.toggled = toggled;
    }

    /* Damage Source Stuff */
    @Override
    public String getDeathMessage(Player killer, Player victim, DamageInfo damageInfo) {
        return null;
    }

    @Override
    public DamageSource next() {
        return getParent();
    }

    /* Language Provider */
    @Override
    public Map<String, String> getLangKeys() {
        var map = new HashMap<String, String>();

        map.putAll(ConfigUtil.toStringMap(getConfig(), true));

        map.put("spell", getName());
        map.put("caster", owner.getName());
        map.put("player", owner.getName());
        map.put("cooldown-r", CooldownManager.formatCooldown(getRemainingCooldown()));

        return map;
    }

    /* Cooldowns */
    @Override
    public Spell cooldown(double cooldown) {
        return null;
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
        return this.cooldownId == null ? getId() : cooldownId;
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

    public void destroy() {

    }
}
