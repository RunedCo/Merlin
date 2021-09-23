package co.runed.merlin.concept.spells;

import co.runed.merlin.concept.triggers.SpellTrigger;
import co.runed.merlin.concept.triggers.lifecycle.TickTrigger;
import org.jetbrains.annotations.NotNull;

public class TickTest extends Spell {
    public TickTest(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @SpellTrigger
    public CastResult onTick(TickTrigger trigger) {
        var context = trigger.getContext();

        context.getCaster().getEntity().sendMessage("Tick!");

        return CastResult.success();
    }
}
