package co.runed.merlin.triggers.movement;

import co.runed.merlin.core.SpellManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PlayerPortalListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEnterPortal(PlayerPortalEvent event) {
        var player = event.getPlayer();

        SpellManager.getInstance().run(player, new EnterPortalTrigger(event));
    }
}
