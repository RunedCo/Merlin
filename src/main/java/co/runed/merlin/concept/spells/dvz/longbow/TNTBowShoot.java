package co.runed.merlin.concept.spells.dvz.longbow;

import co.runed.merlin.concept.items.ItemDefinition;
import co.runed.merlin.concept.items.ammo.AmmoImpl;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.spells.Spell;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.spells.SpellManager;
import co.runed.merlin.concept.triggers.SpellTrigger;
import co.runed.merlin.concept.triggers.interact.InteractParams;
import co.runed.merlin.concept.triggers.projectile.OnShootTrigger;
import co.runed.merlin.concept.util.Projectile;
import co.runed.merlin.concept.util.task.RepeatingTask;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class TNTBowShoot extends Spell {
    public TNTBowShoot(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @SpellTrigger
    public CastResult onShoot(OnShootTrigger trigger) {
        var caster = context.getCasterEntity();
        trigger.setSpawnProjectile(false);
        trigger.setConsumeItem(false);

        var projectile = new Projectile(EntityType.ARROW)
                .owner(caster)
                .location(trigger.getProjectileLocation())
                .velocity(trigger.getVelocity())
                .removeOnFinish(false)
                .onStart((proj) -> {
                    var entity = proj.getEntity();
                    var armorStand = (ArmorStand) context.getWorld().spawnEntity(trigger.getProjectileLocation(), EntityType.ARMOR_STAND);
                    armorStand.setBasePlate(false);
                    armorStand.setInvulnerable(true);
                    armorStand.setSilent(true);
                    armorStand.setCollidable(false);
                    armorStand.setAI(false);
                    armorStand.setSmall(true);
                    armorStand.setVisible(false);
                    armorStand.setItem(EquipmentSlot.HEAD, new ItemStack(Material.TNT));

                    entity.addPassenger(armorStand);
                })
                .onTick(proj -> {
//                    var entity = proj.getEntity();
//                    var loc = entity.getLocation();
//                    var armorStand = (ArmorStand) entity.getPassengers().get(0);
//                    var oldPose = armorStand.getHeadPose();
//
//                    armorStand.setHeadPose(new EulerAngle(Math.toRadians(loc.getPitch()), Math.toRadians(-loc.getYaw()), oldPose.getZ()));
                })
                .onHit((proj, target) -> target.damage(1))
                .onFinish(proj -> {
                    var armorStand = (ArmorStand) proj.getEntity().getPassengers().get(0);
                    var loc = armorStand.getLocation();

                    armorStand.getWorld().playSound(Sound.sound(Key.key("block.note_block.hat"), Sound.Source.PLAYER, 1, 1), loc.getX(), loc.getY(), loc.getZ());

                    var task = new RepeatingTask(20L)
                            .duration(Duration.ofSeconds(4))
                            .run(() -> {
                                switch (armorStand.getItem(EquipmentSlot.HEAD).getType()) {
                                    case TNT -> armorStand.setItem(EquipmentSlot.HEAD, new ItemStack(Material.YELLOW_WOOL));
                                    case YELLOW_WOOL -> armorStand.setItem(EquipmentSlot.HEAD, new ItemStack(Material.TNT));
                                }
                            })
                            .onFinish(() -> {
                                armorStand.getWorld().createExplosion(armorStand.getEyeLocation(), 0.1f);
                                proj.destroy(true);
                            });
                })
                .run();

        return CastResult.success();
    }

    @Override
    public CastResult onLeftClick(InteractParams params) {
        if (getParent().getDefinition() instanceof ItemDefinition itemDef) {
            var ammo = (AmmoImpl) SpellManager.getInstance().getOrCreateProvider(context.getCaster().getEntity(), itemDef.getAmmoDefinition());
            ammo.addAmmo(1);
        }

        return CastResult.success();
    }
}
