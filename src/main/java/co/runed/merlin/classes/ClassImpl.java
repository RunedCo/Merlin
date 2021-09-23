package co.runed.merlin.classes;

import co.runed.merlin.core.SpellProviderType;
import co.runed.merlin.spells.SpellProvider;

public class ClassImpl extends SpellProvider {
    public ClassImpl(ClassDefinition definition) {
        super(definition);
    }

    @Override
    public SpellProviderType getType() {
        return SpellProviderType.CLASS;
    }
}
