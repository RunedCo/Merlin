package co.runed.merlin.triggers.inventory;

import co.runed.merlin.core.SpellManager;
import co.runed.merlin.items.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryListener implements Listener {
    /* Events for changing selected item */
    private void onSelect(Event event, LivingEntity entity, ItemStack newItem, ItemStack oldItem) {
        if (newItem != null) {
            SpellManager.getInstance().run(entity, new SelectItemTrigger(event, newItem));
        }

        if (oldItem != null) {
            SpellManager.getInstance().run(entity, new DeselectItemTrigger(event, oldItem));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSelectItem(PlayerItemHeldEvent event) {
        var player = event.getPlayer();

        var inv = player.getInventory();

        var newItem = inv.getItem(event.getNewSlot());
        var previousItem = inv.getItem(event.getPreviousSlot());

        this.onSelect(event, player, newItem, previousItem);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryInteract(InventoryInteractEvent event) {

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDropItem(PlayerDropItemEvent event) {
        var player = event.getPlayer();
        var inv = player.getInventory();

        var item = event.getItemDrop().getItemStack();
        var itemId = ItemManager.getInstance().getIdFromStack(item);

        if (itemId == null) return;
        if (!itemId.equals(ItemManager.getInstance().getIdFromStack(inv.getItemInMainHand()))) return;

        this.onSelect(event, player, null, item);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickupItem(EntityPickupItemEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;

        var player = (Player) event.getEntity();
        var inv = player.getInventory();

        var newItem = event.getItem().getItemStack();

        if (!inv.getItemInMainHand().isSimilar(newItem)) return;

        this.onSelect(event, player, newItem, null);
    }

    /* Inventory Click Events */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerClickInventory(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;

        var stack = event.getClickedInventory().getItem(event.getSlot());

        if (stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName() && ChatColor.stripColor(stack.getItemMeta().getDisplayName()).equals("")) {
            event.setResult(Event.Result.DENY);
        }

        SpellManager.getInstance().run(player, new ClickInventoryTrigger(event));
    }

    // TODO player open and close inv events
//    @EventHandler(priority = EventPriority.HIGHEST)
//    private void onPlayerOpenInventory(InventoryOpenEvent event) {
//        Player player = (Player) event.getPlayer();
//
//        Properties properties = new Properties();
//        properties.set(AbilityProperties.INVENTORY, event.getInventory());
//        properties.set(AbilityProperties.EVENT, event);
//
//        AbilityManager.getInstance().trigger(player, AbilityTrigger.ON_OPEN_INVENTORY, properties);
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    private void onPlayerCloseInventory(InventoryCloseEvent event) {
//        Player player = (Player) event.getPlayer();
//
//        Properties properties = new Properties();
//        properties.set(AbilityProperties.INVENTORY, event.getInventory());
//        properties.set(AbilityProperties.EVENT, event);
//
//        AbilityManager.getInstance().trigger(player, AbilityTrigger.ON_CLOSE_INVENTORY, properties);
//    }
}
