package co.runed.merlin.concept.spells.dvz.rocketboots;

import co.runed.bolster.fx.particles.ParticleData;
import co.runed.bolster.fx.particles.ParticleGroup;
import co.runed.bolster.fx.particles.ParticleSet;
import co.runed.bolster.fx.particles.ParticleType;
import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.spells.Spell;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.triggers.movement.SneakParams;
import co.runed.merlin.concept.triggers.movement.SneakTrigger;
import co.runed.merlin.concept.util.Leap;
import co.runed.merlin.concept.util.task.RepeatingTask;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class RocketJump extends Spell implements SneakTrigger {
    private static final ParticleType BOOTS_LAVA = new ParticleType("boots_lava", new ParticleGroup(new ParticleData(Particle.LAVA).setSpeed(0)));

    public RocketJump(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @Override
    public CastResult onSneak(CastContext context, SneakParams params) {
        if (params.isSneaking()) return CastResult.skip();

        var entity = context.getCasterEntity();

        var leap = new Leap(2, 20);
        var particles = ParticleSet.getActive(entity).get(BOOTS_LAVA);

        var task = new RepeatingTask(2L)
                .repeats(5)
                .run(() -> {
                    leap.apply(entity);
                    particles.spawnParticle(entity.getWorld(), entity.getLocation(), 5, 0, 0, 0, 0);
                });

        return CastResult.success();
    }
}
