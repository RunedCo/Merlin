package co.runed.merlin.listeners;

import co.runed.merlin.core.AbilityManager;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractAtEntityListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event)
    {
        Player player = event.getPlayer();
        EntityEquipment inv = player.getEquipment();
        ItemStack stack = inv.getItemInMainHand();

        Properties properties = new Properties();
        properties.set(AbilityProperties.EVENT, event);
        properties.set(AbilityProperties.ITEM_STACK, stack);
        properties.set(AbilityProperties.TARGET, event.getRightClicked());

        AbilityManager.getInstance().trigger(player, AbilityTrigger.RIGHT_CLICK_ENTITY, properties);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractedWith(PlayerInteractAtEntityEvent event)
    {
        if (!(event.getRightClicked() instanceof LivingEntity)) return;

        LivingEntity entity = (LivingEntity) event.getRightClicked();
        EntityEquipment inv = entity.getEquipment();
        ItemStack stack = inv.getItemInMainHand();

        Properties properties = new Properties();
        properties.set(AbilityProperties.EVENT, event);
        properties.set(AbilityProperties.ITEM_STACK, stack);
        properties.set(AbilityProperties.TARGET, event.getPlayer());

        AbilityManager.getInstance().trigger(entity, AbilityTrigger.ON_INTERACTED_WITH, properties);
    }
}
