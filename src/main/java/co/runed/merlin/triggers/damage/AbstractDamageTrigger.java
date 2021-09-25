package co.runed.merlin.triggers.damage;

import co.runed.bolster.util.BukkitUtil;
import co.runed.merlin.triggers.AbstractItemEventTrigger;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDamageTrigger extends AbstractItemEventTrigger<EntityDamageEvent> {
    public AbstractDamageTrigger(EntityDamageEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }

    public EntityDamageEvent.DamageCause getCause() {
        return getBaseEvent().getCause();
    }

    public double getFinalDamage() {
        return getBaseEvent().getFinalDamage();
    }

    public double getDamage() {
        return getBaseEvent().getDamage();
    }

    public double getDamage(EntityDamageEvent.DamageModifier type) {
        return getBaseEvent().getDamage(type);
    }

    public double getOriginalDamage(EntityDamageEvent.DamageModifier type) {
        return getBaseEvent().getOriginalDamage(type);
    }

    public void setDamage(double damage) {
        getBaseEvent().setDamage(damage);
    }

    public void setDamage(EntityDamageEvent.DamageModifier type, double damage) {
        getBaseEvent().setDamage(type, damage);
    }

    public @Nullable LivingEntity getDamager() {
        return BukkitUtil.getDamagerFromEvent(getBaseEvent());
    }
}
