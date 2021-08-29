package co.runed.merlin.concept.classes;

import co.runed.merlin.concept.spells.SpellProvider;
import co.runed.merlin.core.SpellProviderType;

public class ClassImpl extends SpellProvider {
    public ClassImpl(ClassDefinition definition) {
        super(definition);
    }

    @Override
    public SpellProviderType getType() {
        return SpellProviderType.CLASS;
    }
}
