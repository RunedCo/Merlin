package co.runed.merlin.triggers.interact;

import co.runed.merlin.triggers.damage.AbstractDamageTrigger;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class LeftClickEntityTrigger extends AbstractDamageTrigger {
    public LeftClickEntityTrigger(EntityDamageEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
