package co.runed.merlin.abilities.effects;

import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.conditions.entity.IsEntityTypeCondition;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public class PlayGlobalSoundAbility extends Ability
{
    Target<Location> target;
    Sound sound;
    String soundId = null;
    SoundCategory soundCategory;
    float volume;
    float pitch;

    public PlayGlobalSoundAbility(Target<Location> target, Sound sound, SoundCategory soundCategory, float volume, float pitch)
    {
        this(target, soundCategory, volume, pitch);

        this.sound = sound;
    }

    public PlayGlobalSoundAbility(Target<Location> target, String sound, SoundCategory soundCategory, float volume, float pitch)
    {
        this(target, soundCategory, volume, pitch);

        this.soundId = sound;
    }

    private PlayGlobalSoundAbility(Target<Location> target, SoundCategory soundCategory, float volume, float pitch)
    {
        super();

        this.condition(new IsEntityTypeCondition(Target.CASTER, EntityType.PLAYER));

        this.target = target;
        this.soundCategory = soundCategory;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void onActivate(Properties properties)
    {
        Location location = this.target.get(properties);

        if (this.soundId == null)
        {
            location.getWorld().playSound(location, this.sound, this.soundCategory, this.volume, this.pitch);
        }
        else
        {
            location.getWorld().playSound(location, this.soundId, this.soundCategory, this.volume, this.pitch);
        }
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
