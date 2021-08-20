package co.runed.merlin.conditions.entity;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.Definition;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.target.Target;
import co.runed.merlin.classes.MerlinClass;
import co.runed.merlin.conditions.Condition;
import co.runed.merlin.conditions.IConditional;
import co.runed.merlin.core.ClassManager;
import co.runed.merlin.core.MerlinRegistries;
import org.bukkit.ChatColor;

public class HasClassCondition extends Condition
{
    Target<BolsterEntity> target;
    String classId;

    public HasClassCondition(Target<BolsterEntity> target, Definition<MerlinClass> classDefinition)
    {
        this(target, MerlinRegistries.CLASSES.getId(classDefinition));
    }

    public HasClassCondition(Target<BolsterEntity> target, String classId)
    {
        this.target = target;
        this.classId = classId;
    }

    @Override
    public boolean evaluate(IConditional conditional, Properties properties)
    {
        BolsterEntity entity = this.target.get(properties);

        if (entity == null) return false;

        MerlinClass merlinClass = ClassManager.getInstance().getClass(entity.getBukkit());

        if (merlinClass == null || this.classId == null) return false;

        return merlinClass.getId().equals(this.classId);
    }

    @Override
    public void onFail(IConditional conditional, Properties properties, boolean inverted)
    {

    }

    @Override
    public String getErrorMessage(IConditional conditional, Properties properties, boolean inverted)
    {
        MerlinClass merlinClass = MerlinRegistries.CLASSES.get(classId).create();

        if (inverted) return ChatColor.RED + "You must not be a " + merlinClass.getName() + " to use this ability!";

        return ChatColor.RED + "You must be a " + merlinClass.getName() + " to use this ability!";
    }
}
