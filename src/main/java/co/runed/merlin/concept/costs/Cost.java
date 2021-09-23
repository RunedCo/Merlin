package co.runed.merlin.concept.costs;

import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.triggers.Trigger;

public abstract class Cost {
    public abstract CastResult evaluate(Trigger trigger);

    public abstract void run(Trigger trigger);
}
