package co.runed.merlin.costs;

import co.runed.merlin.spells.CastResult;
import co.runed.merlin.triggers.Trigger;

public abstract class Cost {
    public abstract CastResult evaluate(Trigger trigger);

    public abstract void run(Trigger trigger);
}
