package co.runed.merlin.triggers.damage;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class TakeDamageTrigger extends AbstractDamageTrigger {
    public TakeDamageTrigger(EntityDamageEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
