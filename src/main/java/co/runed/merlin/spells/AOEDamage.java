package co.runed.merlin.spells;

import co.runed.bolster.damage.DamageInfo;
import co.runed.bolster.damage.DamageType;
import co.runed.merlin.triggers.SpellTrigger;
import co.runed.merlin.triggers.damage.DamageTrigger;
import co.runed.merlin.util.AOE;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class AOEDamage extends Spell {
    private double radius = 1;
    private double damage;

    public AOEDamage(@NotNull SpellDefinition definition, double radius) {
        this(definition);

        this.radius = radius;
    }

    public AOEDamage(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        super.loadConfig(config);

        radius = config.getDouble("radius", radius);
    }

    @SpellTrigger
    public CastResult onDamageEntity(DamageTrigger trigger) {
        if (trigger.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return CastResult.skip();

        var attacker = getOwner();

        var aoe = AOE.livingEntities(attacker.getLocation(), radius)
                .run(e -> {
                    var damage = new DamageInfo(trigger.getDamage())
                            .withType(DamageType.SECONDARY)
                            .withAttacker(attacker)
                            .apply(e);
                });

        return CastResult.success();
    }
}
