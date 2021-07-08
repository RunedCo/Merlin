package co.runed.merlin.abilities.entity.movement;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.BukkitUtil;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.target.Target;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class TeleportAbility extends Ability
{
    Target<BolsterEntity> target;
    Location location;

    public TeleportAbility(Target<BolsterEntity> target, Location location)
    {
        super();

        this.location = location;
        this.target = target;
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {
        var locString = config.getString("location");

        if (locString != null) this.location = BukkitUtil.stringToLocation(locString);
    }

    @Override
    public void onActivate(Properties properties)
    {
        this.target.get(properties).teleport(location);
    }
}
