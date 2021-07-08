package co.runed.merlin.abilities.items;

import co.runed.bolster.util.Definition;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.conditions.entity.IsEntityTypeCondition;
import co.runed.merlin.core.ItemManager;
import co.runed.merlin.costs.ItemCost;
import co.runed.merlin.items.Item;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ConvertItemAbility extends Ability
{
    int inputCost;
    Definition<Item> outputItemDefinition;
    int outputCount;

    public ConvertItemAbility(int inputCost, Definition<Item> outputItemDefinition, int outputItemCount)
    {
        super();

        this.inputCost = inputCost;

        this.outputItemDefinition = outputItemDefinition;
        this.outputCount = outputItemCount;

        this.cost(new ItemCost(inputCost));
        this.condition(new IsEntityTypeCondition(Target.CASTER, EntityType.PLAYER));
    }

    @Override
    public void onActivate(Properties properties)
    {
        Player player = (Player) properties.get(AbilityProperties.CASTER).getBukkit();

        ItemManager.getInstance().giveItem(player, player.getInventory(), this.outputItemDefinition, this.outputCount);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
