package co.runed.merlin.abilities.core;

import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.Ability;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import java.util.function.BiConsumer;

public class FunctionAbility extends Ability
{
    BiConsumer<LivingEntity, Properties> lambda;

    public FunctionAbility(BiConsumer<LivingEntity, Properties> lambda)
    {
        super();

        this.lambda = lambda;
    }

    @Override
    public void onActivate(Properties properties)
    {
        this.lambda.accept(this.getCaster(), properties);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
