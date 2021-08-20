package co.runed.merlin.listeners;

import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import co.runed.merlin.core.AbilityManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Event that triggers casting an ability on left or right click
 */
public class PlayerInteractListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack stack = event.getItem();

        Properties properties = new Properties();
        properties.set(AbilityProperties.ITEM_STACK, stack);
        properties.set(AbilityProperties.BLOCK_ACTION, event.getAction());
        properties.set(AbilityProperties.BLOCK, event.getClickedBlock());
        properties.set(AbilityProperties.BLOCK_FACE, event.getBlockFace());
        properties.set(AbilityProperties.EVENT, event);

        AbilityTrigger trigger = null;

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            trigger = event.getAction() == Action.LEFT_CLICK_AIR ? AbilityTrigger.LEFT_CLICK_AIR : AbilityTrigger.LEFT_CLICK_BLOCK;

            if (player.isSneaking())
            {
                AbilityManager.getInstance().trigger(player, AbilityTrigger.SHIFT_LEFT_CLICK, properties);
            }

            AbilityManager.getInstance().trigger(player, AbilityTrigger.LEFT_CLICK, properties);
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            trigger = event.getAction() == Action.RIGHT_CLICK_AIR ? AbilityTrigger.RIGHT_CLICK_AIR : AbilityTrigger.RIGHT_CLICK_BLOCK;

            if (player.isSneaking())
            {
                AbilityManager.getInstance().trigger(player, AbilityTrigger.SHIFT_RIGHT_CLICK, properties);
            }

            AbilityManager.getInstance().trigger(player, AbilityTrigger.RIGHT_CLICK, properties);
        }

        AbilityManager.getInstance().trigger(player, trigger, properties);
    }
}
