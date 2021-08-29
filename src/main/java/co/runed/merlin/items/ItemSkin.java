package co.runed.merlin.items;

import co.runed.bolster.common.util.Identifiable;
import co.runed.bolster.util.ItemBuilder;
import co.runed.bolster.util.config.Configurable;
import co.runed.merlin.concept.items.ItemManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemSkin implements Identifiable, Configurable {
    private final String id;
    private String name = "";
    private int customModelData = 0;
    private boolean showName = false;

    public ItemSkin(String id, String name, int customModelData, boolean showName) {
        this.id = id;
        this.name = name;
        this.customModelData = customModelData;
        this.showName = showName;
    }

    @Override
    public String getId() {
        // TODO
        return "";
//        return MerlinRegistries.ITEM_SKINS.getId(this);
    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        name = config.getString("name", name);
        showName = config.getBoolean("show-name", showName);
    }

    public String getName() {
        return name;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public boolean shouldShowName() {
        return showName;
    }

    public ItemStack toItemStack(ItemStack input) {
        return new ItemBuilder(input)
                .setPersistentData(ItemManager.ITEM_SKIN_KEY, PersistentDataType.STRING, getId())
                .build();
    }
}
