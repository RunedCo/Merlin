package co.runed.merlin.concept.costs;

import co.runed.merlin.concept.items.ItemDefinition;
import co.runed.merlin.concept.items.ItemManager;
import co.runed.merlin.concept.spells.CastResult;
import co.runed.merlin.concept.triggers.Trigger;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class ItemCost extends Cost {
    private int amount = 1;
    private ItemDefinition item = null;

    public ItemCost(int amount, ItemDefinition item) {
        super();

        this.amount = amount;
        this.item = item;
    }

    public ItemCost(int amount) {
        this(amount, null);
    }

    @Override
    public CastResult evaluate(Trigger trigger) {
        var context = trigger.getContext();
        var entity = context.getCaster().getEntity();

        if (item == null) item = context.getItem().getDefinition();
        if (item == null) return CastResult.fail();

        if (!ItemManager.getInstance().anyInventoryContainsAtLeast(entity, item, amount)) return CastResult.fail();

        return CastResult.success();
    }

    @Override
    public void run(Trigger trigger) {
        var context = trigger.getContext();
        var caster = context.getCaster();

        if (caster.getEntity() instanceof Player player && player.getGameMode() == GameMode.CREATIVE) return;

        if (item == null) return;

        for (var inv : caster.toBolster().getInventories()) {
            var success = ItemManager.getInstance().removeItem(inv, item, amount);

            if (success) return;
        }
    }
}
