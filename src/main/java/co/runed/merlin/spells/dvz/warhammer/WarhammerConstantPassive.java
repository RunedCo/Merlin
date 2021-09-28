package co.runed.merlin.spells.dvz.warhammer;

import co.runed.bolster.util.ItemBuilder;
import co.runed.merlin.items.ItemDefinition;
import co.runed.merlin.spells.CastResult;
import co.runed.merlin.spells.Spell;
import co.runed.merlin.spells.SpellDefinition;
import co.runed.merlin.triggers.SpellTrigger;
import co.runed.merlin.triggers.inventory.SelectItemTrigger;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WarhammerConstantPassive extends Spell {
    public WarhammerConstantPassive(@NotNull SpellDefinition definition) {
        super(definition);
    }

    // TODO set to previous instead of air
    @SpellTrigger
    public CastResult onSelect(SelectItemTrigger trigger) {
        var setArrow = getParent().getDefinition() instanceof ItemDefinition item && trigger.isItem(item);
        var item = setArrow ? new ItemBuilder(Material.ARROW).build() : new ItemStack(Material.AIR);

        trigger.getContext().getCaster().toBolster().getPlayerInventory().setItem(13, item);

        return CastResult.success();
    }
}
