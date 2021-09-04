package co.runed.merlin.concept.spells.wand;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.damage.DamageType;
import co.runed.bolster.fx.particles.ParticleInfo;
import co.runed.bolster.fx.particles.ParticleType;
import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.spells.Spell;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.triggers.interact.InteractParams;
import co.runed.merlin.concept.triggers.interact.RightClickTrigger;
import co.runed.merlin.concept.util.Projectile;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class WandBlast extends Spell implements RightClickTrigger {
    private static final ParticleType WAND_BLAST = new ParticleType("wand_blast", new ParticleInfo(Particle.REDSTONE, new Particle.DustOptions(Color.PURPLE, 1)));
    private static final Sound WAND_FIRE = Sound.sound(Key.key("nisovin.wandfire"), Sound.Source.PLAYER, 1, 1);
    private static final Sound WAND_HIT = Sound.sound(Key.key("nisovin.wandhit"), Sound.Source.PLAYER, 1, 1);

    public WandBlast(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @Override
    public CastResult onRightClick(CastContext context, InteractParams params) {
        var caster = context.getCasterEntity();

        var damage = new DamageInfo(10.0)
                .setNoDamageTicks(0)
                .withAttacker(caster)
                .withSource(this)
                .withType(DamageType.MAGIC);

        var projectile = new Projectile()
                .owner(caster)
                .location(caster.getEyeLocation())
                .particles(WAND_BLAST)
                .speed(8)
                .onStart(proj -> caster.playSound(WAND_FIRE))
                .onHit((proj, target) -> caster.playSound(WAND_HIT))
                .onHitBlock((proj, block) -> block.setType(Material.DIAMOND_BLOCK))
                .damage(damage)
                .run();

        return CastResult.success();
    }
}
