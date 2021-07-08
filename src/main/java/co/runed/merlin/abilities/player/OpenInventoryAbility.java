package co.runed.merlin.abilities.player;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.conditions.entity.IsEntityTypeCondition;
import co.runed.merlin.target.Target;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.function.Supplier;

public class OpenInventoryAbility extends Ability
{
    Target<BolsterEntity> target;
    Supplier<Inventory> inventory;

    public OpenInventoryAbility(Target<BolsterEntity> target, Supplier<Inventory> inventory)
    {
        this.target = target;
        this.inventory = inventory;

        this.condition(new IsEntityTypeCondition(target, EntityType.PLAYER));
    }

    @Override
    public void onActivate(Properties properties)
    {
        Player player = (Player) this.target.get(properties).getBukkit();

        player.closeInventory();
        player.openInventory(inventory.get());
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
