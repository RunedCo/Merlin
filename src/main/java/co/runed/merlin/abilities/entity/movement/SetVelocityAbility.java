package co.runed.merlin.abilities.entity.movement;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

public class SetVelocityAbility extends Ability
{
    Target<BolsterEntity> target;
    Vector velocity;

    public SetVelocityAbility(Target<BolsterEntity> target, Vector velocity)
    {
        super();

        this.target = target;
        this.velocity = velocity;
    }

    @Override
    public void onActivate(Properties properties)
    {
        this.target.get(properties).getBukkit().setVelocity(velocity);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
