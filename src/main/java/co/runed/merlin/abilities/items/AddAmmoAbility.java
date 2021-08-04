package co.runed.merlin.abilities.items;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.Definition;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.core.AbilityManager;
import co.runed.merlin.core.AbilityProviderType;
import co.runed.merlin.core.MerlinRegistries;
import co.runed.merlin.items.AmmoDefinition;
import co.runed.merlin.items.Ammunition;
import co.runed.merlin.items.Item;
import co.runed.merlin.target.Target;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

public class AddAmmoAbility extends Ability
{
    Target<BolsterEntity> target;
    Definition<Item> ammoType;
    int count;

    public AddAmmoAbility(Target<BolsterEntity> target, AmmoDefinition ammoType, int count)
    {
        super();

        this.target = target;
        this.ammoType = ammoType;
        this.count = count;
    }

    @Override
    public void onActivate(Properties properties)
    {
        LivingEntity entity = target.get(properties).getBukkit();
        String ammoId = MerlinRegistries.ITEMS.getId(ammoType);

        if (AbilityManager.getInstance().hasProvider(entity, AbilityProviderType.ITEM, ammoId))
        {
            Ammunition ammunition = (Ammunition) AbilityManager.getInstance().getProvider(entity, AbilityProviderType.ITEM, ammoId);

            ammunition.addAmmo(this.count);
        }
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
