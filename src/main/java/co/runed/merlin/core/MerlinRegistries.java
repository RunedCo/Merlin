package co.runed.merlin.core;

import co.runed.bolster.util.registries.DefinitionRegistry;
import co.runed.bolster.util.registries.Registry;
import co.runed.merlin.Merlin;
import co.runed.merlin.classes.MerlinClass;
import co.runed.merlin.items.Item;
import co.runed.merlin.items.ItemSkin;
import co.runed.merlin.upgrade.Upgrade;

public class MerlinRegistries
{
    public static final DefinitionRegistry<Item> ITEMS = new DefinitionRegistry<>(Merlin.getInstance(), "items");
    public static final DefinitionRegistry<MerlinClass> CLASSES = new DefinitionRegistry<>(Merlin.getInstance(), "classes");
    public static final Registry<ItemSkin> ITEM_SKINS = new Registry<>(Merlin.getInstance(), "skins");
    public static final Registry<Upgrade> UPGRADES = new Registry<>(Merlin.getInstance(), "upgrades");
}
