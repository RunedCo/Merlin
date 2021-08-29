package co.runed.merlin.concept.triggers.inventory;

import co.runed.merlin.concept.triggers.AbstractItemEventParams;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class SelectItemParams extends AbstractItemEventParams<Event> {
    public SelectItemParams(Event baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
