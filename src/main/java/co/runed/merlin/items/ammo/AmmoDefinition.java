package co.runed.merlin.items.ammo;

import co.runed.bolster.util.registries.Definition;
import co.runed.merlin.items.ItemDefinition;
import co.runed.merlin.items.ItemImpl;
import co.runed.merlin.items.ItemRequirement;
import co.runed.merlin.spells.SpellDefinition;
import co.runed.merlin.spells.SpellOption;

import java.util.function.Function;

public class AmmoDefinition extends ItemDefinition {
    private final SpellDefinition BASE_AMMO_SPELL = new SpellDefinition("update_ammo_display", BaseAmmoSpell::new)
            .addOptions(SpellOption.HIDE_ERROR_MESSAGES, SpellOption.IGNORE_CANCELLED);

    public AmmoDefinition(String id) {
        this(id, AmmoImpl::new);
    }

    public AmmoDefinition(String id, Function<AmmoDefinition, AmmoImpl> supplier) {
        super(id, (definition -> supplier.apply((AmmoDefinition) definition)));

        this.addSpell(BASE_AMMO_SPELL, ItemRequirement.ALWAYS);
    }

    @Override
    public AmmoDefinition setName(String name) {
        return (AmmoDefinition) super.setName(name);
    }

    @Override
    public AmmoDefinition from(Definition<ItemImpl> parent) {
        return (AmmoDefinition) super.from(parent);
    }

    @Override
    public AmmoDefinition register() {
        return (AmmoDefinition) super.register();
    }
}
