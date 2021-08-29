package co.runed.merlin.concept;

import co.runed.bolster.damage.DamageSource;
import co.runed.bolster.entity.BolsterEntity;
import co.runed.merlin.concept.items.ItemDefinition;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Caster implements DamageSource {
    private static final Map<UUID, Caster> _casters = new HashMap<>();

    private LivingEntity entity;

    public Caster(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    public Location getLocation() {
        return getEntity().getLocation();
    }

    public Location getEyeLocation() {
        return getEntity().getEyeLocation();
    }

//    public void addSpell(Spell spell) {
//        spells.add(spell);
//
//        if (spell.isEnabled()) return;
//
//        spell.setOwner(entity);
//        spell.setEnabled(true);
//
//        SpellManager.getInstance().run(getEntity(), EnableTrigger.class, EnableTrigger::onEnable);
//    }
//
//    public void disableSpell(Spell spell) {
//        spell.setEnabled(false);
//    }
//
//    public void removeSpell(Spell spell) {
//        SpellManager.getInstance().run(getEntity(), DisableTrigger.class, DisableTrigger::onDisable);
//
//        spells.remove(spell);
//        spell.destroy();
//    }

    public boolean isHolding(ItemDefinition definition) {
        return isEquipped(definition, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);
    }

    public boolean isArmor(ItemDefinition definition) {
        return isEquipped(definition, EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.HEAD, EquipmentSlot.LEGS);
    }

    public boolean isEquipped(ItemDefinition definition) {
        return isEquipped(definition, EnumSet.allOf(EquipmentSlot.class).toArray(new EquipmentSlot[0]));
    }

    public boolean isEquipped(ItemDefinition definition, EquipmentSlot... slots) {
        var entity = getEntity();
        var equipment = entity.getEquipment();

        if (equipment == null) return false;

        for (var slot : slots) {
            var item = ItemDefinition.from(equipment.getItem(slot));

            if (definition.equals(item)) return true;
        }

        return false;
    }

    public BolsterEntity toBolster() {
        return BolsterEntity.from(getEntity());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LivingEntity entity) {
            return entity.getUniqueId().equals(this.getEntity().getUniqueId());
        }

        if (obj instanceof BolsterEntity entity) {
            return entity.getUniqueId().equals(this.getEntity().getUniqueId());
        }

        return super.equals(obj);
    }

    public static Caster from(BolsterEntity entity) {
        return from(entity.getEntity());
    }

    public static Caster from(LivingEntity entity) {
        var caster = from(entity.getUniqueId());

        if (caster == null) caster = new Caster(entity);

        caster.setEntity(entity);

        _casters.put(entity.getUniqueId(), caster);

        return caster;
    }

    public static Caster from(UUID uuid) {
        if (_casters.containsKey(uuid)) {
            return _casters.get(uuid);
        }

        return null;
    }
}
