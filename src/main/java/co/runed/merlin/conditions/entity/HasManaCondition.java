package co.runed.merlin.conditions.entity;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import co.runed.merlin.core.ManaManager;
import co.runed.merlin.costs.ManaCost;
import org.bukkit.ChatColor;

public class HasManaCondition extends Condition
{
    Target<BolsterEntity> target;
    float amount = -1;

    public HasManaCondition(Target<BolsterEntity> target, float amount)
    {
        this(target);

        this.amount = amount;
    }

    public HasManaCondition(Target<BolsterEntity> target)
    {
        super();

        this.target = target;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        if (!(conditional instanceof Ability ability)) return false;

        BolsterEntity entity = this.target.get(properties);

        float reqAmount = amount > 0 ? amount : this.getManaCost(ability);

        return ManaManager.getInstance().hasEnoughMana(entity.getBukkit(), reqAmount);
    }

    private float getManaCost(Ability ability)
    {
        float cost = 0f;

        for (var abilityCost : ability.getCosts())
        {
            if (abilityCost instanceof ManaCost manaCost)
            {
                cost += manaCost.getCost();
            }
        }

        return cost;
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        if (inverted) return ChatColor.LIGHT_PURPLE + "Too much mana!";

        return ChatColor.LIGHT_PURPLE + "Not enough mana!";
    }
}
