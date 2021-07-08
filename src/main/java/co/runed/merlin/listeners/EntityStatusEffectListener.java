package co.runed.merlin.listeners;

import co.runed.bolster.events.entity.EntityAddStatusEffectEvent;
import co.runed.bolster.events.entity.EntityRemoveStatusEffectEvent;
import co.runed.merlin.core.AbilityManager;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EntityStatusEffectListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntityStatusEffectRemoved(EntityRemoveStatusEffectEvent event)
    {
        LivingEntity entity = event.getEntity();

        Properties properties = new Properties();
        properties.set(AbilityProperties.STATUS_EFFECT, event.getStatusEffect());
        properties.set(AbilityProperties.STATUS_EFFECT_REMOVAL_CAUSE, event.getCause());
        properties.set(AbilityProperties.STATUS_EFFECT_REMOVAL_DATA, event.getData());
        properties.set(AbilityProperties.EVENT, event);

        AbilityManager.getInstance().trigger(entity, AbilityTrigger.ON_REMOVE_STATUS_EFFECT, properties);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntityStatusEffectAdded(EntityAddStatusEffectEvent event)
    {
        LivingEntity entity = event.getEntity();

        Properties properties = new Properties();
        properties.set(AbilityProperties.STATUS_EFFECT, event.getStatusEffect());
        properties.set(AbilityProperties.EVENT, event);

        AbilityManager.getInstance().trigger(entity, AbilityTrigger.ON_ADD_STATUS_EFFECT, properties);
    }
}