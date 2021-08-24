package co.runed.merlin.concept.spells;

import co.runed.bolster.common.util.IDescribable;
import co.runed.bolster.common.util.IIdentifiable;
import co.runed.bolster.common.util.INameable;
import co.runed.bolster.util.config.IConfigurable;
import co.runed.merlin.concept.costs.Cost;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class SpellDefinition implements IIdentifiable, IDescribable, INameable, IConfigurable {
    private final String id;
    private String name;
    private String description;

    private int charges = 1;
    private int priority = 0;
    private final List<Cost> costs = new ArrayList<>();

    private final Supplier<Spell> consumer;

    public SpellDefinition(String id, Supplier<Spell> consumer) {
        this.id = id;
        this.consumer = consumer;
    }

    public SpellDefinition charges(int charges) {
        this.charges = charges;

        return this;
    }

    public SpellDefinition priority(int priority) {
        this.priority = priority;

        return this;
    }

    public int getPriority() {
        return priority;
    }

    public SpellDefinition cost(Cost... costs) {
        this.costs.addAll(Arrays.stream(costs).toList());

        return this;
    }

    public List<Cost> getCosts() {
        return costs;
    }

    public SpellDefinition options(SpellOption... options) {
        return this;
    }

    public boolean hasOption(SpellOption option) {
        return true;
    }

    public Spell create(ConfigurationSection config) {
        return this.consumer.get();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        if (name == null) return id;

        return name;
    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        this.name = config.getString("name", name);
        this.description = config.getString("description", description);
    }

    @Override
    public void create() {

    }
}
