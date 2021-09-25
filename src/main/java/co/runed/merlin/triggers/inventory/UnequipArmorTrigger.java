package co.runed.merlin.triggers.inventory;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.inventory.ItemStack;

public class UnequipArmorTrigger extends ArmorChangeTrigger {
    public UnequipArmorTrigger(PlayerArmorChangeEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
