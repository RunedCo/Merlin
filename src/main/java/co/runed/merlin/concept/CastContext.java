package co.runed.merlin.concept;

import co.runed.merlin.concept.items.ItemDefinition;
import co.runed.merlin.concept.items.ItemImpl;
import co.runed.merlin.concept.spells.Spell;
import co.runed.merlin.concept.spells.SpellProvider;
import co.runed.merlin.concept.triggers.Params;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class CastContext {
    private final Caster caster;
    private final Location castLocation;
    private final SpellProvider provider;
    private final Spell spell;
    private boolean cancelled = false;
    private Params params;

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

    public boolean isCancelled() {
        if (params != null) return params.isCancelled();

        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        if (params != null) params.setCancelled(cancelled);

        this.cancelled = true;
    }

    public CastContext setParams(Params params) {
        this.params = params;

        return this;
    }
}
