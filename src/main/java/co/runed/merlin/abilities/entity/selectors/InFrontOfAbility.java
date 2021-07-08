package co.runed.merlin.abilities.entity.selectors;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.BukkitUtil;
import co.runed.merlin.target.Target;

public class InFrontOfAbility extends MultiTargetAbility
{
    Target<BolsterEntity> target;
    float distance;

    public InFrontOfAbility(Target<BolsterEntity> target, float distance)
    {
        super(null);

        this.target = target;
        this.distance = distance;

        this.entityFunction((properties) -> BukkitUtil.getEntitiesInFrontOf(this.target.get(properties).getBukkit(), this.distance));
    }
}
