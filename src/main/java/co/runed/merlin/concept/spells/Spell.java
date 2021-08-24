package co.runed.merlin.concept.spells;

import co.runed.bolster.common.util.IDescribable;
import co.runed.bolster.common.util.IIdentifiable;
import co.runed.bolster.common.util.INameable;
import co.runed.bolster.damage.DamageSource;
import co.runed.bolster.util.IOwned;
import co.runed.bolster.util.config.IConfigurable;
import co.runed.merlin.concept.CastContext;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

public abstract class Spell implements IIdentifiable, INameable, IDescribable, IConfigurable, IOwned, DamageSource {
    SpellDefinition container;

    boolean enabled = true;
    boolean initialised = false;

    double cooldown = 0;

    LivingEntity owner;
    SpellProvider parent;

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isInitialised() {
        return initialised;
    }

    public SpellDefinition getContainer() {
        return container;
    }

    public void setParent(SpellProvider parent) {
        this.parent = parent;
    }

    public SpellProvider getParent() {
        return parent;
    }

    public CastResult preCast(CastContext context) {
        if (!isInitialised() && isEnabled()) return CastResult.fail();
        if (isOnCooldown() && !hasOption(SpellOption.IGNORE_COOLDOWN)) return CastResult.fail("Ability is on cooldown!");

        var costResult = evaluateCosts(context);
        if (!costResult.isSuccess() && !hasOption(SpellOption.IGNORE_COSTS)) return costResult;

        // TODO costs?

        return CastResult.success();
    }

    public CastResult evaluateCosts(CastContext context) {
        for (var cost : getContainer().getCosts()) {
            var result = cost.evaluate(context);

            if (!result.isSuccess()) return result;
        }

        return CastResult.success();
    }

    public void runCosts(CastContext context) {
        for (var cost : getContainer().getCosts()) {
            cost.run(context);
        }
    }

    public CastResult postCast(CastContext context) {
        if (!hasOption(SpellOption.IGNORE_COSTS)) runCosts(context);

        return CastResult.success();
    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        this.enabled = config.getBoolean("enabled", enabled);
        this.cooldown = config.getDouble("cooldown", cooldown);
    }

    @Override
    public void create() {

    }

    @Override
    public String getDescription() {
        return getContainer().getDescription();
    }

    @Override
    public String getId() {
        return getContainer().getId();
    }

    @Override
    public String getName() {
        if (getContainer().getName() == null) return getId();

        return getContainer().getName();
    }

    @Override
    public LivingEntity getOwner() {
        return owner;
    }

    @Override
    public void setOwner(LivingEntity entity) {
        this.owner = entity;
    }

    public boolean hasOption(SpellOption option) {
        return getContainer().hasOption(option);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpellDefinition container) {
            return container.equals(getContainer());
        }

        return super.equals(obj);
    }

    public void destroy() {

    }
}
