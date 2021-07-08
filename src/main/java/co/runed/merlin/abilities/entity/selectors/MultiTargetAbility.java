package co.runed.merlin.abilities.entity.selectors;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.conditions.Condition;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An ability that runs for multiple targets
 */
public class MultiTargetAbility extends Ability
{
    Function<Properties, List<Entity>> entityFunction;
    int maxTargets = -1; // -1 is no limit
    List<Target<BolsterEntity>> ignoredTargets = new ArrayList<>();
    boolean shouldActivateIfEmpty = false;

    private boolean lastRunEmpty;
    private List<Condition.Data> parentConditions = new ArrayList<>();
    private boolean addConditionsToParent = true;

    public MultiTargetAbility(Function<Properties, List<Entity>> entityFunction)
    {
        super();

        this.entityFunction(entityFunction);

//        this.setEvaluateConditions(false);

        this.moveDefaultConditions();
    }

    private void moveDefaultConditions()
    {
        this.parentConditions = new ArrayList<>(this.conditions);
        this.conditions.clear();
    }

    public MultiTargetAbility entityFunction(Function<Properties, List<Entity>> entityFunction)
    {
        this.entityFunction = entityFunction;

        return this;
    }

    public MultiTargetAbility maxTargets(int maxTargets)
    {
        this.maxTargets = maxTargets;

        return this;
    }

    public MultiTargetAbility ignoredTarget(Target<BolsterEntity> target)
    {
        this.ignoredTargets.add(target);

        return this;
    }

    public MultiTargetAbility ignoreIf(Supplier<Boolean> func)
    {
        return this;
    }

    public MultiTargetAbility shouldActivateIfEmpty(boolean shouldActivateIfEmpty)
    {
        this.shouldActivateIfEmpty = shouldActivateIfEmpty;

        return this;
    }

    @Override
    public String getDescription()
    {
        if (super.getDescription() != null) return super.getDescription();

        StringBuilder desc = new StringBuilder();

        for (Ability ability : this.getChildren())
        {
            if (ability.getDescription() == null || ability.getDescription().isEmpty()) continue;

            desc.append(ability.getDescription()).append("\n");
        }

        return desc.toString();
    }

    @Override
    public boolean evaluateConditions(Properties properties)
    {
        // COPY OLD CONDITIONS
        List<Condition.Data> oldConditions = new ArrayList<>(this.conditions);

        // REPLACE OLD CONDITIONS WITH PARENT CONDITIONS
        this.conditions = new ArrayList<>(this.parentConditions);

        // EVALUATE OLD + PARENT
        boolean success = super.evaluateConditions(properties);

        // REVERT TO OLD CONDITIONS
        this.conditions = oldConditions;

        return success;
    }

    // TODO might not work (see old implementation on github)
    @Override
    public void activateAbilityAndChildren(Properties properties)
    {
        List<Entity> entities = entityFunction.apply(properties);

        for (Target<BolsterEntity> ignored : this.ignoredTargets)
        {
            entities.remove(ignored.get(properties).getBukkit());
        }

        int count = Math.min(maxTargets > 0 ? maxTargets : entities.size(), entities.size());

        lastRunEmpty = entities.size() <= 0;

        for (int i = 0; i < count; i++)
        {
            Entity entity = entities.get(i);
            Properties newProperties = new Properties(properties);
            newProperties.set(AbilityProperties.TARGET, entity);

            if (super.canActivate(newProperties) && super.evaluateConditions(newProperties))
            {
                super.activateAbilityAndChildren(newProperties);
            }
        }
    }

    @Override
    public void onActivate(Properties properties)
    {

    }

    @Override
    public void onPostActivate(Properties properties)
    {
        if (!lastRunEmpty || shouldActivateIfEmpty) super.onPostActivate(properties);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {

    }
}
