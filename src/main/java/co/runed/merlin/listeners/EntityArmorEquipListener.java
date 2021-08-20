package co.runed.merlin.listeners;

import co.runed.merlin.core.AbilityManager;
import co.runed.bolster.common.properties.Properties;
import co.runed.merlin.abilities.AbilityProperties;
import co.runed.merlin.abilities.AbilityTrigger;
import co.runed.merlin.core.ItemManager;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;

public class EntityArmorEquipListener implements Listener
{
    private final EnumSet<EquipmentSlot> armorSlots = EnumSet.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.HEAD, EquipmentSlot.CHEST);

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerArmorChange(PlayerArmorChangeEvent event)
    {
        Player player = event.getPlayer();
        PlayerArmorChangeEvent.SlotType slot = event.getSlotType();

        ItemStack newStack = event.getNewItem();
        ItemStack oldStack = event.getOldItem();

        if (newStack == null || oldStack == null || ItemManager.getInstance().areStacksEqual(newStack, oldStack))
            return;

        if (oldStack.getType() != Material.AIR)
        {
            Properties unequipProperties = new Properties();
            unequipProperties.set(AbilityProperties.EVENT, event);
            unequipProperties.set(AbilityProperties.ITEM_STACK, oldStack);
            unequipProperties.set(AbilityProperties.ARMOR_SLOT, slot);

            AbilityManager.getInstance().trigger(player, AbilityTrigger.ON_UNEQUIP_ARMOR, unequipProperties);
        }

        if (newStack.getType() != Material.AIR)
        {
            Properties equipProperties = new Properties();
            equipProperties.set(AbilityProperties.EVENT, event);
            equipProperties.set(AbilityProperties.ITEM_STACK, newStack);
            equipProperties.set(AbilityProperties.ARMOR_SLOT, slot);

            AbilityManager.getInstance().trigger(player, AbilityTrigger.ON_EQUIP_ARMOR, equipProperties);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEntitySpawnEquip(EntitySpawnEvent event)
    {
        if (!(event.getEntity() instanceof LivingEntity)) return;

        LivingEntity entity = (LivingEntity) event.getEntity();

        EntityEquipment equipment = entity.getEquipment();

        for (EquipmentSlot slot : armorSlots)
        {
            ItemStack stack = equipment.getItem(slot);

            if (stack == null || stack.getType() == Material.AIR) continue;

            Properties equipProperties = new Properties();
            equipProperties.set(AbilityProperties.EVENT, event);
            equipProperties.set(AbilityProperties.ITEM_STACK, stack);
            equipProperties.set(AbilityProperties.ARMOR_SLOT, PlayerArmorChangeEvent.SlotType.valueOf(slot.name()));

            AbilityManager.getInstance().trigger(entity, AbilityTrigger.ON_EQUIP_ARMOR, equipProperties);
        }
    }
}