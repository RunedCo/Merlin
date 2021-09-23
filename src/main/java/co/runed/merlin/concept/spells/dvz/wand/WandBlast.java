package co.runed.merlin.concept.spells.dvz.wand;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.damage.DamageType;
import co.runed.bolster.fx.particles.ParticleData;
import co.runed.bolster.fx.particles.ParticleGroup;
import co.runed.bolster.fx.particles.ParticleType;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.spells.Spell;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.triggers.SpellTrigger;
import co.runed.merlin.concept.triggers.interact.LeftClickTrigger;
import co.runed.merlin.concept.triggers.interact.RightClickTrigger;
import co.runed.merlin.concept.util.Projectile;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class WandBlast extends Spell {
    private static final ParticleType WAND_BLAST = new ParticleType("wand_blast", new ParticleGroup()
            .add(new ParticleData(Particle.REDSTONE)
                    .setColor(Color.PURPLE))
            .add(new ParticleData(Particle.REDSTONE)
                    .setColor(Color.BLUE))
    );

    private static final ParticleType WAND_FLAME = new ParticleType("wand_flame", new ParticleGroup(new ParticleData(Particle.SOUL_FIRE_FLAME).setSpeed(0)));
//    private static final ParticleType WAND_BUBBLE = new ParticleType("wand_bubble", new ParticleInfo(Particle.WATER_BUBBLE));
//    private static final ParticleType WAND_MAGIC = new ParticleType("wand_magic", new ParticleInfo(Particle.HEART));

    private static final Sound WAND_FIRE = Sound.sound(Key.key("nisovin.wandfire"), Sound.Source.PLAYER, 1, 1);
    private static final Sound WAND_HIT = Sound.sound(Key.key("nisovin.wandhit"), Sound.Source.PLAYER, 1, 1);

    private static ParticleType[] types = new ParticleType[]{WAND_BLAST, WAND_FLAME};

    private int activeType = 0;

    public WandBlast(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @SpellTrigger
    public CastResult onRightClick(RightClickTrigger trigger) {
        var context = trigger.getContext();
        var caster = context.getCasterEntity();
        var world = caster.getWorld();

        var damage = new DamageInfo(10.0)
                .setNoDamageTicks(0)
                .withAttacker(caster)
                .withSource(this)
                .withType(DamageType.MAGIC);

        var projectile = new Projectile()
                .owner(caster)
                .location(caster.getEyeLocation())
                .particles(types[activeType])
                .speed(8)
                .onStart(proj -> world.playSound(WAND_FIRE))
                .onHit((proj, target) -> caster.playSound(WAND_HIT))
                .onHitBlock((proj, block) -> block.setType(Material.DIAMOND_BLOCK))
                .damage(damage)
                .run();

        return CastResult.success();
    }

    @SpellTrigger
    public CastResult onLeftClick(LeftClickTrigger trigger) {
        activeType = (activeType + 1) % types.length;

        var context = trigger.getContext();

        context.getCasterEntity().sendMessage("Active particle is now " + types[activeType].getId());

        return CastResult.success();
    }
}
