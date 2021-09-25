package co.runed.merlin.triggers.inventory;

import co.runed.merlin.triggers.AbstractItemEventTrigger;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ArmorChangeTrigger extends AbstractItemEventTrigger<PlayerArmorChangeEvent> {
    private PlayerArmorChangeEvent.SlotType slotType;

    public ArmorChangeTrigger(PlayerArmorChangeEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }

    public PlayerArmorChangeEvent.SlotType getSlotType() {
        return getBaseEvent().getSlotType();
    }
}
