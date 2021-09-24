package co.runed.merlin.spells.dvz;

import co.runed.merlin.spells.CastResult;
import co.runed.merlin.spells.Spell;
import co.runed.merlin.spells.SpellDefinition;
import co.runed.merlin.triggers.SpellTrigger;
import co.runed.merlin.triggers.projectile.ShootTrigger;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AmmoShootSpell extends Spell {
    private boolean shootArrow = false;

    public AmmoShootSpell(@NotNull SpellDefinition definition) {
        this(definition, false);
    }

    public AmmoShootSpell(@NotNull SpellDefinition definition, boolean shootArrow) {
        super(definition);

        this.shootArrow = shootArrow;
    }

    @SpellTrigger
    public CastResult onShoot(ShootTrigger trigger) {
        var context = trigger.getContext();
        var caster = context.getCaster().getEntity();

        trigger.setConsumeItem(false);

        if (!shootArrow) trigger.setSpawnProjectile(false);

        if (caster instanceof Player player) {
            player.updateInventory();
        }

        return CastResult.success();
    }
}
