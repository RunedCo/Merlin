package co.runed.merlin.triggers.lifecycle;

import co.runed.merlin.core.SpellManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerConnect(PlayerJoinEvent event) {
        var player = event.getPlayer();

        SpellManager.getInstance().run(player, new ConnectTrigger(event));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerDisconnect(PlayerQuitEvent event) {
        var player = event.getPlayer();

        SpellManager.getInstance().run(player, new DisconnectTrigger(event));
    }
}
