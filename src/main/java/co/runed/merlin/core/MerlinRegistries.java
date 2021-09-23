package co.runed.merlin.core;

import co.runed.bolster.util.registries.DefinitionRegistry;
import co.runed.merlin.Merlin;
import co.runed.merlin.classes.ClassImpl;
import co.runed.merlin.items.ItemImpl;

public class MerlinRegistries {
    public static DefinitionRegistry<ItemImpl> ITEMS = new DefinitionRegistry<>(Merlin.getInstance(), "items");
    public static DefinitionRegistry<ClassImpl> CLASSES = new DefinitionRegistry<>(Merlin.getInstance(), "classes");
}
