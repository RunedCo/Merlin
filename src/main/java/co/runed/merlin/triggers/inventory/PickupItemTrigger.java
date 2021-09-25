package co.runed.merlin.triggers.inventory;

import co.runed.merlin.triggers.AbstractItemEventTrigger;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PickupItemTrigger extends AbstractItemEventTrigger<EntityPickupItemEvent> {
    public PickupItemTrigger(EntityPickupItemEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
