package co.runed.merlin.concept.triggers.inventory;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.triggers.Trigger;

public interface DeselectItemTrigger extends Trigger {
    CastResult onDeselectItem(CastContext context, SelectItemParams params);
}
