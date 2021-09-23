package co.runed.merlin.spells.dvz.runeblade;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.damage.DamageType;
import co.runed.merlin.core.CastContext;
import co.runed.merlin.spells.CastResult;
import co.runed.merlin.spells.Spell;
import co.runed.merlin.spells.SpellDefinition;
import co.runed.merlin.triggers.SpellTrigger;
import co.runed.merlin.triggers.interact.InteractTrigger;
import co.runed.merlin.triggers.interact.RightClickTrigger;
import co.runed.merlin.util.AOE;
import co.runed.merlin.util.Leap;
import co.runed.merlin.util.task.RepeatingTask;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.HashSet;

public class Runedash extends Spell {
    private final double upwardsVelocity = 0.5;
    private final double forwardsVelocity = 5;
    private final double damage = 20;
    private final double damageRadius = 2;
    private final double cooldownReduction = 0;
    private final Collection<LivingEntity> hitEntities = new HashSet<>();

    public Runedash(SpellDefinition definition) {
        super(definition);
    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        super.loadConfig(config);
    }

    @SpellTrigger
    public CastResult onRightClick(RightClickTrigger trigger) {
        var context = trigger.getContext();

        if (!trigger.isRightClick()) return CastResult.fail();

        var entity = context.getCaster().getEntity();

        var task = new RepeatingTask(2L)
                .until(() -> false)
                .run(() -> doTick(trigger))
                .onFinish(hitEntities::clear);

        var leap = new Leap(upwardsVelocity, forwardsVelocity)
                .onLand(task::cancel)
                .apply(entity);

        entity.getWorld().playSound(Sound.sound(Key.key("dvz:runeblade_runedash"), Sound.Source.PLAYER, 1, 1));

        return CastResult.success();
    }

    public boolean isFrom(CastContext context) {
        return this.getParent().equals(context.getProvider());
    }

    private void doTick(InteractTrigger trigger) {
        var context = trigger.getContext();

        var targets = AOE.livingEntities(context.getCaster().getLocation(), 5)
                .ignoreIf((target) -> context.getCaster().equals(target))
                .ignoreIf(() -> hitEntities);

        var damage = new DamageInfo(100)
                .withType(DamageType.PRIMARY)
                .withAttacker(context.getCaster().getEntity())
                .withSource(this);

        for (var target : targets) {
            damage.apply(target);

            hitEntities.add(target);

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
