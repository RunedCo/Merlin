package co.runed.merlin.items.skins;

import co.runed.bolster.util.registries.Definition;
import co.runed.bolster.util.registries.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public class ItemSkinDefinition extends Definition<ItemSkin> {
    private String name;

    public ItemSkinDefinition(String id) {
        super(id, (def) -> new ItemSkin((ItemSkinDefinition) def));
    }

    @Override
    public void loadConfig(ConfigurationSection config) {

    }

    @Override
    public @Nullable Registry<Definition<ItemSkin>> getRegistry() {
        return null;
    }
}
