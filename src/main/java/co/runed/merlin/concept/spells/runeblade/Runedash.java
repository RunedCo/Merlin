package co.runed.merlin.concept.spells.runeblade;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.damage.DamageType;
import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.spells.Spell;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.triggers.interact.InteractParams;
import co.runed.merlin.concept.triggers.interact.RightClickTrigger;
import co.runed.merlin.concept.util.AOE;
import co.runed.merlin.concept.util.Leap;
import co.runed.merlin.concept.util.task.RepeatingTask;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.configuration.ConfigurationSection;

public class Runedash extends Spell implements RightClickTrigger {
    private final double upwardsVelocity = 0.5;
    private final double forwardsVelocity = 5;
    private final double damage = 20;
    private final double damageRadius = 2;
    private final double cooldownReduction = 0;

    public Runedash(SpellDefinition definition) {
        super(definition);
    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        super.loadConfig(config);
    }

    @Override
    public CastResult onRightClick(CastContext context, InteractParams params) {
        if (!params.isRightClick()) return CastResult.fail();
        var entity = context.getCaster().getEntity();

        var task = new RepeatingTask(2L)
                .until(() -> false)
                .run(() -> doTick(context, params));

        var leap = new Leap(upwardsVelocity, forwardsVelocity)
                .onLand(task::cancel)
                .apply(entity);

        entity.getWorld().playSound(Sound.sound(Key.key("dvz:runeblade_runedash"), Sound.Source.PLAYER, 1, 1));

        return CastResult.success();
    }

    public boolean isFrom(CastContext context) {
        return this.getParent().equals(context.getProvider());
    }

    private void doTick(CastContext context, InteractParams params) {
        var targets = AOE.livingEntities(context.getCaster().getLocation(), 5)
                .ignoreIf((target) -> context.getCaster().equals(target));

        var damage = new DamageInfo(100)
                .withType(DamageType.PRIMARY)
                .withAttacker(context.getCaster().getEntity())
                .withSource(this);

        for (var target : targets) {
            damage.apply(target);

            context.getCaster().getEntity().sendMessage("Hit " + target.getName());
        }

//        var aoe = new EntityAOE(context.getCaster().getLocation(), 5)
//                .run(target -> {
//                    if (target instanceof LivingEntity livingEntity)
//                    {
//                        livingEntity.damage(1000);
//                    }
//                });
    }

//    @EventHandler(priority = EventPriority.HIGH)
//    private void onPlayerMove(PlayerMoveEvent event)
//    {
//        if (this.delayedIgnoreTriggered) return;
//        if (this.getCaster() == null) return;
//        if (!event.getPlayer().getUniqueId().equals(this.getCaster().getUniqueId())) return;
//        if (!event.getPlayer().isOnGround()) return;
//
//        this.delayedIgnoreTriggered = true;
//
//        Bukkit.getScheduler().scheduleSyncDelayedTask(Bolster.getInstance(), () -> {
//            this.ignoreNext = false;
//            this.delayedIgnoreTriggered = false;
//            this.touchedGround = true;
//        }, 10L);
//    }
//
//    @EventHandler(priority = EventPriority.HIGH)
//    private void onNextFallDamage(EntityDamageEvent event)
//    {
//        if (!ignoreNext) return;
//        if (this.getCaster() == null) return;
//        if (!event.getEntity().getUniqueId().equals(this.getCaster().getUniqueId())) return;
//        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
//
//        this.ignoreNext = false;
//
//        event.setCancelled(true);
//    }
}
