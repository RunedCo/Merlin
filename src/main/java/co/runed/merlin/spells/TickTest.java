package co.runed.merlin.spells;

import co.runed.merlin.triggers.SpellTrigger;
import co.runed.merlin.triggers.lifecycle.TickTrigger;
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
