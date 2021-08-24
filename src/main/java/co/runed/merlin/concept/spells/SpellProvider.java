package co.runed.merlin.concept.spells;

import co.runed.bolster.common.util.IDescribable;
import co.runed.bolster.common.util.IIdentifiable;
import co.runed.bolster.common.util.INameable;
import co.runed.bolster.util.config.IConfigurable;
import co.runed.merlin.concept.items.ItemRequirements;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpellProvider implements IIdentifiable, INameable, IDescribable, IConfigurable {
    List<SpellData> spells = new ArrayList<>();

    public void addSpell(SpellDefinition spell) {
        this.addSpell(spell, ItemRequirements.ALWAYS);
    }

    public void addSpell(SpellDefinition spell, ItemRequirements... options) {
        spells.add(new SpellData(spell, options));
    }

    @Override
    public void loadConfig(ConfigurationSection config) {

    }

    @Override
    public void create() {

    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    private static class SpellData {
        List<ItemRequirements> options = new ArrayList<>();
        SpellDefinition spell;

        public SpellData(SpellDefinition spell, ItemRequirements... options) {
            this.spell = spell;
            this.options.addAll(Arrays.stream(options).toList());
        }
    }
}
