package co.runed.merlin.concept.items.ammo;

import co.runed.merlin.concept.items.ItemDefinition;
import co.runed.merlin.concept.items.ItemManager;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.spells.Spell;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.triggers.SpellTrigger;
import co.runed.merlin.concept.triggers.inventory.ClickInventoryTrigger;
import co.runed.merlin.concept.triggers.inventory.SelectItemTrigger;
import org.jetbrains.annotations.NotNull;

public class BaseAmmoSpell extends Spell {
    public BaseAmmoSpell(@NotNull SpellDefinition definition) {
        super(definition);
    }

    /* Update ammo display so the correct ammo is consumed and a bow is unable to be drawn when out of ammo */
    @SpellTrigger
    public CastResult onSelectItem(SelectItemTrigger trigger) {
        var stack = trigger.getItemStack();
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
    @SpellTrigger
    public CastResult onClickInventory(ClickInventoryTrigger trigger) {
        if (trigger.getItemStack() == null) return CastResult.fail();
        if (trigger.getClickedInventory() == null) return CastResult.fail();

        var stackDef = ItemManager.getInstance().getDefFrom(trigger.getItemStack());
        if (getParent().getDefinition().equals(stackDef)) trigger.setCancelled(true);

        return CastResult.success();
    }
}
