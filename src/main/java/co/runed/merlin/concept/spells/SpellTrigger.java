package co.runed.merlin.concept.spells;

import co.runed.merlin.concept.triggers.EventParams;
import co.runed.merlin.concept.triggers.Trigger;
import co.runed.merlin.concept.triggers.interact.InteractParams;
import co.runed.merlin.concept.triggers.interact.InteractTrigger;

public class SpellTrigger {
    public static SpellTrigger CLICK = new SpellTrigger(InteractTrigger.class, InteractParams.class);

    public SpellTrigger(Class<? extends Trigger> triggerClass, Class<? extends EventParams> paramsClass) {

    }
}
