package co.runed.merlin.abilities.event;

import co.runed.bolster.common.math.Operation;

public class CancelDamageAbility extends ModifyDamageAbility
{
    public CancelDamageAbility()
    {
        super(Operation.SET, 0);
    }
}
