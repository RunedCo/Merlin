package co.runed.merlin.listeners;

import co.runed.merlin.core.AbilityManager;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Event that triggers casting ability on shooting a bow
 */
public class EntityShootBowListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLivingEntityShootBow(EntityShootBowEvent event)
    {
        LivingEntity entity = event.getEntity();
        ItemStack stack = event.getBow();

        Properties properties = new Properties();
        properties.set(AbilityProperties.EVENT, event);
        properties.set(AbilityProperties.ITEM_STACK, stack);
        properties.set(AbilityProperties.FORCE, event.getForce());
        properties.set(AbilityProperties.VELOCITY, event.getProjectile().getVelocity());

        AbilityManager.getInstance().trigger(entity, AbilityTrigger.ON_SHOOT, properties);
    }
}
