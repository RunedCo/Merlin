package co.runed.merlin.concept.triggers.inventory;

import co.runed.merlin.concept.items.ItemManager;
import co.runed.merlin.concept.spells.SpellManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerSelectListener implements Listener {
    private void trigger(Event event, LivingEntity entity, ItemStack newItem, ItemStack oldItem) {
        if (newItem != null) {
            SpellManager.getInstance().run(entity, SelectItemTrigger.class, (spell, context) -> spell.onSelectItem(context.setEvent(event), new SelectItemParams(event, newItem)));
        }

        if (oldItem != null) {
            SpellManager.getInstance().run(entity, DeselectItemTrigger.class, (spell, context) -> spell.onDeselectItem(context.setEvent(event), new SelectItemParams(event, oldItem)));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSelectItem(PlayerItemHeldEvent event) {
        var player = event.getPlayer();

        var inv = player.getInventory();

        var newItem = inv.getItem(event.getNewSlot());
        var previousItem = inv.getItem(event.getPreviousSlot());

        this.trigger(event, player, newItem, previousItem);
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

        this.trigger(event, player, null, item);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickupItem(EntityPickupItemEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;

        var player = (Player) event.getEntity();
        var inv = player.getInventory();

        var newItem = event.getItem().getItemStack();

        if (!inv.getItemInMainHand().isSimilar(newItem)) return;

        this.trigger(event, player, newItem, null);
    }
}
