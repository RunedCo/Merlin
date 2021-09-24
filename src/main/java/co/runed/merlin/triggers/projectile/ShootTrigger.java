package co.runed.merlin.triggers.projectile;

import co.runed.merlin.triggers.EventTrigger;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ShootTrigger extends EventTrigger<Event> {
    private LivingEntity entity;
    private ItemStack itemStack;
    private Entity projectile;
    private float force;
    private Vector velocity;
    private boolean cancelled = false;

    public ShootTrigger(Event baseEvent, LivingEntity entity, ItemStack item, Entity projectile, float force, Vector velocity) {
        super(baseEvent);

        this.entity = entity;
        this.itemStack = item;
        this.projectile = projectile;
        this.force = force;
        this.velocity = velocity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Entity getProjectile() {
        return projectile;
    }

    public Location getProjectileLocation() {
        return getProjectile().getLocation();
    }

    public float getForce() {
        return force;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setSpawnProjectile(boolean spawnArrow) {
        if (getBaseEvent() instanceof Cancellable event) {
            event.setCancelled(!spawnArrow);
        }
    }

    public void setConsumeItem(boolean consumeItem) {
        if (getBaseEvent() instanceof EntityShootBowEvent event) {
            event.setConsumeItem(consumeItem);
        }
    }

    @Override
    public void setCancelled(boolean cancelled) {
        setSpawnProjectile(!cancelled);

        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
