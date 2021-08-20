package co.runed.merlin.conditions.item;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.Definition;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import co.runed.merlin.core.ItemManager;
import co.runed.merlin.core.MerlinRegistries;
import co.runed.merlin.items.Item;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class HasItemCondition extends Condition
{
    Target<BolsterEntity> target;
    String id;
    int count;

    public HasItemCondition(Target<BolsterEntity> target, Definition<Item> item, int count)
    {
        this(target, MerlinRegistries.ITEMS.getId(item), count);
    }

    public HasItemCondition(Target<BolsterEntity> target, String id, int count)
    {
        this.target = target;
        this.id = id;
        this.count = count;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        LivingEntity entity = this.target.get(properties).getBukkit();

        if (entity instanceof Player && ((Player) entity).getGameMode() == GameMode.CREATIVE) return true;

        if (this.id == null)
        {
            this.id = properties.get(AbilityProperties.ITEM).getId();
        }

//        for (Inventory inv : BolsterEntity.from(entity).getInventories())
//        {
//            boolean contains = ItemManager.getInstance().inventoryContainsAtLeast(inv, this.id, this.count);
//
//            if (contains) return true;
//        }

        return ItemManager.getInstance().getAllInventoryItemCount(entity, id) > 0;
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
