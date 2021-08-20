package co.runed.merlin.abilities.items;

import co.runed.bolster.util.Definition;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.effects.PlaySoundAbility;
import co.runed.merlin.conditions.entity.IsEntityTypeCondition;
import co.runed.merlin.conditions.world.BlockIsMaterialCondition;
import co.runed.merlin.core.ItemManager;
import co.runed.merlin.items.Item;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class CollectItemAbility extends Ability
{
    Collection<Material> materials = new ArrayList<>();
    Definition<Item> itemDefinition;
    int outputCount = 1;

    public CollectItemAbility(Collection<Material> materials, Definition<Item> itemDefinition, int outputCount)
    {
        super();

        this.itemDefinition = itemDefinition;
        this.materials = materials;
        this.outputCount = outputCount;

        // SET DEFAULT COOLDOWN
        this.cooldown(10);

        this.condition(new IsEntityTypeCondition(Target.CASTER, EntityType.PLAYER));
        this.condition(new BlockIsMaterialCondition(this.materials));

        this.ability(new PlaySoundAbility(Target.CASTER, Target.CASTER_LOCATION, "activatebow", SoundCategory.BLOCKS, 0.5f, 1.5f));
    }

    @Override
    public void onActivate(Properties properties)
    {
        Player player = (Player) properties.get(AbilityProperties.CASTER).getBukkit();

        ItemManager.getInstance().giveItem(player, player.getInventory(), itemDefinition, outputCount);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
