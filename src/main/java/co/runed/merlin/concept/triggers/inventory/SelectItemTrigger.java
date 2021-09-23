package co.runed.merlin.concept.triggers.inventory;

import co.runed.merlin.concept.triggers.AbstractItemEventTrigger;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class SelectItemTrigger extends AbstractItemEventTrigger<Event> {
    public SelectItemTrigger(Event baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
