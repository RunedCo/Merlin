package co.runed.merlin.items;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.events.player.LoadPlayerDataEvent;
import co.runed.bolster.managers.Manager;
import co.runed.bolster.util.registries.Definition;
import co.runed.merlin.Merlin;
import co.runed.merlin.core.MerlinRegistries;
import co.runed.merlin.core.SpellManager;
import co.runed.merlin.spells.SpellProviderDefinition;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemManager extends Manager {
    public static final NamespacedKey ITEM_ID_KEY = new NamespacedKey(Merlin.getInstance(), "item-id");
    public static final NamespacedKey ITEM_SKIN_KEY = new NamespacedKey(Merlin.getInstance(), "item-skin");

    private static ItemManager _instance;

    public ItemManager(Plugin plugin) {
        super(plugin);

        _instance = this;
    }

    public ItemImpl getOrCreate(LivingEntity entity, Definition<ItemImpl> itemDef) {
        if (!(itemDef instanceof SpellProviderDefinition<ItemImpl> spellItemDef)) return null;

        return (ItemImpl) SpellManager.getInstance().getOrCreateProvider(entity, spellItemDef);
    }

    public boolean canGiveItem(LivingEntity entity, Definition<ItemImpl> spItemDef, int amount) {
        if (spItemDef == null) return false;
        if (!(spItemDef instanceof ItemDefinition itemDef)) return true;

        var currentAmount = getAllInventoryItemCount(entity, itemDef);
        var newAmount = currentAmount + amount;

        var max = itemDef.getMaxInInventory();

        return newAmount <= max || max <= -1;
    }

    public int getAllInventoryItemCount(LivingEntity entity, Definition<ItemImpl> itemDef) {
        var numberFound = 0;

        for (var inventory : BolsterEntity.from(entity).getInventories()) {
            numberFound += getItemCount(inventory, itemDef);
        }

        return numberFound;
    }

    public boolean anyInventoryContainsAtLeast(LivingEntity entity, Definition<ItemImpl> itemDef, int count) {
        return getAllInventoryItemCount(entity, itemDef) >= count;
    }

    public int getItemCount(Inventory inventory, Definition<ItemImpl> itemDef) {
        var numberFound = 0;

        for (var stack : inventory) {
            var stackItemId = getIdFromStack(stack);

            if (stackItemId == null) continue;

            if (stackItemId.equals(itemDef.getId())) numberFound += stack.getAmount();
        }

        return numberFound;
    }

    public boolean inventoryContainsAtLeast(Inventory inventory, Definition<ItemImpl> itemDef, int count) {
        return getItemCount(inventory, itemDef) >= count;
    }

    public @Nullable String getIdFromStack(ItemStack stack) {
        if (stack == null) return null;
        if (stack.getType() == Material.AIR) return null;
        if (!stack.hasItemMeta()) return null;

        var meta = stack.getItemMeta();
        var pdc = meta.getPersistentDataContainer();

        return pdc.get(ITEM_ID_KEY, PersistentDataType.STRING);
    }

    public @Nullable Definition<ItemImpl> getDefFrom(ItemStack stack) {
        return MerlinRegistries.ITEMS.get(getIdFromStack(stack));
    }

    public List<ItemImpl> getItems(LivingEntity entity) {
        return SpellManager.getInstance().getProviders(entity).stream().map((prov) -> {
            if (prov instanceof ItemImpl item) return item;

            return null;
        }).collect(Collectors.toList());
    }

    public void giveItem(LivingEntity entity, Definition<ItemImpl> itemDef, int amount) {
        this.giveItem(entity, BolsterEntity.from(entity).getPlayerInventory(), itemDef, amount);
    }

    public ItemImpl giveItem(LivingEntity entity, Inventory inventory, Definition<ItemImpl> itemDef, int amount) {
        if (itemDef == null) return null;
        var item = getOrCreate(entity, itemDef);

        if (item == null) return null;

        if (!canGiveItem(entity, itemDef, amount)) return null;

        var stack = item.toItemStack();
        stack.setAmount(amount);

        var amountRemaining = amount;
        var maxSize = stack.getMaxStackSize();

        while (amountRemaining > 0) {
            var itemStack = stack.clone();

            var removedAmount = Math.min(amountRemaining, maxSize);

            itemStack.setAmount(removedAmount);

            inventory.addItem(itemStack);

            amountRemaining -= removedAmount;
        }

        return item;
    }

    public void rebuildAllItemStacks(LivingEntity entity) {
        for (var item : getItems(entity)) {
            rebuildItemStack(entity, item.getDefinition());
        }
    }

    public void rebuildItemStack(LivingEntity entity, Definition<ItemImpl> itemDef) {
        var item = getOrCreate(entity, itemDef);

        if (item == null) return;

        for (var inventory : BolsterEntity.from(entity).getInventories()) {
            for (var i = 0; i < inventory.getSize(); i++) {
                var stack = inventory.getItem(i);

                var stackItemId = getIdFromStack(stack);

                if (stackItemId == null || !stackItemId.equals(itemDef.getId())) continue;

                var newStack = item.toItemStack().clone();
                newStack.setAmount(stack.getAmount());

                if (!stack.equals(newStack)) inventory.setItem(i, newStack);
            }
        }
    }

    public List<ItemStack> getAllItemStacks(Inventory inventory, Definition<ItemImpl> itemDef) {
        var out = new ArrayList<ItemStack>();

        if (itemDef != null) {
            for (var stack : inventory) {
                var stackId = getIdFromStack(stack);
                var itemId = itemDef.getId();

                if (itemId.equals(stackId)) out.add(stack);
            }
        }

        return out;
    }

    public ItemStack getFirstItemStack(Inventory inventory, Definition<ItemImpl> itemDef) {
        var allStacks = getAllItemStacks(inventory, itemDef);

        if (allStacks.size() <= 0) return new ItemStack(Material.AIR);

        return allStacks.get(0);
    }

    public void clearItem(LivingEntity entity, Definition<ItemImpl> itemDef) {
        SpellManager.getInstance().removeProvider(entity, itemDef);
    }

    public boolean removeItem(Inventory inventory, @NotNull Definition<ItemImpl> itemDef, int count) {
        var stack = getFirstItemStack(inventory, itemDef).clone();
        stack.setAmount(count);

        if (!inventory.containsAtLeast(stack, count)) {
            var entity = inventory.getViewers().size() > 1 ? inventory.getViewers().get(0) : null;

            // TODO this is a bit jank
            if (entity != null && itemDef instanceof ItemDefinition definition && definition.shouldClearOnRemove() && !anyInventoryContainsAtLeast(entity, itemDef, 1)) {
                clearItem(entity, itemDef);
            }

            inventory.removeItem(stack);

            return false;
        }

        inventory.removeItem(stack);

        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(PlayerJoinEvent event) {
        rebuildOnJoin(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onLoadPlayer(LoadPlayerDataEvent event) {
        rebuildOnJoin(event.getPlayer());
    }

    private void rebuildOnJoin(Player player) {
        if (player == null) return;

        for (var inventory : BolsterEntity.from(player).getInventories()) {
            for (var itemStack : inventory) {
                var itemDef = ItemDefinition.from(itemStack);

                if (itemDef == null) return;

                getOrCreate(player, itemDef);
            }
        }

        rebuildAllItemStacks(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onDropItem(PlayerDropItemEvent event) {
        var player = event.getPlayer();
        var stack = event.getItemDrop().getItemStack();
        var itemDef = ItemDefinition.from(stack);

        if (itemDef == null) return;

        var item = getOrCreate(player, itemDef);

        if (item == null) return;
        if (item.getOwner() == null || player != item.getOwner()) return;
        if (!item.isDroppable()) event.setCancelled(true);
        if (!event.isCancelled() && !anyInventoryContainsAtLeast(player, itemDef, 1)) clearItem(player, itemDef);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPickupItem(EntityPickupItemEvent event) {
        var entity = event.getEntity();
        var stack = event.getItem().getItemStack();

        var itemDef = ItemDefinition.from(stack);

        if (itemDef == null) return;

        getOrCreate(entity, itemDef);
    }


    public static ItemManager getInstance() {
        return _instance;
    }
}
