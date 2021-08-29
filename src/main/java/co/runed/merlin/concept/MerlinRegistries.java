package co.runed.merlin.concept;

import co.runed.bolster.util.registries.DefinitionRegistry;
import co.runed.merlin.Merlin;
import co.runed.merlin.concept.classes.ClassImpl;
import co.runed.merlin.concept.items.ItemImpl;

public class MerlinRegistries {
    public static DefinitionRegistry<ItemImpl> ITEMS = new DefinitionRegistry<>(Merlin.getInstance(), "items");
    public static DefinitionRegistry<ClassImpl> CLASSES = new DefinitionRegistry<>(Merlin.getInstance(), "classes");
}
