package co.runed.merlin.core;

import co.runed.dayroom.util.Identifiable;

public class SpellProviderType implements Identifiable {
    public static final SpellProviderType CLASS = new SpellProviderType("class", true);
    public static final SpellProviderType ITEM = new SpellProviderType("item", false);

    String id;
    boolean solo;

    public SpellProviderType(String id, boolean solo) {
        this.id = id;
        this.solo = solo;
    }

    public boolean isSolo() {
        return solo;
    }

    @Override
    public String getId() {
        return id;
    }
}
