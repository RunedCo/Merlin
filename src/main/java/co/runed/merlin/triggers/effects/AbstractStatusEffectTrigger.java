package co.runed.merlin.triggers.effects;

import co.runed.bolster.events.entity.StatusEffectEvent;
import co.runed.bolster.status.StatusEffect;
import co.runed.merlin.triggers.EventTrigger;

public abstract class AbstractStatusEffectTrigger<T extends StatusEffectEvent> extends EventTrigger<T> {
    public AbstractStatusEffectTrigger(T baseEvent) {
        super(baseEvent);
    }

    public StatusEffect getStatusEffect() {
        return getBaseEvent().getStatusEffect();
    }
}
