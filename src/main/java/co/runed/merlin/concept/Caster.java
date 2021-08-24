package co.runed.merlin.concept;

import co.runed.bolster.damage.DamageSource;
import co.runed.bolster.entity.BolsterEntity;
import co.runed.merlin.concept.items.ItemDefinition;
import co.runed.merlin.concept.spells.Spell;
import co.runed.merlin.concept.spells.Spells;
import co.runed.merlin.concept.triggers.lifecycle.DisableTrigger;
import co.runed.merlin.concept.triggers.lifecycle.EnableTrigger;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;

import java.util.*;

public class Caster implements DamageSource {
    private static final Map<UUID, Caster> _casters = new HashMap<>();

    private LivingEntity entity;
    private final Set<Spell> spells = new HashSet<>();

    public Caster(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    public void enableSpell(Spell spell) {
        spells.add(spell);
        spell.setOwner(entity);

        Spells.getInstance().run(getEntity(), EnableTrigger.class, (_spell, _context) -> ((EnableTrigger) _spell).onEnable(_context));
    }

    public void disableSpell(Spell spell) {
        Spells.getInstance().run(getEntity(), DisableTrigger.class, (_spell, _context) -> ((DisableTrigger) _spell).onDisable(_context));

        spells.remove(spell);
        spell.destroy();
    }

    public boolean isHolding(ItemDefinition container) {
        return isEquipped(container, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND);
    }

    public boolean isEquipped(ItemDefinition container) {
        return isEquipped(container, EnumSet.allOf(EquipmentSlot.class).toArray(new EquipmentSlot[0]));
    }

    public boolean isEquipped(ItemDefinition container, EquipmentSlot... slots) {
        var entity = getEntity();
        var equipment = entity.getEquipment();

        if (equipment == null) return false;

        for (var slot : slots) {
            var item = ItemDefinition.from(equipment.getItem(slot));

            if (container.equals(item)) return true;
        }

        return false;
    }

    public static Caster from(BolsterEntity entity) {
        return from(entity.getBukkit());
    }

    public static Caster from(LivingEntity entity) {
        Caster caster = from(entity.getUniqueId());

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
