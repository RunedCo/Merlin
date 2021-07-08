package co.runed.merlin.abilities.entity.health;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.DamageType;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.bolster.v1_16_R3.CraftUtil;
import co.runed.merlin.abilities.Ability;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

public class DamageEntityAbility extends Ability
{
    Target<BolsterEntity> target;
    double damage;
    DamageType cause;

    public DamageEntityAbility(Target<BolsterEntity> target, double damage, DamageType cause)
    {
        super();

        this.target = target;
        this.damage = damage;
        this.cause = cause;
    }

    @Override
    public void onActivate(Properties properties)
    {
        LivingEntity target = this.target.get(properties).getBukkit();

        CraftUtil.damageEntity(target, damage, this.getCaster(), this.cause.getDamageCause());
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
