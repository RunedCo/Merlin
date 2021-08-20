package co.runed.merlin.costs;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.game.Cost;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.core.ManaManager;

public class ManaCost extends Cost
{
    float cost = 0;

    public ManaCost(float cost)
    {
        this.cost = cost;
    }

    public float getCost()
    {
        return cost;
    }

    @Override
    public boolean evaluate(Properties properties)
    {
        return ManaManager.getInstance().hasEnoughMana(properties.get(AbilityProperties.CASTER).getBukkit(), this.cost);
    }

    @Override
    public boolean run(Properties properties)
    {
        ManaManager manager = ManaManager.getInstance();
        BolsterEntity caster = properties.get(AbilityProperties.CASTER);

        if (this.cost <= 0) return true;

        if (manager.getCurrentMana(caster.getBukkit()) < this.cost)
        {
            return false;
        }

        manager.addCurrentMana(caster.getBukkit(), -this.cost);

        return true;
    }

    @Override
    public String getErrorMessage(Properties properties)
    {
        return "You don't have enough mana!";
    }
}
