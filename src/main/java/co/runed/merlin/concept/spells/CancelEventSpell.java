package co.runed.merlin.concept.spells;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.triggers.Trigger;
import co.runed.merlin.concept.triggers.lifecycle.OnTriggerTrigger;
import co.runed.merlin.concept.triggers.lifecycle.TriggerParams;

public class CancelEventSpell extends Spell implements OnTriggerTrigger {
    private Class<? extends Trigger> triggerClass;

    public CancelEventSpell(SpellDefinition definition, Class<? extends Trigger> triggerClass) {
        super(definition);

        this.triggerClass = triggerClass;
    }

    @Override
    public CastResult onTrigger(CastContext context, TriggerParams params) {
        if (params.getTriggerClass() == triggerClass) params.getTriggerContext().setCancelled(true);

        return CastResult.success();
    }

    public Class<? extends Trigger> getTriggerClass() {
        return triggerClass;
    }
}
