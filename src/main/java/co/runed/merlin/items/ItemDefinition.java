package co.runed.merlin.items;

import co.runed.bolster.game.traits.Trait;
import co.runed.bolster.util.Category;
import co.runed.bolster.util.ItemBuilder;
import co.runed.bolster.util.registries.Definition;
import co.runed.bolster.util.registries.Registry;
import co.runed.merlin.core.MerlinRegistries;
import co.runed.merlin.items.ammo.AmmoDefinition;
import co.runed.merlin.spells.SpellDefinition;
import co.runed.merlin.spells.SpellProviderDefinition;
import co.runed.merlin.spells.type.SpellType;
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
    private boolean droppable = false;
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
    public ItemDefinition addSpell(SpellDefinition spell) {
        return (ItemDefinition) super.addSpell(spell);
    }

    public ItemDefinition addSpell(SpellDefinition spell, ItemRequirement requirement) {
        return this.addSpell(spell, new SpellType(), requirement);
    }

    @Override
    public ItemDefinition addSpell(SpellDefinition spell, SpellType spellType) {
        return this.addSpell(spell, spellType, ItemRequirement.ALWAYS);
    }

    public ItemDefinition addSpell(SpellDefinition spell, SpellType spellType, ItemRequirement requirement) {
        itemRequirements.put(spell, requirement);

        return (ItemDefinition) super.addSpell(spell, spellType);
    }

    @Override
    public <J> ItemDefinition setTrait(Trait<J> key, J value) {
        return (ItemDefinition) super.setTrait(key, value);
    }

    @Override
    public ItemDefinition setName(String name) {
        return (ItemDefinition) super.setName(name);
    }

    public ItemDefinition setDroppable(boolean droppable) {
        this.droppable = droppable;

        return this;
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
    public ItemDefinition addCategories(Category... categories) {
        return (ItemDefinition) super.addCategories(categories);
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
        value.setDroppable(droppable);

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
