package co.runed.merlin.conditions.entity;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.game.Team;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;

public class IsOnTeamCondition extends Condition
{
    Target<BolsterEntity> target;
    Team team;

    public IsOnTeamCondition(Target<BolsterEntity> target, Team team)
    {
        super();

        this.target = target;
        this.team = team;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        BolsterEntity entity = this.target.get(properties);

        return this.team.isInTeam(entity.getBukkit());
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        return null;
//
//        if (inverted) return ChatColor.RED + "You must not be on the " + this.team.getName() + " to use this ability!";
//
//        return ChatColor.RED + "You must be on the " + this.team.getName() + " to use this ability!";
    }
}
