package co.runed.merlin.listeners;

import co.runed.merlin.core.AbilityManager;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerConnect(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        Properties properties = new Properties();
        properties.set(AbilityProperties.EVENT, event);

        AbilityManager.getInstance().trigger(player, AbilityTrigger.ON_CONNECT, properties);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerDisconnect(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        Properties properties = new Properties();
        properties.set(AbilityProperties.EVENT, event);

        AbilityManager.getInstance().trigger(player, AbilityTrigger.ON_DISCONNECT, properties);
    }
}
