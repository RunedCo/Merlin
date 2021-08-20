package co.runed.merlin.conditions.entity;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffectType;

public class HasPotionEffect extends Condition
{
    Target<BolsterEntity> target;
    PotionEffectType potionEffect;

    public HasPotionEffect(Target<BolsterEntity> target, PotionEffectType potionEffect)
    {
        super();

        this.target = target;
        this.potionEffect = potionEffect;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        return this.target.get(properties).getBukkit().hasPotionEffect(potionEffect);
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        if (inverted) return ChatColor.RED + "You must not have " + potionEffect.getName() + " to use this ability!";

        return ChatColor.RED + "You must have " + potionEffect.getName() + " to use this ability!";
    }
}