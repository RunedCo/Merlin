package co.runed.merlin.concept.items;

import co.runed.bolster.util.Category;
import co.runed.bolster.util.ItemBuilder;
import co.runed.bolster.util.registries.Definition;
import co.runed.bolster.util.registries.Registry;
import co.runed.merlin.concept.MerlinRegistries;
import co.runed.merlin.concept.definitions.SpellProviderDefinition;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.spells.SpellType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ItemDefinition extends SpellProviderDefinition<ItemImpl> {
    private final Map<SpellDefinition, ItemRequirement> itemRequirements = new HashMap<>();
    private int maxItems = -1;
    private boolean clearOnRemove = true;
    private AmmoDefinition ammoDefinition = null;
    private int maxAmmo = 64;
    private ItemBuilder baseItemStack = new ItemBuilder(Material.STICK);

    public ItemDefinition(String id) {
        this(id, ItemImpl::new);
    }

    public ItemDefinition(String id, Function<ItemDefinition, ItemImpl> supplier) {
        super(id, (def) -> supplier.apply((ItemDefinition) def));
    }

    @Override
    public ItemDefinition addSpell(SpellDefinition spell, SpellType spellType) {
        return this.addSpell(spell, spellType, 0L);
    }

    @Override
    public ItemDefinition addSpell(SpellDefinition spell, SpellType spellType, long frequency) {
        return this.addSpell(spell, spellType, frequency, ItemRequirement.ALWAYS);
    }

    public ItemDefinition addSpell(SpellDefinition spell, SpellType spellType, ItemRequirement requirement) {
        return this.addSpell(spell, spellType, 0L, requirement);
    }

    public ItemDefinition addSpell(SpellDefinition spell, SpellType spellType, long frequency, ItemRequirement requirement) {
        itemRequirements.put(spell, requirement);

        return (ItemDefinition) super.addSpell(spell, spellType, frequency);
    }

    @Override
    public ItemDefinition setName(String name) {
        return (ItemDefinition) super.setName(name);
    }

    public ItemDefinition setMaxInInventory(int maxItems) {
        this.maxItems = maxItems;

        return this;
    }

    public int getMaxInInventory() {
        return maxItems;
    }

    public ItemDefinition setBaseItemStack(ItemBuilder baseItemStack) {
        this.baseItemStack = baseItemStack;

        return this;
    }

    public ItemBuilder getBaseItemStack() {
        return baseItemStack;
    }

    @Override
    public ItemDefinition from(Definition<ItemImpl> parent) {
        super.from(parent);

        if (parent instanceof ItemDefinition itemDef) {
            itemRequirements.putAll(itemDef.itemRequirements);
            baseItemStack = itemDef.getBaseItemStack();
        }

        return this;
    }

    @Override
    public ItemDefinition category(Category... categories) {
        return (ItemDefinition) super.category(categories);
    }

    public ItemDefinition setAmmoDefinition(AmmoDefinition ammoDefinition) {
        this.ammoDefinition = ammoDefinition;

        return this;
    }

    public AmmoDefinition getAmmoDefinition() {
        return ammoDefinition;
    }

    public boolean hasAmmo() {
        return this.ammoDefinition != null;
    }

    public ItemDefinition setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;

        return this;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public ItemDefinition clearOnRemove(boolean clearOnRemove) {
        this.clearOnRemove = clearOnRemove;

        return this;
    }

    public boolean shouldClearOnRemove() {
        return clearOnRemove;
    }

    @Override
    public ItemDefinition register() {
        return (ItemDefinition) super.register();
    }

    @Override
    public Registry<Definition<ItemImpl>> getRegistry() {
        return MerlinRegistries.ITEMS;
    }

    @Override
    public ItemStack getIcon() {
        return create().toItemStack();
    }

    @Override
    protected ItemImpl preCreate(ItemImpl output) {
        var value = super.preCreate(output);

        value.setItemRequirements(itemRequirements);

        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemImpl item) {
            return item.getDefinition().equals(this);
        }

        return super.equals(obj);
    }

    public static @Nullable ItemDefinition from(ItemStack stack) {
        var id = ItemManager.getInstance().getIdFromStack(stack);

        if (id == null) return null;

        return (ItemDefinition) MerlinRegistries.ITEMS.get(id);
    }
}
