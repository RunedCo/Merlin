package co.runed.merlin.abilities.core;

import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.abilities.AbilityTrigger;
import co.runed.merlin.core.AbilityProvider;
import org.bukkit.configuration.ConfigurationSection;

public class ForwardTriggerAbility extends Ability
{
    AbilityTrigger trigger;
    boolean limitToParent = false;

    public ForwardTriggerAbility(AbilityTrigger trigger)
    {
        this(trigger, false);
    }

    public ForwardTriggerAbility(AbilityTrigger trigger, boolean limitToParent)
    {
        super();

        this.trigger = trigger;
        this.limitToParent = limitToParent;
    }

    @Override
    public void onActivate(Properties properties)
    {
        AbilityProvider parent = this.limitToParent ? this.getAbilityProvider() : null;

        this.trigger.trigger(this.getCaster(), parent, properties);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
