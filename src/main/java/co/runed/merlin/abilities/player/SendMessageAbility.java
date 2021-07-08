package co.runed.merlin.abilities.player;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.conditions.entity.IsEntityTypeCondition;
import co.runed.merlin.target.Target;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public class SendMessageAbility extends Ability
{
    Target<BolsterEntity> target;
    Component message;

    public SendMessageAbility(Target<BolsterEntity> target, Component message)
    {
        super();

        this.target = target;
        this.message = message;

        this.condition(new IsEntityTypeCondition(target, EntityType.PLAYER));
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }

    @Override
    public void onActivate(Properties properties)
    {
        this.target.get(properties).sendMessage(this.message);
    }
}
