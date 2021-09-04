package co.runed.merlin.concept.triggers.lifecycle;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.triggers.Trigger;

public interface OnTriggerTrigger extends Trigger {
    CastResult onTrigger(CastContext context, TriggerParams params);
}
