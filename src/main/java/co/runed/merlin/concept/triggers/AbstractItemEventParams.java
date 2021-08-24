package co.runed.merlin.concept.triggers;

import co.runed.merlin.concept.items.ItemDefinition;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractItemEventParams<T extends Event> extends EventParams<T> {
    private final ItemStack itemStack;
    private final EquipmentSlot equipmentSlot;

    public AbstractItemEventParams(T baseEvent, ItemStack itemStack, EquipmentSlot equipmentSlot) {
        super(baseEvent);

        this.itemStack = itemStack;
        this.equipmentSlot = equipmentSlot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isItem(ItemDefinition item) {
        return true;
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }
}
