package co.runed.merlin.items;

import co.runed.bolster.util.Category;

import java.util.concurrent.Callable;

public class AmmoDefinition extends ItemDefinition
{
    public AmmoDefinition(String id, Callable<Item> callable)
    {
        super(id, callable);
    }

    @Override
    public AmmoDefinition setName(String name)
    {
        return (AmmoDefinition) super.setName(name);
    }

    @Override
    public AmmoDefinition category(Category... category)
    {
        return (AmmoDefinition) super.category(category);
    }

    @Override
    public AmmoDefinition register()
    {
        return (AmmoDefinition) super.register();
    }
}
