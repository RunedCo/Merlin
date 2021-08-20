package co.runed.merlin.abilities.effects;

import co.runed.bolster.fx.particles.ParticleType;
import co.runed.bolster.game.PlayerData;
import co.runed.bolster.managers.PlayerManager;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlayGlobalParticleAbility extends Ability
{
    Target<Location> target;
    ParticleType type;
    int count;
    int offsetX;
    int offsetY;
    int offsetZ;

    public PlayGlobalParticleAbility(ParticleType type, Target<Location> target, int count, int offsetX, int offsetY, int offsetZ)
    {
        super();

        this.target = target;
        this.type = type;
        this.count = count;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    @Override
    public void onActivate(Properties properties)
    {
        Player caster = (Player) this.getCaster();
        PlayerData data = PlayerManager.getInstance().getPlayerData(caster);
        Location location = this.target.get(properties);

        data.getActiveParticleSet().get(this.type).spawnParticle(location.getWorld(), location, count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
