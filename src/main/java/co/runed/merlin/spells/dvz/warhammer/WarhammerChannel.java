package co.runed.merlin.spells.dvz.warhammer;

import co.runed.merlin.spells.CastResult;
import co.runed.merlin.spells.Spell;
import co.runed.merlin.spells.SpellDefinition;
import co.runed.merlin.triggers.SpellTrigger;
import co.runed.merlin.triggers.Trigger;
import co.runed.merlin.triggers.core.TickTrigger;
import org.jetbrains.annotations.NotNull;

public class WarhammerChannel extends Spell {
    public WarhammerChannel(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @Override
    public CastResult preCast(Trigger trigger) {
        var result = super.preCast(trigger);

        if (!trigger.getContext().getCaster().isDrawingBow()) return CastResult.skip();

        return result;
    }

    @SpellTrigger
    public CastResult onTick(TickTrigger trigger) {

        return CastResult.success();
    }
}
