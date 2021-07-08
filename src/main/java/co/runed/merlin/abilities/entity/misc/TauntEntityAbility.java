package co.runed.merlin.abilities.entity.misc;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

public class TauntEntityAbility extends Ability
{
    Target<BolsterEntity> taunting;
    Target<BolsterEntity> taunted;

    public TauntEntityAbility(Target<BolsterEntity> taunted)
    {
        this(Target.CASTER, taunted);
    }

    public TauntEntityAbility(Target<BolsterEntity> taunting, Target<BolsterEntity> taunted)
    {
        super();

        this.taunting = taunting;
        this.taunted = taunted;
    }

    @Override
    public void onActivate(Properties properties)
    {
        LivingEntity entity = taunted.get(properties).getBukkit();

        if (entity instanceof Creature creature)
        {
            creature.setTarget(taunting.get(properties).getBukkit());
        }
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
