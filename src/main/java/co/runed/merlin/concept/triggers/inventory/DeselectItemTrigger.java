package co.runed.merlin.concept.triggers.inventory;

import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class DeselectItemTrigger extends SelectItemTrigger {
    public DeselectItemTrigger(Event baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
