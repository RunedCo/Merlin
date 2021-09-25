package co.runed.merlin.triggers.effects;

import co.runed.bolster.events.entity.EntityAddStatusEffectEvent;

public class AddStatusEffectTrigger extends AbstractStatusEffectTrigger<EntityAddStatusEffectEvent> {
    public AddStatusEffectTrigger(EntityAddStatusEffectEvent baseEvent) {
        super(baseEvent);
    }
}
