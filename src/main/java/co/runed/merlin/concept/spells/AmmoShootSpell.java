package co.runed.merlin.concept.spells;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.triggers.projectile.OnShootParams;
import co.runed.merlin.concept.triggers.projectile.OnShootTrigger;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AmmoShootSpell extends Spell implements OnShootTrigger {
    private boolean shootArrow = false;

    public AmmoShootSpell(@NotNull SpellDefinition definition) {
        this(definition, false);
    }

    public AmmoShootSpell(@NotNull SpellDefinition definition, boolean shootArrow) {
        super(definition);

        this.shootArrow = shootArrow;
    }

    @Override
    public CastResult onShoot(CastContext context, OnShootParams params) {
        var caster = context.getCaster().getEntity();

        params.setConsumeItem(false);

        if (!shootArrow) params.setSpawnProjectile(false);

        if (caster instanceof Player player) {
            player.updateInventory();
        }

        return CastResult.success();
    }
}
