package co.runed.merlin.concept.items;

import co.runed.bolster.common.util.IIdentifiable;
import co.runed.bolster.common.util.INameable;
import co.runed.bolster.util.config.IConfigurable;
import co.runed.merlin.Merlin;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.spells.SpellType;
import co.runed.merlin.concept.spells.Spells;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ItemDefinition implements IIdentifiable, INameable, IConfigurable {
    public static final NamespacedKey ITEM_ID_KEY = new NamespacedKey(Merlin.getInstance(), "item-id");
    public static final NamespacedKey ITEM_SKIN_KEY = new NamespacedKey(Merlin.getInstance(), "item-skin");

    public static ItemDefinition RUNEBLADE = new ItemDefinition("runeblade")
            .addSpell(Spells.RUNEBLADE_RUNEDASH, SpellType.NORMAL, ItemRequirements.ANY_HAND)
            .addSpell(Spells.RUNEBLADE_RUNEDASH, SpellType.REPEATING, 10L, ItemRequirements.ANY_HAND);

    private static final Map<String, ItemDefinition> containerMap = new HashMap<>();

    String id;
    String name;
    int maxLevel;

    public ItemDefinition(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        if (name == null) return id;

        return name;
    }

    public ItemDefinition addSpell(SpellDefinition spell, SpellType spellType, ItemRequirements options) {
        return this;
    }

    public ItemDefinition addSpell(SpellDefinition spell, SpellType spellType, long frequency, ItemRequirements options) {
        return this;
    }

    public ItemDefinition setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;

        return this;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public static @Nullable String itemIdFromStack(ItemStack stack) {
        if (stack == null) return null;
        if (stack.getType() == Material.AIR) return null;
        if (!stack.hasItemMeta()) return null;

        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        return pdc.get(ITEM_ID_KEY, PersistentDataType.STRING);
    }

    public static @Nullable ItemDefinition from(ItemStack stack) {
        var id = itemIdFromStack(stack);

        if (id == null) return null;

        return containerMap.getOrDefault(id, null);
    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        maxLevel = config.getInt("max-level", maxLevel);
    }

    @Override
    public void create() {

    }
}
