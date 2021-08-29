package co.runed.merlin.concept.triggers;

import co.runed.merlin.concept.items.ItemDefinition;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractItemEventParams<T extends Event> extends EventParams<T> {
    private final ItemStack itemStack;

    public AbstractItemEventParams(T baseEvent, ItemStack itemStack) {
        super(baseEvent);

        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isItem(ItemDefinition item) {
        return true;
    }

}
