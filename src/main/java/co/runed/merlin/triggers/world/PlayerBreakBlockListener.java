package co.runed.merlin.triggers.world;

import co.runed.merlin.core.SpellManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;

/**
 * Event that triggers casting an ability when a block is broken
 */
public class PlayerBreakBlockListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerBreakBlock(BlockBreakEvent event) {
        var player = event.getPlayer();

        SpellManager.getInstance().run(player, new BreakBlockTrigger(event));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerDamageBlock(BlockDamageEvent event) {
        var player = event.getPlayer();

        SpellManager.getInstance().run(player, new DamageBlockTrigger(event));
    }
}
