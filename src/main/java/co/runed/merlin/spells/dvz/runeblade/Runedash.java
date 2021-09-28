package co.runed.merlin.spells.dvz.runeblade;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.damage.DamageType;
import co.runed.bolster.util.config.ConfigEntry;
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
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.HashSet;

public class Runedash extends Spell {
    public static final Sound DASH_SOUND = Sound.sound(Key.key("dvz:runeblade_runedash"), Sound.Source.PLAYER, 1, 1);

    @ConfigEntry("upward-velocity")
    private double upwardVelocity = 0.5;
    @ConfigEntry("forward-velocity")
    private double forwardVelocity = 5;
    @ConfigEntry("damage")
    private double damage = 20;
    @ConfigEntry("damage-radius")
    private double damageRadius = 2;
    @ConfigEntry("cooldown-reduction")
    private double cooldownReduction = 0;

    private final Collection<LivingEntity> hitEntities = new HashSet<>();

    public Runedash(SpellDefinition definition) {
        super(definition);
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

        var leap = new Leap(upwardVelocity, forwardVelocity)
                .onLand(task::cancel)
                .apply(entity);

        entity.getWorld().playSound(DASH_SOUND);

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
    }
}
