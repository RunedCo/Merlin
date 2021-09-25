package co.runed.merlin.triggers.damage;

import co.runed.bolster.util.BukkitUtil;
import co.runed.merlin.core.SpellManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Event that triggers casting an ability when an entity is damaged
 */
public class EntityDamageListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDamageEntity(EntityDamageByEntityEvent event) {
        var entity = BukkitUtil.getDamagerFromEvent(event);

        if (entity == null) return;
        
        var inv = entity.getEquipment();
        var stack = inv.getItemInMainHand();

        SpellManager.getInstance().run(entity, new DamageTrigger(event, stack));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntityTakeDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        var inv = entity.getEquipment();
        var stack = inv.getItemInMainHand();

        SpellManager.getInstance().run(entity, new TakeDamageTrigger(event, stack));

        if (event.getFinalDamage() >= entity.getHealth()) {
            SpellManager.getInstance().run(entity, new FatalDamageTrigger(event, stack));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntityDeath(EntityDeathEvent event) {
        var entity = event.getEntity();
        var inv = entity.getEquipment();

        SpellManager.getInstance().run(entity, new DeathTrigger(event));

        SpellManager.getInstance().run(new AnyEntityDeathTrigger(event));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onKillEntity(EntityDeathEvent event) {
        var player = event.getEntity().getKiller();

        if (player == null) return;

        SpellManager.getInstance().run(player, new KillEntityTrigger(event));
    }
}
