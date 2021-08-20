package co.runed.merlin.abilities.entity.status;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.status.StatusEffect;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import org.bukkit.configuration.ConfigurationSection;

public class ClearStatusEffectAbility extends Ability
{
    Target<BolsterEntity> target;
    Class<? extends StatusEffect> statusEffect;

    public ClearStatusEffectAbility(Target<BolsterEntity> target, Class<? extends StatusEffect> statusEffect)
    {
        super();

        this.target = target;
        this.statusEffect = statusEffect;
    }

    @Override
    public void onActivate(Properties properties)
    {
        this.target.get(properties).clearStatusEffect(statusEffect);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
