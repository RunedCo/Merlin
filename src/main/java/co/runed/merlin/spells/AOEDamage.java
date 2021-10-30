package co.runed.merlin.spells;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.damage.DamageType;
import co.runed.bolster.util.config.ConfigEntry;
import co.runed.merlin.triggers.SpellTrigger;
import co.runed.merlin.triggers.damage.DamageTrigger;
import co.runed.merlin.util.AOE;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AOEDamage extends Spell {
    @ConfigEntry(key = "radius")
    private double radius = 1;
    @ConfigEntry(key = "damage")
    private double damage;

    public AOEDamage(@NotNull SpellDefinition definition, double radius) {
        this(definition);

        this.radius = radius;
    }

    public AOEDamage(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @Override
    public Component getDeathMessage(LivingEntity killer, Player victim, DamageInfo damageInfo) {
        return Component.text(victim.getName() + " was killed by AOE damage lol xd by " + killer.getName());
    }

    @SpellTrigger
    public CastResult onDamageEntity(DamageTrigger trigger) {
        if (trigger.getType() != DamageType.PRIMARY) return CastResult.skip();

        var attacker = getOwner();

        var aoe = AOE.livingEntities(attacker.getLocation(), radius)
                .ignoreIf(attacker)
                .ignoreIf(trigger.getTarget())
                .run(e -> {
                    var damage = new DamageInfo(trigger.getDamage())
                            .withType(DamageType.SWEEP_AOE)
                            .withAttacker(attacker)
                            .withSource(this)
                            .apply(e);
                });

        return CastResult.success();
    }
}
