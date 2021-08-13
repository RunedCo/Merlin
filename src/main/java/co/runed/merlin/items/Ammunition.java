package co.runed.merlin.items;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.util.ItemBuilder;
import co.runed.merlin.core.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class Ammunition extends Item
{
    private ItemStack emptyStack = new ItemStack(Material.STONE_BUTTON);
    private ItemStack fullStack = new ItemStack(Material.ARROW);

    private boolean enabled = true;
    private int ammoSlot;
    private int maxAmmo = 64;
    private int ammoCount = 0;

    public void setEmptyItemStack(ItemStack stack)
    {
        this.emptyStack = stack;
    }

    public void setFullItemStack(ItemStack stack)
    {
        this.fullStack = stack;
    }

    public void setAmmoSlot(int slot)
    {
        this.ammoSlot = slot;
    }

    public int getAmmoSlot()
    {
        return ammoSlot;
    }

    public void setMaxAmmo(int maxAmmo)
    {
        this.maxAmmo = maxAmmo;
    }

    public int getMaxAmmo()
    {
        return maxAmmo;
    }

    public void enable()
    {
        this.enabled = true;
    }

    public void disable()
    {
        this.enabled = false;
    }

    @Override
    public void create()
    {

    }

    public void addAmmo(int amount)
    {
        this.ammoCount += amount;
        this.ammoCount = Math.min(this.ammoCount, this.getMaxAmmo());

        if (this.getEntity() == null) return;

        ItemManager itemManager = ItemManager.getInstance();

        Inventory inventory = BolsterEntity.from(this.getEntity()).getPlayerInventory();

        if (inventory.getItem(this.getAmmoSlot()) == null) inventory.setItem(this.getAmmoSlot(), this.toItemStack());

        itemManager.rebuildItemStack(getEntity(), this.getId());

        inventory.getItem(this.getAmmoSlot()).setAmount(Math.max(1, this.ammoCount));
    }

    public int getCurrentAmmo()
    {
        return ammoCount;
    }

    @Override
    public ItemStack toItemStack()
    {
        ItemBuilder builder = new ItemBuilder(super.toItemStack());
        ItemStack stack = ammoCount > 0 && enabled ? fullStack : emptyStack;

        builder = builder.setMaterial(stack.getType())
                .addLore(ChatColor.GRAY + "" + getCurrentAmmo() + " / " + getMaxAmmo());

        if (stack.hasItemMeta() && stack.getItemMeta().hasCustomModelData())
        {
            builder = builder.setCustomModelData(stack.getItemMeta().getCustomModelData());
        }

        return builder.build();
    }
}
