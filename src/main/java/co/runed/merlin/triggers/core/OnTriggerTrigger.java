package co.runed.merlin.triggers.core;

import co.runed.merlin.core.CastContext;
import co.runed.merlin.triggers.BaseTrigger;
import co.runed.merlin.triggers.Trigger;

import java.lang.reflect.Method;

public class OnTriggerTrigger extends BaseTrigger {
    Trigger trigger;
    Method method;

    public OnTriggerTrigger(Trigger trigger, Method method) {
        this.trigger = trigger;
        this.method = method;
    }

    public CastContext getTriggerContext() {
        return trigger.getContext();
    }

    public Trigger getTrigger() {
        return trigger;
    }
}
