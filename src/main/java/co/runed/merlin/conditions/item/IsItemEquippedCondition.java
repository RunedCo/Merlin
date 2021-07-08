package co.runed.merlin.conditions.item;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.Definition;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import co.runed.merlin.core.ItemManager;
import co.runed.merlin.items.Item;
import co.runed.merlin.target.Target;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Collection;
import java.util.EnumSet;

public class IsItemEquippedCondition extends Condition
{
    Target<BolsterEntity> target;
    Collection<EquipmentSlot> slots;
    Definition<Item> item;

    public IsItemEquippedCondition(Target<BolsterEntity> target, Definition<Item> item)
    {
        this(target, EnumSet.allOf(EquipmentSlot.class), item);
    }

    public IsItemEquippedCondition(Target<BolsterEntity> target, EquipmentSlot slot, Definition<Item> item)
    {
        this(target, EnumSet.of(slot), item);
    }

    public IsItemEquippedCondition(Target<BolsterEntity> target, Collection<EquipmentSlot> slots, Definition<Item> item)
    {
        this.target = target;
        this.slots = slots;
        this.item = item;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        for (EquipmentSlot slot : this.slots)
        {
            if (this.item != null)
            {
                boolean isEquipped = ItemManager.getInstance().isItemEquipped(target.get(properties).getBukkit(), this.item, slot);

                if (isEquipped) return true;
            }
        }

        return false;
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
