package co.runed.merlin.concept.costs;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.spells.CastResult;

public abstract class Cost {
    public abstract CastResult evaluate(CastContext context);

    public abstract void run(CastContext context);
}
