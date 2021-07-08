package co.runed.merlin.conditions;

import co.runed.bolster.util.properties.Properties;
import co.runed.merlin.conditions.base.FunctionCondition;

import java.util.function.BiFunction;

public interface IConditional<T>
{
    default T condition(Condition... condition)
    {
        return this.condition(ConditionPriority.NORMAL, condition);
    }

    default T condition(BiFunction<IConditional, Properties, Boolean> evaluateFunc)
    {
        return this.condition(ConditionPriority.NORMAL, evaluateFunc);
    }

    default T condition(ConditionPriority priority, BiFunction<IConditional, Properties, Boolean> evaluateFunc)
    {
        return this.condition(priority, new FunctionCondition(evaluateFunc));
    }

    T condition(ConditionPriority priority, Condition... condition);

    T shouldShowErrorMessages(boolean showErrors);

    boolean shouldShowErrorMessages();
}
