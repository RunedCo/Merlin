package co.runed.merlin.abilities.player;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.conditions.entity.IsEntityTypeCondition;
import co.runed.merlin.target.Target;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.time.Duration;

public class SendTitleAbility extends Ability
{
    Target<BolsterEntity> target;
    Component titleText = Component.empty();
    Component subtitleText = Component.empty();
    Duration fadeIn = Duration.ZERO;
    Duration stay = Duration.ZERO;
    Duration fadeOut = Duration.ZERO;

    public SendTitleAbility(Target<BolsterEntity> target)
    {
        super();

        this.target = target;

        this.condition(new IsEntityTypeCondition(target, EntityType.PLAYER));
    }

    public SendTitleAbility title(Component titleText)
    {
        this.titleText = titleText;

        return this;
    }

    public SendTitleAbility subtitle(Component subtitleText)
    {
        this.subtitleText = subtitleText;

        return this;
    }

    public SendTitleAbility fadeIn(Duration fadeIn)
    {
        this.fadeIn = fadeIn;

        return this;
    }

    public SendTitleAbility stay(Duration stay)
    {
        this.stay = stay;

        return this;
    }

    public SendTitleAbility fadeOut(Duration fadeOut)
    {
        this.fadeOut = fadeOut;

        return this;
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }

    @Override
    public void onActivate(Properties properties)
    {
        this.target.get(properties).sendTitle(titleText, subtitleText, fadeIn, stay, fadeOut);
    }
}
