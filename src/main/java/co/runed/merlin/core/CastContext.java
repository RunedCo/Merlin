package co.runed.merlin.core;

import co.runed.merlin.items.ItemDefinition;
import co.runed.merlin.items.ItemImpl;
import co.runed.merlin.spells.Spell;
import co.runed.merlin.spells.SpellProvider;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class CastContext {
    private final Caster caster;
    private final Location castLocation;
    private final SpellProvider provider;
    private final Spell spell;

    public CastContext(LivingEntity entity, Spell spell) {
        this.caster = Caster.from(entity);
        this.castLocation = entity.getLocation();

        this.spell = spell;
        this.provider = spell.getParent();
    }

    public Caster getCaster() {
        return caster;
    }

    public LivingEntity getCasterEntity() {
        return getCaster().getEntity();
    }

    public World getWorld() {
        return castLocation.getWorld();
    }

    public ItemImpl getItem() {
        if (spell.getParent() instanceof ItemImpl item) {
            return item;
        }

        return null;
    }

    public Spell getSpell() {
        return spell;
    }

    public Location getCastLocation() {
        return castLocation;
    }

    public SpellProvider getProvider() {
        return provider;
    }

    public boolean itemEquals(@NotNull ItemDefinition item) {
        return item.equals(getItem());
    }

    public boolean hasItem() {
        return this.spell.getParent() instanceof ItemImpl;
    }
}
