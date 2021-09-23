package co.runed.merlin.concept.spells;

import co.runed.merlin.concept.triggers.SpellTrigger;
import co.runed.merlin.concept.triggers.projectile.OnShootTrigger;
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
    public CastResult onShoot(OnShootTrigger trigger) {
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
