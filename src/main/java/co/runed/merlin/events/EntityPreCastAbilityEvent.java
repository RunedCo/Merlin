package co.runed.merlin.events;

import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.AbilityTrigger;
import co.runed.merlin.core.AbilityProvider;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;

public final class EntityPreCastAbilityEvent extends AbilityTriggerEvent
{
    private static final HandlerList handlers = new HandlerList();

    public EntityPreCastAbilityEvent(LivingEntity entity, AbilityProvider abilityProvider, AbilityTrigger trigger, Properties properties)
    {
        super(entity, abilityProvider, trigger, properties);
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
