package co.runed.merlin.concept.classes;

import co.runed.bolster.game.traits.Trait;
import co.runed.bolster.util.Category;
import co.runed.bolster.util.registries.Definition;
import co.runed.bolster.util.registries.Registry;
import co.runed.merlin.concept.MerlinRegistries;
import co.runed.merlin.concept.definitions.SpellProviderDefinition;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.spells.type.SpellType;

import java.util.function.Function;

public class ClassDefinition extends SpellProviderDefinition<ClassImpl> {
    public ClassDefinition(String id) {
        this(id, ClassImpl::new);
    }

    public ClassDefinition(String id, Function<ClassDefinition, ClassImpl> supplier) {
        super(id, (def) -> supplier.apply((ClassDefinition) def));
    }

    @Override
    public ClassDefinition category(Category... categories) {
        return (ClassDefinition) super.category(categories);
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
    public ClassDefinition from(Definition<ClassImpl> parent) {
        return (ClassDefinition) super.from(parent);
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
