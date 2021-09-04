package co.runed.merlin.concept.triggers.interact;

import co.runed.merlin.concept.spells.SpellManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Event that triggers casting an ability on left or right click
 */
public class PlayerInteractListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        var player = event.getPlayer();

        if (event.getAction() == Action.PHYSICAL) return;

        var params = new InteractParams(event);

        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            SpellManager.getInstance().run(player, LeftClickTrigger.class, (spell, context) -> spell.onLeftClick(context.setParams(params), params));
        }
        else {
            SpellManager.getInstance().run(player, RightClickTrigger.class, (spell, context) -> spell.onRightClick(context.setParams(params), params));
        }
    }
}