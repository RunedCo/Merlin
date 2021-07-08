package co.runed.merlin.conditions.item;

import co.runed.bolster.util.Definition;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import co.runed.merlin.core.ItemManager;
import co.runed.merlin.core.MerlinRegistries;
import co.runed.merlin.items.Item;
import org.bukkit.inventory.ItemStack;

public class ItemStackIsItemCondition extends Condition
{
    Definition<Item> item;

    public ItemStackIsItemCondition(Definition<Item> item)
    {
        this.item = item;
    }

    public ItemStackIsItemCondition(Item item)
    {
        this(item.getDefinition());
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        if (!properties.contains(AbilityProperties.ITEM_STACK)) return false;

        ItemStack stack = properties.get(AbilityProperties.ITEM_STACK);
        String id = MerlinRegistries.ITEMS.getId(item);

        if (id == null) return false;

        return id.equals(ItemManager.getInstance().getItemIdFromStack(stack));
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        return null;
    }
}
