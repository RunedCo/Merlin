package co.runed.merlin.items;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Hat extends Item
{
    public static final ItemStack HAT_BASE = new ItemStack(Material.MINECART);

    @Override
    public void create()
    {
        this.addAbility(new EquipHatAbility().trigger(AbilityTrigger.RIGHT_CLICK));
    }

    public static class EquipHatAbility extends Ability
    {
        @Override
        public void onActivate(Properties properties)
        {
            BolsterEntity caster = properties.get(AbilityProperties.CASTER);
            ItemStack item = properties.get(AbilityProperties.ITEM_STACK);

            EntityEquipment equipment = caster.getEquipment();

            ItemStack invItem = equipment.getItem(EquipmentSlot.HEAD);

            if (invItem.getType() == Material.AIR)
            {
                equipment.setHelmet(item);
                item.setAmount(0);
            }
        }

        @Override
        public void loadConfig(ConfigurationSection config)
        {

        }
    }
}
