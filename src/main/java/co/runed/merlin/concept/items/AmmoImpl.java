package co.runed.merlin.concept.items;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AmmoImpl extends ItemImpl {
    private int ammoCount = 10;
    private int ammoSlot;
    private boolean displayEnabled = true;
    private ItemStack fullStack = new ItemStack(Material.ARROW);
    private ItemStack emptyStack = new ItemStack(Material.IRON_INGOT);

    public AmmoImpl(AmmoDefinition definition) {
        super(definition);
    }

    public void setDisplayEnabled(boolean displayEnabled) {
        this.displayEnabled = displayEnabled;

        this.rebuild();
    }

    public void setAmmoSlot(int slot) {
        this.ammoSlot = slot;
    }

    public int getAmmoSlot() {
        return ammoSlot;
    }

    public int getMaxAmmo() {
        return getDefinition().getMaxAmmo();
    }

    public void addAmmo(int amount) {
        this.ammoCount += amount;
        this.ammoCount = Math.min(this.ammoCount, getMaxAmmo());

        if (getOwner() == null) return;

        var inventory = BolsterEntity.from(getOwner()).getPlayerInventory();

        if (inventory.getItem(getAmmoSlot()) == null) inventory.setItem(getAmmoSlot(), toItemStack());

        this.rebuild();

        inventory.getItem(getAmmoSlot()).setAmount(Math.max(1, this.ammoCount));
    }

    public int getCurrentAmmo() {
        return ammoCount;
    }

    @Override
    public ItemStack toItemStack() {
        var builder = new ItemBuilder(super.toItemStack());
        var stack = ammoCount > 0 && displayEnabled ? fullStack : emptyStack;

        builder = builder.setMaterial(stack.getType())
                .addLore(ChatColor.GRAY + "" + getCurrentAmmo() + " / " + getMaxAmmo());

        if (stack.hasItemMeta() && stack.getItemMeta().hasCustomModelData()) {
            builder = builder.setCustomModelData(stack.getItemMeta().getCustomModelData());
        }

        return builder.build();
    }
}
