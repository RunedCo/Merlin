package co.runed.merlin.items.ammo;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.ItemBuilder;
import co.runed.merlin.items.ItemImpl;
import co.runed.merlin.items.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AmmoImpl extends ItemImpl {
    private int ammoCount = 10;
    private int ammoSlot;
    private boolean displayEnabled = true;
    private ItemStack fullStack = new ItemBuilder(Material.ARROW).build();
    private ItemStack inactiveStack = new ItemBuilder(Material.PAPER).setCustomModelData(30).build();
    private ItemStack emptyStack = new ItemBuilder(Material.PAPER).setCustomModelData(31).build();

    public AmmoImpl(AmmoDefinition definition) {
        super(definition);
    }

    public void setDisplayEnabled(boolean displayEnabled) {
        this.displayEnabled = displayEnabled;

        rebuild();
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
        ammoCount += amount;
        ammoCount = Math.max(0, Math.min(ammoCount, getMaxAmmo()));

        if (getOwner() == null) return;

        var inventory = BolsterEntity.from(getOwner()).getPlayerInventory();

        rebuild();

        for (var stack : ItemManager.getInstance().getAllItemStacks(inventory, getDefinition())) {
            stack.setAmount(Math.max(1, ammoCount));
        }
    }

    public int getCurrentAmmo() {
        return ammoCount;
    }

    @Override
    public ItemStack toItemStack() {
        var builder = new ItemBuilder(super.toItemStack());
        var stack = ammoCount > 0 ? (displayEnabled ? fullStack : inactiveStack) : emptyStack;

        builder = builder.setMaterial(stack.getType())
                .addLore(ChatColor.GRAY + "" + getCurrentAmmo() + " / " + getMaxAmmo());

        if (stack.hasItemMeta() && stack.getItemMeta().hasCustomModelData()) {
            builder = builder.setCustomModelData(stack.getItemMeta().getCustomModelData());
        }

        return builder.build();
    }
}
