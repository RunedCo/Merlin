package co.runed.merlin.triggers.effects;

import co.runed.bolster.events.entity.EntityRemoveStatusEffectEvent;
import co.runed.bolster.status.StatusEffect;

public class RemoveStatusEffectTrigger extends AbstractStatusEffectTrigger<EntityRemoveStatusEffectEvent> {
    public RemoveStatusEffectTrigger(EntityRemoveStatusEffectEvent baseEvent) {
        super(baseEvent);
    }

    public StatusEffect.RemovalCause getCause() {
        return getBaseEvent().getCause();
    }

    public Object getData() {
        return getBaseEvent().getData();
    }
}
