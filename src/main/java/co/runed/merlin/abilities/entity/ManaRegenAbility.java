package co.runed.merlin.abilities.entity;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.core.ManaManager;
import co.runed.merlin.core.MerlinTraits;
import co.runed.merlin.target.Target;
import org.bukkit.configuration.ConfigurationSection;

public class ManaRegenAbility extends Ability
{
    Target<BolsterEntity> target;

    public ManaRegenAbility(Target<BolsterEntity> target)
    {
        this.target = target;
        this.cooldown(0.5d);
    }

    @Override
    public void onActivate(Properties properties)
    {
        BolsterEntity target = this.target.get(properties);

        ManaManager.getInstance().addCurrentMana(target.getBukkit(), target.getTrait(MerlinTraits.MANA_PER_SECOND) / 2);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
