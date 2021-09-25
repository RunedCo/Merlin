package co.runed.merlin.triggers.lifecycle;

import co.runed.merlin.core.SpellManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EntitySpawnListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntitySpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity le)) return;

        this.run(event, le);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerJoin(PlayerJoinEvent event) {
        this.run(event, event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        this.run(event, event.getPlayer());
    }

    private void run(Event event, LivingEntity entity) {
        SpellManager.getInstance().run(entity, new SpawnTrigger(event));
    }
}
