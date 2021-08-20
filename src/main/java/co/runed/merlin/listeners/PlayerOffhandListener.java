package co.runed.merlin.listeners;

import co.runed.merlin.core.AbilityManager;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Event that triggers casting an ability on swapping offhand (pushing F)
 */
public class PlayerOffhandListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerOffhand(PlayerSwapHandItemsEvent event)
    {
        Player player = event.getPlayer();
        ItemStack stack = event.getOffHandItem();

        Properties properties = new Properties();
        properties.set(AbilityProperties.EVENT, event);
        properties.set(AbilityProperties.ITEM_STACK, stack);

        event.setCancelled(true);

        AbilityManager.getInstance().trigger(player, AbilityTrigger.ON_SWAP_OFFHAND, properties);
    }
}
