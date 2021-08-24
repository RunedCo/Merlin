package co.runed.merlin.concept.costs;

import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.items.ItemDefinition;
import co.runed.merlin.concept.spells.CastResult;

public class ItemCost extends Cost {
    int amount = 1;
    ItemDefinition item = null;

    public ItemCost(int amount, ItemDefinition item) {
        this(amount);

        this.item = item;
    }

    public ItemCost(int amount) {
        super();

        this.amount = amount;
    }

    @Override
    public CastResult evaluate(CastContext context) {
        if (item == null) item = context.getItem();
        if (item == null) return CastResult.fail();

        return CastResult.success();
    }

    @Override
    public void run(CastContext context) {

    }
}
