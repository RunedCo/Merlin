package co.runed.merlin.triggers.effects;

import co.runed.bolster.events.entity.EntityAddStatusEffectEvent;
import co.runed.bolster.events.entity.EntityRemoveStatusEffectEvent;
import co.runed.merlin.core.SpellManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EntityStatusEffectListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntityStatusEffectRemoved(EntityRemoveStatusEffectEvent event) {
        var entity = event.getEntity();

        SpellManager.getInstance().run(entity, new RemoveStatusEffectTrigger(event));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntityStatusEffectAdded(EntityAddStatusEffectEvent event) {
        var entity = event.getEntity();

        SpellManager.getInstance().run(entity, new AddStatusEffectTrigger(event));
    }
}