package co.runed.merlin.items;

import co.runed.merlin.core.CastContext;
import org.bukkit.inventory.EquipmentSlot;

import java.util.function.Function;

public enum ItemRequirement {
    // Always trigger regardless of equipped item
    ALWAYS((ctx) -> true),

    // Item is in Main Hand
    MAIN_HAND((ctx) -> ctx.getCaster().isEquipped(ctx.getItem().getDefinition(), EquipmentSlot.HAND)),

    // Item is in Off Hand
    OFF_HAND((ctx) -> ctx.getCaster().isEquipped(ctx.getItem().getDefinition(), EquipmentSlot.OFF_HAND)),

    // Item is in Off Hand or Main Hand
    ANY_HAND((ctx) -> ctx.getCaster().isHolding(ctx.getItem().getDefinition())),

    // Item is in any armor equipment slot
    ARMOR((ctx) -> ctx.getCaster().isArmor(ctx.getItem().getDefinition())),

    // Item is in any EquipmentSlot
    EQUIPPED((ctx) -> ctx.getCaster().isEquipped(ctx.getItem().getDefinition())),

    // Item is anywhere in inventory
    INVENTORY((ctx) -> ItemManager.getInstance().anyInventoryContainsAtLeast(ctx.getCasterEntity(), ctx.getItem().getDefinition(), 1));

    private final Function<CastContext, Boolean> evaluate;

    ItemRequirement(Function<CastContext, Boolean> evaluate) {
        this.evaluate = evaluate;
    }

    public boolean evaluate(CastContext context) {
        return evaluate.apply(context);
    }
}
