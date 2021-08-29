package co.runed.merlin.core;

public class SpellProviderType {
    public static final SpellProviderType CLASS = new SpellProviderType("class", true);
    public static final SpellProviderType ITEM = new SpellProviderType("item", false);
    public static final SpellProviderType UPGRADE = new SpellProviderType("upgrade", false);

    String id;
    boolean solo;

    public SpellProviderType(String id, boolean solo) {
        this.id = id;
        this.solo = solo;
    }

    public boolean isSolo() {
        return solo;
    }
}
