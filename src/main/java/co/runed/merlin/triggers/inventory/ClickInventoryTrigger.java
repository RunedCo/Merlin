package co.runed.merlin.triggers.inventory;

import co.runed.merlin.triggers.AbstractItemEventTrigger;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClickInventoryTrigger extends AbstractItemEventTrigger<InventoryClickEvent> {
    public ClickInventoryTrigger(InventoryClickEvent baseEvent) {
        super(baseEvent, baseEvent.getCurrentItem());
    }

    @NotNull
    public InventoryType.SlotType getSlotType() {
        return getBaseEvent().getSlotType();
    }

    @Nullable
    public ItemStack getCursor() {
        return getBaseEvent().getCursor();
    }

    @Nullable
    public ItemStack getCurrentItem() {
        return getBaseEvent().getCurrentItem();
    }

    public boolean isRightClick() {
        return getBaseEvent().isRightClick();
    }

    public boolean isLeftClick() {
        return getBaseEvent().isLeftClick();
    }

    public boolean isShiftClick() {
        return getBaseEvent().isShiftClick();
    }

    public void setCurrentItem(@Nullable ItemStack stack) {
        getBaseEvent().setCurrentItem(stack);
    }

    @Nullable
    public Inventory getClickedInventory() {
        return getBaseEvent().getClickedInventory();
    }

    public int getSlot() {
        return getBaseEvent().getSlot();
    }

    public int getRawSlot() {
        return getBaseEvent().getRawSlot();
    }

    public int getHotbarButton() {
        return getBaseEvent().getHotbarButton();
    }

    @NotNull
    public InventoryAction getAction() {
        return getBaseEvent().getAction();
    }

    @NotNull
    public ClickType getClick() {
        return getBaseEvent().getClick();
    }
}
