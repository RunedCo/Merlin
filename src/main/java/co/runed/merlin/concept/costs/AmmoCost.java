package co.runed.merlin.concept.costs;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.items.ItemManager;
import co.runed.merlin.concept.items.ammo.AmmoDefinition;
import co.runed.merlin.concept.items.ammo.AmmoImpl;
import co.runed.merlin.concept.spells.CastResult;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class AmmoCost extends Cost {
    int amount = 1;
    AmmoDefinition ammoDef = null;

    public AmmoCost(int amount, AmmoDefinition ammoDef) {
        super();

        this.amount = amount;
        this.ammoDef = ammoDef;
    }

    public AmmoCost(int amount) {
        this(amount, null);
    }

    @Override
    public CastResult evaluate(CastContext context) {
        var entity = context.getCaster().getEntity();

        if (entity instanceof Player player && player.getGameMode() == GameMode.CREATIVE) return CastResult.success();

        if (ammoDef == null) {
            var parentDef = context.getItem().getDefinition();

            if (parentDef.hasAmmo()) {
                ammoDef = parentDef.getAmmoDefinition();
            }
        }
        if (ammoDef == null) return CastResult.fail();

        var ammoImpl = (AmmoImpl) ItemManager.getInstance().getOrCreate(entity, ammoDef);

        if (ammoImpl.getCurrentAmmo() < amount) return CastResult.fail();

        return CastResult.success();
    }

    @Override
    public void run(CastContext context) {
        var caster = context.getCaster();

        if (caster.getEntity() instanceof Player player && player.getGameMode() == GameMode.CREATIVE) return;

        var ammoImpl = (AmmoImpl) ItemManager.getInstance().getOrCreate(caster.getEntity(), ammoDef);

        ammoImpl.addAmmo(-amount);
    }
}
