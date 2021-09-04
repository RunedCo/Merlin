package co.runed.merlin.concept.triggers.interact;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.triggers.Trigger;

public interface LeftClickTrigger extends Trigger {
    CastResult onLeftClick(CastContext context, InteractParams params);
}
