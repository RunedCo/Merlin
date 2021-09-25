package co.runed.merlin.triggers.damage;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class DamageTrigger extends AbstractDamageTrigger {
    public DamageTrigger(EntityDamageEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
