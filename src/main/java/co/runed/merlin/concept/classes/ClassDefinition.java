package co.runed.merlin.concept.classes;

import co.runed.bolster.util.Category;
import co.runed.bolster.util.registries.Definition;
import co.runed.bolster.util.registries.Registry;
import co.runed.merlin.concept.MerlinRegistries;
import co.runed.merlin.concept.definitions.SpellProviderDefinition;

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
    public ClassDefinition register() {
        return (ClassDefinition) super.register();
    }

    @Override
    public Registry<Definition<ClassImpl>> getRegistry() {
        return MerlinRegistries.CLASSES;
    }
}
