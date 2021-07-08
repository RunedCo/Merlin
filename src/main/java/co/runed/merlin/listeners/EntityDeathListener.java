package co.runed.merlin.listeners;

import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class EntityDeathListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntityDeath(EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();
        EntityEquipment inv = entity.getEquipment();
        ItemStack stack = inv.getItemInMainHand();

        Properties properties = new Properties();
        properties.set(AbilityProperties.ITEM_STACK, stack);
        properties.set(AbilityProperties.EVENT, event);

        AbilityTrigger.ON_DEATH.trigger(entity, properties);

        Properties globalDeathProperties = new Properties(properties);
        globalDeathProperties.set(AbilityProperties.TARGET, entity);

        AbilityTrigger.ON_ENTITY_DEATH.triggerAll(globalDeathProperties);
    }
}
