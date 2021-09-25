package co.runed.merlin.triggers.item;

import co.runed.merlin.triggers.AbstractItemEventTrigger;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class ConsumeItemTrigger extends AbstractItemEventTrigger<PlayerItemConsumeEvent> {
    public ConsumeItemTrigger(PlayerItemConsumeEvent baseEvent, ItemStack itemStack) {
        super(baseEvent, itemStack);
    }
}
