package co.runed.merlin.concept.triggers.interact;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.triggers.Trigger;

public interface InteractTrigger extends Trigger {
    CastResult onClick(CastContext context, InteractParams params);
}
