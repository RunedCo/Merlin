package co.runed.merlin.triggers.item;

import co.runed.merlin.core.SpellManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Event that triggers casting an ability when a fish is caught
 */
public class PlayerFishListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_ENTITY && event.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;

        var player = event.getPlayer();

        SpellManager.getInstance().run(player, new CatchFishTrigger(event));
    }
}
