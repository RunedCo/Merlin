package co.runed.merlin.concept.triggers.projectile;

import co.runed.merlin.concept.spells.SpellManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Event that triggers casting ability on shooting a bow
 */
public class EntityProjectileListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLivingEntityShootBow(EntityShootBowEvent event) {
        var entity = event.getEntity();
        var stack = event.getBow();
        var projectile = event.getProjectile();

        onShoot(event, entity, stack, projectile, event.getForce(), projectile.getVelocity());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerThrowEgg(PlayerEggThrowEvent event) {
        var entity = event.getPlayer();
        var stack = event.getEgg().getItem();
        var projectile = event.getEgg();

        onShoot(event, entity, stack, projectile, 1.0f, projectile.getVelocity());
    }

    private void onShoot(Event event, LivingEntity entity, ItemStack item, Entity projectile, float force, Vector velocity) {
        var trigger = new OnShootTrigger(event, entity, item, projectile, force, velocity);

        SpellManager.getInstance().run(entity, trigger);
    }
}
