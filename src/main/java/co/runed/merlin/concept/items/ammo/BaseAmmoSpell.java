package co.runed.merlin.concept.items.ammo;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.items.ItemDefinition;
import co.runed.merlin.concept.items.ItemManager;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.spells.Spell;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.triggers.inventory.ClickInventoryParams;
import co.runed.merlin.concept.triggers.inventory.ClickInventoryTrigger;
import co.runed.merlin.concept.triggers.inventory.SelectItemParams;
import co.runed.merlin.concept.triggers.inventory.SelectItemTrigger;
import org.jetbrains.annotations.NotNull;

public class BaseAmmoSpell extends Spell implements SelectItemTrigger, ClickInventoryTrigger {
    public BaseAmmoSpell(@NotNull SpellDefinition definition) {
        super(definition);
    }

    /* Update ammo display so the correct ammo is consumed and a bow is unable to be drawn when out of ammo */
    @Override
    public CastResult onSelectItem(CastContext context, SelectItemParams params) {
        var stack = params.getItemStack();
        var parent = getParent();
        var stackDef = ItemManager.getInstance().getDefFrom(stack);

        if (stackDef instanceof ItemDefinition itemDefinition && itemDefinition.hasAmmo()) {
            var ammoDef = itemDefinition.getAmmoDefinition();

            if (parent instanceof AmmoImpl ammo) {
                ammo.setDisplayEnabled(ammo.getDefinition().equals(ammoDef));
            }
        }

        return CastResult.success();
    }

    /* Stop player from being able to move ammo in inventory */
    @Override
    public CastResult onClickInventory(CastContext context, ClickInventoryParams params) {
        if (params.getItemStack() == null) return CastResult.fail();
        if (params.getClickedInventory() == null) return CastResult.fail();

        var stackDef = ItemManager.getInstance().getDefFrom(params.getItemStack());
        if (getParent().getDefinition().equals(stackDef)) context.setCancelled(true);

        return CastResult.success();
    }
}
