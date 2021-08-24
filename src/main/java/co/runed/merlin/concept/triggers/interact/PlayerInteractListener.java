package co.runed.merlin.concept.triggers.interact;

import co.runed.merlin.concept.spells.Spells;
import org.bukkit.entity.Player;
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

        Player player = event.getPlayer();

        if (event.getAction() == Action.PHYSICAL) return;

        Spells.getInstance().run(player, InteractTrigger.class, (spell, context) -> spell.onClick(context, new InteractParams(event)));
    }
}