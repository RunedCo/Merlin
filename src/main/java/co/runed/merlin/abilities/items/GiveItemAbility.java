package co.runed.merlin.abilities.items;

import co.runed.bolster.Bolster;
import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.Definition;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.conditions.item.CanGiveItemCondition;
import co.runed.merlin.core.ItemManager;
import co.runed.merlin.core.MerlinRegistries;
import co.runed.merlin.items.Item;
import co.runed.merlin.target.Target;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class GiveItemAbility extends Ability
{
    Target<BolsterEntity> target;
    int amount = 1;
    String itemId;

    public GiveItemAbility(Target<BolsterEntity> target, Definition<Item> itemDef, int amount)
    {
        this(target, MerlinRegistries.ITEMS.getId(itemDef), amount);
    }

    public GiveItemAbility(Target<BolsterEntity> target, String itemId, int amount)
    {
        super();

        this.target = target;
        this.itemId = itemId;
        this.amount = amount;

        this.condition(new CanGiveItemCondition(target, itemId, amount));
    }

    @Override
    public void onActivate(Properties properties)
    {
        LivingEntity entity = this.target.get(properties).getBukkit();

        if (!(entity instanceof Player player)) return;

        if (itemId == null)
        {
            Bolster.getInstance().getLogger().severe("Error giving item with id " + this.itemId + " to " + player.getName() + " (not registered!)");

            return;
        }

        ItemManager.getInstance().giveItem(entity, player.getInventory(), this.itemId, this.amount);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
