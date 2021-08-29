package co.runed.merlin.concept.triggers.inventory;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.triggers.Trigger;

public interface SelectItemTrigger extends Trigger {
    CastResult onSelectItem(CastContext context, SelectItemParams params);
}
