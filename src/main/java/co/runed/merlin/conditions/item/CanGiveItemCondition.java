package co.runed.merlin.conditions.item;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.Definition;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import co.runed.merlin.core.ItemManager;
import co.runed.merlin.core.MerlinRegistries;
import co.runed.merlin.items.Item;
import co.runed.merlin.target.Target;
import org.bukkit.entity.LivingEntity;

public class CanGiveItemCondition extends Condition
{
    Target<BolsterEntity> target;
    String id;
    int count;

    public CanGiveItemCondition(Target<BolsterEntity> target, Definition<Item> item, int count)
    {
        this(target, MerlinRegistries.ITEMS.getId(item), count);
    }

    public CanGiveItemCondition(Target<BolsterEntity> target, String id, int count)
    {
        super();

        this.target = target;
        this.id = id;
        this.count = count;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        LivingEntity entity = this.target.get(properties).getBukkit();

        if (this.id == null && properties.contains(AbilityProperties.ITEM))
        {
            this.id = properties.get(AbilityProperties.ITEM).getId();
        }

        boolean canGive = ItemManager.getInstance().canGiveItem(entity, this.id, count);

        return canGive;
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
