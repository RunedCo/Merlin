package co.runed.merlin.concept.triggers.lifecycle;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.triggers.Trigger;

public class TriggerParams {
    Class<? extends Trigger> triggerClass;
    CastContext triggerContext;

    public TriggerParams(Class<? extends Trigger> triggerClass, CastContext triggerContext) {
        this.triggerClass = triggerClass;
        this.triggerContext = triggerContext;
    }

    public CastContext getTriggerContext() {
        return triggerContext;
    }

    public Class<? extends Trigger> getTriggerClass() {
        return triggerClass;
    }
}
