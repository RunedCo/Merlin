package co.runed.merlin.spells.dvz.warhammer;

import co.runed.merlin.spells.CastResult;
import co.runed.merlin.spells.Spell;
import co.runed.merlin.spells.SpellDefinition;
import co.runed.merlin.triggers.SpellTrigger;
import co.runed.merlin.triggers.projectile.ShootTrigger;
import org.jetbrains.annotations.NotNull;

public class CancelShoot extends Spell {
    public CancelShoot(@NotNull SpellDefinition definition) {
        super(definition);
    }

    @SpellTrigger
    public CastResult onShoot(ShootTrigger trigger) {
        trigger.setConsumeItem(false);
        trigger.setCancelled(true);

        return CastResult.success();
    }
}
