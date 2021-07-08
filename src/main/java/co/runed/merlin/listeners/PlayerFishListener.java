package co.runed.merlin.listeners;

import co.runed.merlin.core.AbilityManager;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Event that triggers casting an ability when a fish is caught
 */
public class PlayerFishListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerFish(PlayerFishEvent event)
    {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_ENTITY && event.getState() != PlayerFishEvent.State.CAUGHT_FISH)
            return;

        Player player = event.getPlayer();
        ItemStack stack = player.getInventory().getItemInMainHand();

        Properties properties = new Properties();
        properties.set(AbilityProperties.EVENT, event);
        properties.set(AbilityProperties.CAUGHT, event.getCaught());
        properties.set(AbilityProperties.HOOK, event.getHook());
        properties.set(AbilityProperties.ITEM_STACK, stack);
        properties.set(AbilityProperties.FISH_STATE, event.getState());

        AbilityManager.getInstance().trigger(player, AbilityTrigger.ON_CATCH_FISH, properties);
    }
}
