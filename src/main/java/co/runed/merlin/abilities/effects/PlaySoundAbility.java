package co.runed.merlin.abilities.effects;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.conditions.entity.IsEntityTypeCondition;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlaySoundAbility extends Ability
{
    Target<BolsterEntity> entityTarget;
    Target<Location> locationTarget;
    Sound sound;
    String soundId = null;
    SoundCategory soundCategory;
    float volume;
    float pitch;

    public PlaySoundAbility(Target<BolsterEntity> target, Target<Location> locationTarget, Sound sound, SoundCategory soundCategory, float volume, float pitch)
    {
        this(target, locationTarget, soundCategory, volume, pitch);

        this.sound = sound;
    }

    public PlaySoundAbility(Target<BolsterEntity> target, Target<Location> locationTarget, String sound, SoundCategory soundCategory, float volume, float pitch)
    {
        this(target, locationTarget, soundCategory, volume, pitch);

        this.soundId = sound;
    }

    private PlaySoundAbility(Target<BolsterEntity> target, Target<Location> locationTarget, SoundCategory soundCategory, float volume, float pitch)
    {
        super();

        this.condition(new IsEntityTypeCondition(Target.CASTER, EntityType.PLAYER));

        this.entityTarget = target;
        this.locationTarget = locationTarget;
        this.soundCategory = soundCategory;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void onActivate(Properties properties)
    {
        Player player = (Player) this.entityTarget.get(properties).getBukkit();

        if (this.soundId == null)
        {
            player.playSound(this.locationTarget.get(properties), this.sound, this.soundCategory, this.volume, this.pitch);
        }
        else
        {
            player.playSound(this.locationTarget.get(properties), this.soundId, this.soundCategory, this.volume, this.pitch);
        }
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
