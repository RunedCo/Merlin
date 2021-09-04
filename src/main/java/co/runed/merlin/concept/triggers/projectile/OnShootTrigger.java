package co.runed.merlin.concept.triggers.projectile;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.triggers.Trigger;

public interface OnShootTrigger extends Trigger {
    CastResult onShoot(CastContext context, OnShootParams params);
}
