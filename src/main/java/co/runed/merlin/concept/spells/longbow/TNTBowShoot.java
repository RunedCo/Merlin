package co.runed.merlin.concept.spells.longbow;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.items.AmmoImpl;
import co.runed.merlin.concept.items.ItemDefinition;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.spells.Spell;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.spells.SpellManager;
import co.runed.merlin.concept.triggers.interact.InteractParams;
import co.runed.merlin.concept.triggers.interact.LeftClickTrigger;
import co.runed.merlin.concept.triggers.projectile.OnShootParams;
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
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class TNTBowShoot extends Spell implements OnShootTrigger, LeftClickTrigger {
    public TNTBowShoot(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @Override
    public CastResult onShoot(CastContext context, OnShootParams params) {
        var caster = context.getCasterEntity();
        params.setSpawnProjectile(false);
        params.setConsumeItem(false);

        var projectile = new Projectile(EntityType.ARROW)
                .owner(caster)
                .location(params.getProjectileLocation())
                .velocity(params.getVelocity())
                .removeOnFinish(false)
                .onStart((proj) -> {
                    var entity = proj.getEntity();
                    var armorStand = (ArmorStand) context.getWorld().spawnEntity(params.getProjectileLocation(), EntityType.ARMOR_STAND);
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
                    var entity = proj.getEntity();
                    var loc = entity.getLocation();
                    var armorStand = (ArmorStand) entity.getPassengers().get(0);
                    armorStand.setHeadPose(new EulerAngle(Math.toRadians(-loc.getPitch()), Math.toRadians(-loc.getYaw()), 0));
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
    public CastResult onLeftClick(CastContext context, InteractParams params) {
        if (getParent().getDefinition() instanceof ItemDefinition itemDef) {
            var ammo = (AmmoImpl) SpellManager.getInstance().getOrCreateProvider(context.getCaster().getEntity(), itemDef.getAmmoDefinition());
            ammo.addAmmo(1);
        }

        return CastResult.success();
    }
}
