package co.runed.merlin.triggers.damage;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class FatalDamageTrigger extends AbstractDamageTrigger {
    public FatalDamageTrigger(EntityDamageEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
