package co.runed.merlin.conditions.abilities;

import co.runed.bolster.common.properties.Properties;
import co.runed.bolster.common.properties.Property;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;

public class PropertyEqualsCondition<T> extends Condition
{
    Property<T> property;
    T value;

    public PropertyEqualsCondition(Property<T> property, T value)
    {
        this.property = property;
        this.value = value;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        return properties.get(this.property).equals(this.value);
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        return null;
    }
}
