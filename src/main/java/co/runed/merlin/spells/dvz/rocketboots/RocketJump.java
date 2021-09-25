package co.runed.merlin.spells.dvz.rocketboots;

import co.runed.bolster.fx.particles.ParticleData;
import co.runed.bolster.fx.particles.ParticleGroup;
import co.runed.bolster.fx.particles.ParticleSet;
import co.runed.bolster.fx.particles.ParticleType;
import co.runed.merlin.spells.CastResult;
import co.runed.merlin.spells.Spell;
import co.runed.merlin.spells.SpellDefinition;
import co.runed.merlin.triggers.SpellTrigger;
import co.runed.merlin.triggers.movement.SneakTrigger;
import co.runed.merlin.util.Leap;
import co.runed.merlin.util.task.RepeatingTask;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class RocketJump extends Spell {
    private static final ParticleType BOOTS_LAVA = new ParticleType("boots_lava", new ParticleGroup(new ParticleData(Particle.LAVA).setSpeed(0)));
    private static final Sound BOOTS_LAUNCH = Sound.sound(Key.key("nisovin.rocketboots"), Sound.Source.PLAYER, 1, 1);

    public RocketJump(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @SpellTrigger
    public CastResult onSneak(SneakTrigger trigger) {
        var context = trigger.getContext();
        if (trigger.isSneaking()) return CastResult.skip();

        var entity = context.getCasterEntity();

        var leap = new Leap(2, 20);
        var particles = ParticleSet.getActive(entity).get(BOOTS_LAVA);

        var task = new RepeatingTask(2L)
                .repeats(5)
                .run(() -> {
                    leap.apply(entity);
                    particles.spawnParticle(entity.getWorld(), entity.getLocation(), 5, 0, 0, 0, 0);
                });

        entity.playSound(BOOTS_LAUNCH);

        return CastResult.success();
    }
}
