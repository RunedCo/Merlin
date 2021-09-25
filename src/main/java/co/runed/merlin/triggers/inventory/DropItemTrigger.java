package co.runed.merlin.triggers.inventory;

import co.runed.merlin.triggers.AbstractItemEventTrigger;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class DropItemTrigger extends AbstractItemEventTrigger<PlayerDropItemEvent> {
    public DropItemTrigger(PlayerDropItemEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
