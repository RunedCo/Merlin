package co.runed.merlin.abilities.entity.health;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.conditions.base.NotCondition;
import co.runed.merlin.conditions.entity.IsMaxHealthCondition;
import org.bukkit.configuration.ConfigurationSection;

public class HealEntityAbility extends Ability
{
    Target<BolsterEntity> target;
    double healAmount;

    public HealEntityAbility(Target<BolsterEntity> target)
    {
        this(target, -1);
    }

    public HealEntityAbility(Target<BolsterEntity> target, double healAmount)
    {
        super();

        this.target = target;
        this.healAmount = healAmount;

        this.condition(new NotCondition(new IsMaxHealthCondition(target)));
    }

    @Override
    public void onActivate(Properties properties)
    {
        BolsterEntity bolsterEntity = this.target.get(properties);

        double healAmount = this.healAmount;

        if (healAmount <= 0)
        {
            healAmount = bolsterEntity.getMaxHealth();
        }

        bolsterEntity.addHealth(healAmount);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
