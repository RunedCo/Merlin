package co.runed.merlin.items;

import co.runed.bolster.common.util.IIdentifiable;
import co.runed.bolster.util.ItemBuilder;
import co.runed.bolster.util.config.IConfigurable;
import co.runed.merlin.core.MerlinRegistries;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemSkin implements IIdentifiable, IConfigurable
{
    private final String id;
    private String name = "";
    private int customModelData = 0;
    private boolean showName = false;

    public ItemSkin(String id, String name, int customModelData, boolean showName)
    {
        this.id = id;
        this.name = name;
        this.customModelData = customModelData;
        this.showName = showName;
    }

    @Override
    public String getId()
    {
        return MerlinRegistries.ITEM_SKINS.getId(this);
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {
        name = config.getString("name", name);
        showName = config.getBoolean("show-name", showName);
    }

    @Override
    public void create()
    {

    }

    public String getName()
    {
        return name;
    }

    public int getCustomModelData()
    {
        return customModelData;
    }

    public boolean shouldShowName()
    {
        return showName;
    }

    public ItemStack toItemStack(ItemStack input)
    {
        return new ItemBuilder(input)
                .setPersistentData(Item.ITEM_SKIN_KEY, PersistentDataType.STRING, getId())
                .build();
    }
}
