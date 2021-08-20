package co.runed.merlin.abilities.entity.status;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.status.StatusEffect;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Supplier;

public class AddStatusEffectAbility extends Ability
{
    Target<BolsterEntity> target;
    Supplier<StatusEffect> statusEffect;

    public AddStatusEffectAbility(Target<BolsterEntity> target, Supplier<StatusEffect> statusEffect)
    {
        super();

        this.target = target;
        this.statusEffect = statusEffect;
    }

    @Override
    public void onActivate(Properties properties)
    {
        this.target.get(properties).addStatusEffect(this.statusEffect.get());
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
