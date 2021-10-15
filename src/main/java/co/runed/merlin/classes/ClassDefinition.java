package co.runed.merlin.classes;

import co.runed.bolster.game.traits.Trait;
import co.runed.bolster.util.Category;
import co.runed.bolster.util.registries.Definition;
import co.runed.bolster.util.registries.Registry;
import co.runed.merlin.core.MerlinRegistries;
import co.runed.merlin.spells.SpellDefinition;
import co.runed.merlin.spells.SpellProviderDefinition;
import co.runed.merlin.spells.type.SpellType;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;
import java.util.function.Supplier;

public class ClassDefinition extends SpellProviderDefinition<ClassImpl> {
    private Supplier<Disguise> disguiseFunction = null;

    public ClassDefinition(String id) {
        this(id, ClassImpl::new);

        this.setIcon(new ItemStack(Material.PLAYER_HEAD));
    }

    public ClassDefinition(String id, Function<ClassDefinition, ClassImpl> supplier) {
        super(id, (def) -> supplier.apply((ClassDefinition) def));
    }

    public ClassDefinition setDisguise(Supplier<Disguise> disguiseFunction) {
        this.disguiseFunction = disguiseFunction;

        return this;
    }

    @Override
    public ClassDefinition addCategories(Category... categories) {
        return (ClassDefinition) super.addCategories(categories);
    }

    @Override
    public ClassDefinition addSpell(SpellDefinition spell) {
        return (ClassDefinition) super.addSpell(spell);
    }

    @Override
    public ClassDefinition addSpell(SpellDefinition spell, SpellType spellType) {
        return (ClassDefinition) super.addSpell(spell, spellType);
    }

    @Override
    public <J> ClassDefinition setTrait(Trait<J> key, J value) {
        return (ClassDefinition) super.setTrait(key, value);
    }

    @Override
    public ClassDefinition addLangReplacement(String key, String value) {
        return (ClassDefinition) super.addLangReplacement(key, value);
    }

    @Override
    public ClassDefinition setIcon(ItemStack icon) {
        return (ClassDefinition) super.setIcon(icon);
    }

    @Override
    public ClassDefinition setName(String name) {
        return (ClassDefinition) super.setName(name);
    }

    @Override
    public ClassDefinition from(Definition<ClassImpl> parent) {
        var from = (ClassDefinition) super.from(parent);

        setDisguise(from.disguiseFunction);

        return from;
    }

    @Override
    public ClassImpl create() {
        var impl = super.create();

        if (disguiseFunction != null) impl.setDisguise(disguiseFunction.get());

        return impl;
    }

    @Override
    public ClassDefinition register() {
        return (ClassDefinition) super.register();
    }

    @Override
    public Registry<Definition<ClassImpl>> getRegistry() {
        return MerlinRegistries.CLASSES;
    }
}
