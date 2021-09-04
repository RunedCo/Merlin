package co.runed.merlin.concept.spells;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.triggers.lifecycle.TickTrigger;
import org.jetbrains.annotations.NotNull;

public class TickTest extends Spell implements TickTrigger {
    public TickTest(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @Override
    public CastResult onTick(CastContext context) {
        context.getCaster().getEntity().sendMessage("Tick!");

        return CastResult.success();
    }
}
