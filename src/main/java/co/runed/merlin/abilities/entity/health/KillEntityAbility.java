package co.runed.merlin.abilities.entity.health;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;

public class KillEntityAbility extends Ability
{
    Target<BolsterEntity> target;

    public KillEntityAbility(Target<BolsterEntity> target)
    {
        super();

        this.target = target;
    }

    @Override
    public void onActivate(Properties properties)
    {
        BolsterEntity entity = this.target.get(properties);

        if (entity.getBukkit() instanceof ArmorStand)
        {
            entity.getBukkit().setHealth(0);
        }
        else
        {
            entity.getBukkit().damage(1000);
        }
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
