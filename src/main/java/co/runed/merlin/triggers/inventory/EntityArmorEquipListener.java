package co.runed.merlin.triggers.inventory;

import co.runed.merlin.core.SpellManager;
import co.runed.merlin.items.ItemManager;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;

public class EntityArmorEquipListener implements Listener {
    private final EnumSet<EquipmentSlot> armorSlots = EnumSet.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.HEAD, EquipmentSlot.CHEST);

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        var player = event.getPlayer();

        var newStack = event.getNewItem();
        var oldStack = event.getOldItem();

        if (newStack == null || oldStack == null || ItemManager.getInstance().areStacksEqual(newStack, oldStack))
            return;

        if (oldStack.getType() != Material.AIR) {
            SpellManager.getInstance().run(player, new UnequipArmorTrigger(event, oldStack));
        }

        if (newStack.getType() != Material.AIR) {
            SpellManager.getInstance().run(player, new EquipArmorTrigger(event, newStack));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntitySpawnEquip(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        var equipment = entity.getEquipment();

        if (equipment == null) return;

        for (var slot : armorSlots) {
            var stack = equipment.getItem(slot);

            if (stack.getType() == Material.AIR) continue;

            var fakeEvent = new PlayerArmorChangeEvent(null, PlayerArmorChangeEvent.SlotType.valueOf(slot.name()), new ItemStack(Material.AIR), stack);

            SpellManager.getInstance().run(entity, new EquipArmorTrigger(fakeEvent, stack));
        }
    }
}