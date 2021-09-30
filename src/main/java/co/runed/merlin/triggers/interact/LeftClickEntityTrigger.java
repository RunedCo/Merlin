package co.runed.merlin.triggers.interact;

import co.runed.bolster.events.entity.EntityDamageInfoEvent;
import co.runed.merlin.triggers.damage.AbstractDamageTrigger;
import org.bukkit.inventory.ItemStack;

public class LeftClickEntityTrigger extends AbstractDamageTrigger {
    public LeftClickEntityTrigger(EntityDamageInfoEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
