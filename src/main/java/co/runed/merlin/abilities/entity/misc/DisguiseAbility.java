package co.runed.merlin.abilities.entity.misc;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public class DisguiseAbility extends Ability
{
    Target<BolsterEntity> target;
    Disguise disguise;

    public DisguiseAbility(EntityType type)
    {
        this(new MobDisguise(DisguiseType.getType(type)));
    }

    public DisguiseAbility(Disguise disguise)
    {
        super();

        this.disguise = disguise;
    }

    public Disguise getDisguise()
    {
        return this.disguise;
    }

    @Override
    public void onActivate(Properties properties)
    {
        DisguiseAPI.disguiseEntity(target.get(properties).getBukkit(), this.disguise);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
