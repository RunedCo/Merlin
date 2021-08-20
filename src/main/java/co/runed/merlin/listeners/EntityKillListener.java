package co.runed.merlin.listeners;

import co.runed.merlin.core.AbilityManager;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Event that triggers casting an ability when an entity kills an entity
 */
public class EntityKillListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onKillEntity(EntityDeathEvent event)
    {
        Player player = event.getEntity().getKiller();

        if (player == null) return;

        ItemStack stack = player.getInventory().getItemInMainHand();

        Properties properties = new Properties();
        properties.set(AbilityProperties.ITEM_STACK, stack);
        properties.set(AbilityProperties.EVENT, event);
        properties.set(AbilityProperties.TARGET, event.getEntity());
        properties.set(AbilityProperties.DROPS, event.getDrops());

        AbilityManager.getInstance().trigger(player, AbilityTrigger.ON_KILL_ENTITY, properties);
    }
}
