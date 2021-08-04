package co.runed.merlin.items;

import co.runed.bolster.util.Category;
import co.runed.bolster.util.Definition;
import co.runed.bolster.util.registries.DefinitionRegistry;
import co.runed.merlin.core.MerlinRegistries;

import java.util.concurrent.Callable;

public class ItemDefinition extends Definition<Item>
{
    int maxLevel = 0;
    boolean levelable = false;

    public ItemDefinition(String id, Callable<Item> callable)
    {
        super(id, callable);
    }

    @Override
    public ItemDefinition setName(String name)
    {
        return (ItemDefinition) super.setName(name);
    }

    @Override
    public ItemDefinition category(Category... category)
    {
        return (ItemDefinition) super.category(category);
    }

    @Override
    public ItemDefinition register()
    {
        return (ItemDefinition) super.register();
    }

    @Override
    public DefinitionRegistry<Item> getRegistry()
    {
        return MerlinRegistries.ITEMS;
    }

    public ItemDefinition setLevelable(boolean levelable)
    {
        this.levelable = levelable;

        return this;
    }

    public ItemDefinition setMaxLevel(int maxLevel)
    {
        this.maxLevel = maxLevel;

        return this;
    }

    public boolean isLevelable()
    {
        return levelable;
    }

    public int getMaxLevel()
    {
        return maxLevel;
    }
}
