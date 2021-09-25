package co.runed.merlin.spells;

import co.runed.bolster.util.Category;
import co.runed.bolster.util.registries.Definition;
import co.runed.bolster.util.registries.Registry;
import co.runed.dayroom.util.Describable;
import co.runed.merlin.costs.Cost;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class SpellDefinition extends Definition<Spell> implements Describable {
    private String description;

    private int charges = 1;
    private int priority = 0;
    private double cooldown = 0d;
    private double castTime = 0;
    private final List<Supplier<Cost>> costs = new ArrayList<>();
    private final Set<SpellOption> options = new HashSet<>();

    public SpellDefinition(String id, Function<SpellDefinition, Spell> function) {
        super(id, (def) -> function.apply((SpellDefinition) def));
    }

    public SpellDefinition setCharges(int charges) {
        this.charges = charges;

        return this;
    }

    public SpellDefinition setCooldown(double cooldown) {
        this.cooldown = cooldown;

        return this;
    }

    public SpellDefinition setPriority(int priority) {
        this.priority = priority;

        return this;
    }

    public int getPriority() {
        return priority;
    }

    public SpellDefinition addCost(Supplier<Cost> cost) {
        this.costs.add(cost);

        return this;
    }

    public List<Supplier<Cost>> getCosts() {
        return costs;
    }

    public SpellDefinition setCastTime(double castTime) {
        this.castTime = castTime;

        return this;
    }

    @Override
    public SpellDefinition from(Definition<Spell> parent) {
        super.from(parent);

        if (parent instanceof SpellDefinition spellDefinition) {
            for (var option : spellDefinition.options) {
                addOptions(option);
            }

            for (var cost : spellDefinition.costs) {
                addCost(cost);
            }

            setPriority(spellDefinition.priority);
            setCharges(spellDefinition.charges);
            setCooldown(spellDefinition.cooldown);
            setCastTime(spellDefinition.castTime);
        }

        return this;
    }

    @Override
    public SpellDefinition setName(String name) {
        return (SpellDefinition) super.setName(name);
    }

    @Override
    public SpellDefinition addCategories(Category... categories) {
        return (SpellDefinition) super.addCategories(categories);
    }

    public SpellDefinition addOptions(SpellOption... option) {
        this.options.addAll(Arrays.asList(option));

        return this;
    }

    public boolean hasOption(SpellOption option) {
        return this.options.contains(option);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        this.description = config.getString("description", description);

        this.charges = config.getInt("charges", charges);
        this.priority = config.getInt("priority", priority);
        this.cooldown = config.getDouble("cooldown", cooldown);
    }

    @Override
    protected Spell preCreate(Spell output) {
        output.setCooldown(cooldown);
        output.setCharges(charges);
        output.setPriority(priority);
        output.setCastTime(castTime);

        output.initialise();

        return super.preCreate(output);
    }

    @Override
    public Registry<Definition<Spell>> getRegistry() {
        return null;
    }
}
