package co.runed.merlin.abilities.entity.status;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;

import java.util.function.Supplier;

public class AddPotionEffectAbility extends Ability
{
    Target<BolsterEntity> target;
    Supplier<PotionEffect> effect;

    public AddPotionEffectAbility(Target<BolsterEntity> target, Supplier<PotionEffect> effect)
    {
        super();

        this.target = target;
        this.effect = effect;
    }

    @Override
    public void onActivate(Properties properties)
    {
        this.target.get(properties).getBukkit().addPotionEffect(this.effect.get());
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
