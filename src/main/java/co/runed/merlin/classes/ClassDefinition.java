package co.runed.merlin.classes;

import co.runed.bolster.util.Category;
import co.runed.bolster.util.Definition;
import co.runed.bolster.util.registries.DefinitionRegistry;
import co.runed.merlin.core.MerlinRegistries;

import java.util.concurrent.Callable;

public class ClassDefinition extends Definition<MerlinClass>
{
    public ClassDefinition(String id, Callable<MerlinClass> callable)
    {
        super(id, callable);
    }

    @Override
    public ClassDefinition category(Category... category)
    {
        return (ClassDefinition) super.category(category);
    }

    @Override
    public ClassDefinition register()
    {
        return (ClassDefinition) super.register();
    }

    @Override
    public DefinitionRegistry<MerlinClass> getRegistry()
    {
        return MerlinRegistries.CLASSES;
    }
}
