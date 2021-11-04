package co.runed.merlin.triggers.damage;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.damage.DamageType;
import co.runed.bolster.events.entity.EntityDamageInfoEvent;
import co.runed.bolster.util.BukkitUtil;
import co.runed.merlin.triggers.AbstractItemEventTrigger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDamageTrigger extends AbstractItemEventTrigger<EntityDamageInfoEvent> {
    public AbstractDamageTrigger(EntityDamageInfoEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }

    public DamageInfo getDamageInfo() {
        return getBaseEvent().getDamageInfo();
    }

    public DamageType getType() {
        return getDamageInfo().getDamageType();
    }

    public double getFinalDamage() {
        return getBaseEvent().getFinalDamage();
    }

    public double getDamage() {
        return getBaseEvent().getDamage();
    }

    public double getDamage(EntityDamageEvent.DamageModifier type) {
        return getWrappedEvent().getDamage(type);
    }

    public double getOriginalDamage(EntityDamageEvent.DamageModifier type) {
        return getWrappedEvent().getOriginalDamage(type);
    }

    public void setDamage(double damage) {
        getBaseEvent().setDamage(damage);
    }

    public void setDamage(EntityDamageEvent.DamageModifier type, double damage) {
        getWrappedEvent().setDamage(type, damage);
    }

    public EntityDamageEvent getWrappedEvent() {
        return getBaseEvent().getWrappedEvent();
    }

    public Entity getTargetEntity() {
        return getBaseEvent().getEntity();
    }

    public @Nullable LivingEntity getTarget() {
        if (getTargetEntity() instanceof LivingEntity livingEntity) return livingEntity;

        return null;
    }

    public @Nullable LivingEntity getDamager() {
        return BukkitUtil.getDamagerFromEvent(getBaseEvent());
    }
}
