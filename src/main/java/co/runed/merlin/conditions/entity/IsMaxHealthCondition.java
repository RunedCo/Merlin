package co.runed.merlin.conditions.entity;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.ChatColor;

public class IsMaxHealthCondition extends Condition
{
    Target<BolsterEntity> target;

    public IsMaxHealthCondition(Target<BolsterEntity> target)
    {
        super();

        this.target = target;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        BolsterEntity entity = this.target.get(properties);
        double maxHealth = entity.getMaxHealth();

        return entity.getHealth() >= maxHealth;
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        if (inverted) return ChatColor.RED + "You are already at full health!";

        return ChatColor.RED + "You are not at full health!";
    }
}
