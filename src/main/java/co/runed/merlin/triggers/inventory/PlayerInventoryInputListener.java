package co.runed.merlin.triggers.inventory;

import co.runed.merlin.core.SpellManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

/**
 * Event that triggers casting an ability on swapping offhand (pushing F), dropping, or picking up items
 */
public class PlayerInventoryInputListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerOffhand(PlayerSwapHandItemsEvent event) {
        var player = event.getPlayer();
        var stack = event.getOffHandItem();

        SpellManager.getInstance().run(player, new OffhandSwapTrigger(event, stack));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDropItem(PlayerDropItemEvent event) {
        var player = event.getPlayer();
        var stack = event.getItemDrop().getItemStack();

        SpellManager.getInstance().run(player, new DropItemTrigger(event, stack));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPickupItem(EntityPickupItemEvent event) {
        var entity = event.getEntity();
        var stack = event.getItem().getItemStack();

        SpellManager.getInstance().run(entity, new PickupItemTrigger(event, stack));
    }
}
