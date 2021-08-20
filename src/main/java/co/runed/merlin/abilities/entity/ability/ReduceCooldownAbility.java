package co.runed.merlin.abilities.entity.ability;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.cooldown.ICooldownSource;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.core.AbilityProvider;
import co.runed.merlin.target.Target;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

public class ReduceCooldownAbility extends Ability
{
    Target<BolsterEntity> target;
    AbilityProvider provider;
    Collection<ICooldownSource> cooldownSources;
    double reduction = 0;

    public ReduceCooldownAbility(Target<BolsterEntity> target, AbilityProvider provider, double reduction)
    {
        this(target, reduction);

        this.provider = provider;
    }

    private ReduceCooldownAbility(Target<BolsterEntity> target, double reduction)
    {
        this.target = target;
        this.reduction = reduction;
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }

    @Override
    public void onActivate(Properties properties)
    {
        LivingEntity entity = this.target.get(properties).getBukkit();

        if (this.provider != null)
        {
            for (AbilityProvider.AbilityData data : provider.getAbilities())
            {
                Ability ability = data.ability;

                if (!ability.isOnCooldown()) continue;

                ability.setRemainingCooldown(ability.getRemainingCooldown() - reduction);
            }
        }
    }
}
