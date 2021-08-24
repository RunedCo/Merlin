package co.runed.merlin.concept;

import co.runed.merlin.concept.items.ItemDefinition;
import co.runed.merlin.concept.items.ItemSpellProvider;
import co.runed.merlin.concept.spells.Spell;
import co.runed.merlin.concept.spells.SpellProvider;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class CastContext {
    Caster caster;
    Location castLocation;
    ItemDefinition item = null;
    SpellProvider provider;
    Spell spell;

    public CastContext(LivingEntity entity, Spell spell, ItemDefinition container) {
        this(entity, spell);

        this.item = container;
    }

    public CastContext(LivingEntity entity, Spell spell) {
        this.caster = Caster.from(entity);
        this.castLocation = entity.getLocation();

        this.spell = spell;
        this.provider = spell.getParent();
    }

    public Caster getCaster() {
        return caster;
    }

    public ItemDefinition getItem() {
        return item;
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
        return item.equals(this.item);
    }

    public boolean hasItem() {
        return this.spell.getParent() instanceof ItemSpellProvider;
    }
}
