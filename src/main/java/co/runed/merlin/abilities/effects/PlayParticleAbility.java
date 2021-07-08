package co.runed.merlin.abilities.effects;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.fx.particles.ParticleInfo;
import co.runed.bolster.fx.particles.ParticleType;
import co.runed.bolster.game.PlayerData;
import co.runed.bolster.managers.PlayerManager;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.conditions.entity.IsEntityTypeCondition;
import de.slikey.effectlib.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PlayParticleAbility extends Ability
{
    Target<BolsterEntity> target;
    ParticleType type;
    Target<Location> location;
    int count;
    int offsetX;
    int offsetY;
    int offsetZ;
    Effect effect = null;

    public PlayParticleAbility(Target<BolsterEntity> target, ParticleType type, Target<Location> location, Effect effect)
    {
        this(target, type, location);

        this.effect = effect;
    }

    public PlayParticleAbility(Target<BolsterEntity> target, ParticleType type, Target<Location> location, int count, int offsetX, int offsetY, int offsetZ)
    {
        this(target, type, location);

        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    private PlayParticleAbility(Target<BolsterEntity> target, ParticleType type, Target<Location> location)
    {
        super();

        this.target = target;
        this.type = type;
        this.location = location;

        this.condition(new IsEntityTypeCondition(Target.CASTER, EntityType.PLAYER));
        this.condition(new IsEntityTypeCondition(target, EntityType.PLAYER));
    }

    @Override
    public void onActivate(Properties properties)
    {
        Player caster = (Player) this.getCaster();
        PlayerData data = PlayerManager.getInstance().getPlayerData(caster);

        Player target = (Player) this.target.get(properties).getBukkit();

        ParticleInfo info = data.getActiveParticleSet().get(this.type);

        if (effect != null)
        {
            info.playEffect(target, location.get(properties), effect);
            return;
        }

        info.spawnParticle(target, location.get(properties), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
