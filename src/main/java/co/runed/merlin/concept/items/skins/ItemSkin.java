package co.runed.merlin.concept.items.skins;

import co.runed.dayroom.util.Identifiable;
import co.runed.dayroom.util.Nameable;

public class ItemSkin implements Identifiable, Nameable {
    private final ItemSkinDefinition definition;

    public ItemSkin(ItemSkinDefinition definition) {
        this.definition = definition;
    }

    public ItemSkinDefinition getDefinition() {
        return definition;
    }

    @Override
    public String getId() {
        return getDefinition().getId();
    }

    @Override
    public String getName() {
        return getDefinition().getName();
    }
}
