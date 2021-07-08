package co.runed.merlin.listeners;

import co.runed.merlin.core.AbilityManager;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerPortalListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEnterPortal(PlayerPortalEvent event)
    {
        Player player = event.getPlayer();
        ItemStack stack = player.getInventory().getItemInMainHand();

        Properties properties = new Properties();
        properties.set(AbilityProperties.EVENT, event);
        properties.set(AbilityProperties.ITEM_STACK, stack);

        AbilityManager.getInstance().trigger(player, AbilityTrigger.ON_ENTER_PORTAL, properties);
    }
}
