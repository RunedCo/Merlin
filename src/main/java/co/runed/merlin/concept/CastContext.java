package co.runed.merlin.concept;

import co.runed.merlin.concept.items.ItemDefinition;
import co.runed.merlin.concept.items.ItemImpl;
import co.runed.merlin.concept.spells.Spell;
import co.runed.merlin.concept.spells.SpellProvider;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class CastContext {
    private final Caster caster;
    private final Location castLocation;
    private final SpellProvider provider;
    private final Spell spell;
    private boolean cancelled = false;
    private Event event;

    public CastContext(LivingEntity entity, Spell spell) {
        this.caster = Caster.from(entity);
        this.castLocation = entity.getLocation();

        this.spell = spell;
        this.provider = spell.getParent();
    }

    public Caster getCaster() {
        return caster;
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
        if (event instanceof Cancellable cancellable) {
            return cancellable.isCancelled();
        }

        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        if (event instanceof Cancellable cancellable) {
            cancellable.setCancelled(cancelled);
        }

        this.cancelled = true;
    }

    public CastContext setEvent(Event event) {
        this.event = event;

        return this;
    }
}
