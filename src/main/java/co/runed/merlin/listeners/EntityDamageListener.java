package co.runed.merlin.listeners;

import co.runed.merlin.core.AbilityManager;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

/**
 * Event that triggers casting an ability when an entity is damaged
 */
public class EntityDamageListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDamageEntity(EntityDamageByEntityEvent event)
    {
        Entity damager = event.getDamager();
        LivingEntity entity = null;

        if (damager instanceof LivingEntity)
        {
            entity = (LivingEntity) damager;
        }
        else if (damager instanceof Projectile)
        {
            ProjectileSource shooter = ((Projectile) damager).getShooter();

            if (!(shooter instanceof LivingEntity)) return;

            entity = (LivingEntity) shooter;
        }
        else if (damager instanceof TNTPrimed)
        {
            Entity source = ((TNTPrimed) damager).getSource();

            if (!(source instanceof LivingEntity)) return;

            entity = (LivingEntity) source;
        }

        if (entity == null) return;
        EntityEquipment inv = entity.getEquipment();
        ItemStack stack = inv.getItemInMainHand();

        Properties properties = new Properties();
        properties.set(AbilityProperties.TARGET, event.getEntity());
        properties.set(AbilityProperties.ITEM_STACK, stack);
        properties.set(AbilityProperties.EVENT, event);
        properties.set(AbilityProperties.DAMAGE_CAUSE, event.getCause());
        properties.set(AbilityProperties.DAMAGE, event.getDamage());
        properties.set(AbilityProperties.FINAL_DAMAGE, event.getFinalDamage());

        AbilityManager.getInstance().trigger(entity, AbilityTrigger.ON_DAMAGE_ENTITY, properties);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLeftClickEntity(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

        Player player = (Player) event.getDamager();
        EntityEquipment inv = player.getEquipment();
        ItemStack stack = inv.getItemInMainHand();

        Properties properties = new Properties();
        properties.set(AbilityProperties.TARGET, event.getEntity());
        properties.set(AbilityProperties.ITEM_STACK, stack);
        properties.set(AbilityProperties.EVENT, event);
        properties.set(AbilityProperties.DAMAGE_CAUSE, event.getCause());
        properties.set(AbilityProperties.DAMAGE, event.getDamage());
        properties.set(AbilityProperties.FINAL_DAMAGE, event.getFinalDamage());

        AbilityManager.getInstance().trigger(player, AbilityTrigger.LEFT_CLICK_ENTITY, properties);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntityFatalDamage(EntityDamageEvent event)
    {
        if (!(event.getEntity() instanceof LivingEntity)) return;

        LivingEntity entity = (LivingEntity) event.getEntity();
        EntityEquipment inv = entity.getEquipment();
        ItemStack stack = inv.getItemInMainHand();
        double damage = event.getFinalDamage();

        Properties properties = new Properties();
        properties.set(AbilityProperties.EVENT, event);
        properties.set(AbilityProperties.ITEM_STACK, stack);
        properties.set(AbilityProperties.DAMAGE_CAUSE, event.getCause());
        properties.set(AbilityProperties.DAMAGE, damage);
        properties.set(AbilityProperties.FINAL_DAMAGE, event.getFinalDamage());

        if (event instanceof EntityDamageByEntityEvent)
        {
            properties.set(AbilityProperties.DAMAGER, ((EntityDamageByEntityEvent) event).getDamager());
        }

        AbilityManager.getInstance().trigger(entity, AbilityTrigger.ON_TAKE_DAMAGE, properties);

        if (event.getFinalDamage() >= entity.getHealth())
        {
            AbilityManager.getInstance().trigger(entity, AbilityTrigger.ON_TAKE_FATAL_DAMAGE, properties);
        }
    }
}
