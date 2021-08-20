package co.runed.merlin.abilities.items;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.Definition;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.core.ItemManager;
import co.runed.merlin.core.MerlinRegistries;
import co.runed.merlin.items.Item;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class SetItemAbility extends Ability
{
    Target<BolsterEntity> target;
    int slot;
    String itemId;
    int outputCount = 1;

    public SetItemAbility(Target<BolsterEntity> target, int slot, Definition<Item> itemDefinition, int outputCount)
    {
        this(target, slot, MerlinRegistries.ITEMS.getId(itemDefinition), outputCount);
    }

    public SetItemAbility(Target<BolsterEntity> target, int slot, String itemId, int outputCount)
    {
        super();

        this.target = target;
        this.slot = slot;
        this.itemId = itemId;
        this.outputCount = outputCount;
    }

    @Override
    public void onActivate(Properties properties)
    {
        LivingEntity entity = this.target.get(properties).getBukkit();

        if (!(entity instanceof Player player)) return;

        ItemManager.getInstance().setItem(entity, player.getInventory(), this.slot, this.itemId, this.outputCount);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}

