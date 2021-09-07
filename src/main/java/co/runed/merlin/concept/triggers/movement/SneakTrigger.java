package co.runed.merlin.concept.triggers.movement;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.triggers.Trigger;

public interface SneakTrigger extends Trigger {
    CastResult onSneak(CastContext context, SneakParams params);
}
