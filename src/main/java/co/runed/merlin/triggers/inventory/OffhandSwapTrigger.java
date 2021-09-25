package co.runed.merlin.triggers.inventory;

import co.runed.merlin.triggers.AbstractItemEventTrigger;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class OffhandSwapTrigger extends AbstractItemEventTrigger<PlayerSwapHandItemsEvent> {
    public OffhandSwapTrigger(PlayerSwapHandItemsEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
