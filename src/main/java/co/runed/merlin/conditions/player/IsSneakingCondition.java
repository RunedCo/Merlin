package co.runed.merlin.conditions.player;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class IsSneakingCondition extends Condition
{
    Target<BolsterEntity> target;

    public IsSneakingCondition(Target<BolsterEntity> target)
    {
        super();

        this.target = target;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        LivingEntity entity = this.target.get(properties).getBukkit();

        return entity instanceof Player player && player.isSneaking();
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        if (inverted) return ChatColor.RED + "You must not be sneaking to use this ability!";

        return ChatColor.RED + "You must be sneaking to use this ability!";
    }
}
