package co.runed.merlin.core;

import co.runed.bolster.util.registries.DefinitionRegistry;
import co.runed.bolster.util.registries.Registry;
import co.runed.merlin.Merlin;
import co.runed.merlin.classes.ClassImpl;
import co.runed.merlin.items.ItemImpl;
import co.runed.merlin.items.ItemSkin;

public class MerlinRegistries {
    public static DefinitionRegistry<ItemImpl> ITEMS = new DefinitionRegistry<>(Merlin.getInstance(), "items");
    public static DefinitionRegistry<ClassImpl> CLASSES = new DefinitionRegistry<>(Merlin.getInstance(), "classes");
    public static Registry<ItemSkin> ITEM_SKINS = new Registry<>(Merlin.getInstance());
}
