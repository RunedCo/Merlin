package co.runed.merlin.triggers;

import co.runed.merlin.items.ItemDefinition;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractItemEventTrigger<T extends Event> extends EventTrigger<T> {
    private final ItemStack itemStack;

    public AbstractItemEventTrigger(T baseEvent, ItemStack itemStack) {
        super(baseEvent);

        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isItem(@NotNull ItemDefinition item) {
        return item.equals(ItemDefinition.from(getItemStack()));
    }
}
