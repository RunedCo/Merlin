package co.runed.merlin.triggers.lifecycle;

import co.runed.merlin.core.CastContext;
import co.runed.merlin.triggers.BaseTrigger;
import co.runed.merlin.triggers.Trigger;

import java.lang.reflect.Method;

public class OnTrigger extends BaseTrigger {
    Trigger trigger;
    Method method;

    public OnTrigger(Trigger trigger, Method method) {
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
