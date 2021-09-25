package co.runed.merlin.classes;

import co.runed.merlin.core.SpellProviderType;
import co.runed.merlin.spells.SpellProvider;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import org.bukkit.inventory.ItemStack;

public class ClassImpl extends SpellProvider {
    private Disguise disguise = null;

    public ClassImpl(ClassDefinition definition) {
        super(definition);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (disguise != null && getOwner() != null) {
            disguise.setEntity(getOwner());
            disguise.startDisguise();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (disguise != null) {
            disguise.removeDisguise();
        }
    }

    @Override
    public SpellProviderType getType() {
        return SpellProviderType.CLASS;
    }

    @Override
    public ItemStack getIcon() {
        return super.getDefinition().getIcon();
    }

    public void setDisguise(Disguise disguise) {
        this.disguise = disguise;
    }

    public Disguise getDisguise() {
        return disguise;
    }
}
