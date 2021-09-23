package co.runed.merlin.core;

import co.runed.dayroom.math.Operation;
import co.runed.bolster.game.traits.Trait;

public class MerlinTraits {
    /* Merlin Traits */
    public static final Trait<Float> MANA_PER_SECOND = new Trait<>("mana_per_second", 10f, Operation.ADD);
    public static final Trait<Double> SPELL_DAMAGE = new Trait<>("spell_damage", 0.0d, Operation.ADD);

    /* Vanilla Attribute Traits */
    public static final Trait<Double> ATTACK_DAMAGE = new Trait<>("attack_damage", 0.0d, Operation.ADD);
    public static final Trait<Double> ATTACK_SPEED = new Trait<>("attack_speed", 0.0d, Operation.ADD);
    public static final Trait<Double> KNOCKBACK_RESISTANCE = new Trait<>("knockback_resistance", 0.0D, Operation.ADD);
    public static final Trait<Double> KNOCKBACK = new Trait<>("knockback", 0.0d, Operation.ADD);
}
