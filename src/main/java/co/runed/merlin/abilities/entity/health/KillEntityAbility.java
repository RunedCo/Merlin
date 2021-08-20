package co.runed.merlin.abilities.entity.health;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.target.Target;
import org.bukkit.configuration.ConfigurationSection;

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

        entity.getBukkit().damage(1000);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
