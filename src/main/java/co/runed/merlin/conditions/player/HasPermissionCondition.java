package co.runed.merlin.conditions.player;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import org.bukkit.ChatColor;

public class HasPermissionCondition extends Condition
{
    Target<BolsterEntity> target;
    String permission;

    public HasPermissionCondition(Target<BolsterEntity> target, String permission)
    {
        super();

        this.target = target;
        this.permission = permission;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        return this.target.get(properties).getBukkit().hasPermission(this.permission);
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        return ChatColor.RED + "You do not have permission to do that!";
    }
}
