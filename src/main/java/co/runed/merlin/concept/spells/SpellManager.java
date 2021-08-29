package co.runed.merlin.concept.spells;

import co.runed.bolster.util.Manager;
import co.runed.bolster.util.registries.Definition;
import co.runed.merlin.Merlin;
import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.definitions.SpellProviderDefinition;
import co.runed.merlin.concept.spells.runeblade.Runedash;
import co.runed.merlin.concept.triggers.Trigger;
import co.runed.merlin.concept.triggers.interact.PlayerInteractListener;
import co.runed.merlin.concept.triggers.inventory.PlayerInventoryListener;
import co.runed.merlin.concept.triggers.inventory.PlayerSelectListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.BiFunction;

public class SpellManager extends Manager {
    public static final SpellDefinition RUNEDASH = new SpellDefinition("runedash", Runedash::new)
            .setName("Runedash")
            .options(SpellOption.ALERT_WHEN_READY)
            .cooldown(10)
            .priority(1000);

    private final Map<UUID, Collection<SpellProvider>> spellProviders = new HashMap<>();
    private final Map<UUID, Collection<Spell>> spells = new HashMap<>();
    private final Map<UUID, Long> lastErrorTime = new HashMap<>();

    private static SpellManager _instance;

    public SpellManager(Plugin plugin) {
        super(plugin);

        Bukkit.getPluginManager().registerEvents(new PlayerInventoryListener(), Merlin.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), Merlin.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerSelectListener(), Merlin.getInstance());


        _instance = this;
    }

    public SpellProvider getOrCreateProvider(LivingEntity entity, SpellProviderDefinition<?> definition) {
        var provider = SpellManager.getInstance().getProvider(entity, definition);

        if (provider != null) return provider;

        provider = definition.create();

        SpellManager.getInstance().addProvider(entity, provider);

        return provider;
    }

    public void addProvider(LivingEntity entity, SpellProvider provider) {
        spellProviders.putIfAbsent(entity.getUniqueId(), new ArrayList<>());

        var providers = spellProviders.get(entity.getUniqueId());

        // TODO check if provider is solo (e.g class) and disable others of same type if true

        var providerType = provider.getType();

        if (providerType.isSolo()) {
            providers.forEach(p -> {
                if (p.getType() == providerType) {
                    removeProvider(entity, p);
                }
            });
        }

        providers.add(provider);

        provider.setOwner(entity);
        provider.setEnabled(true);
    }

    public void removeProvider(LivingEntity entity, SpellProvider provider) {
        var providers = spellProviders.getOrDefault(entity.getUniqueId(), new ArrayList<>());

        if (!providers.contains(provider)) return;

        provider.setEnabled(false);
    }

    public void removeProvider(LivingEntity entity, Definition<?> definition) {
        for (var prov : new ArrayList<>(getProviders(entity))) {
            if (prov.getDefinition() == definition) {
                removeProvider(entity, prov);
            }
        }
    }

    public void destroyProvider(LivingEntity entity, SpellProvider provider) {
        var providers = spellProviders.getOrDefault(entity.getUniqueId(), new ArrayList<>());

        if (!providers.contains(provider)) return;

        provider.setEnabled(false);
        provider.destroy();

        providers.remove(provider);
    }

    public Collection<SpellProvider> getProviders(LivingEntity entity) {
        return spellProviders.getOrDefault(entity.getUniqueId(), new HashSet<>()).stream().filter(SpellProvider::isEnabled).toList();
    }

    public SpellProvider getProvider(LivingEntity entity, Definition<?> definition) {
        var entityProviders = spellProviders.getOrDefault(entity.getUniqueId(), new HashSet<>());

        for (var provider : entityProviders) {
            if (provider.getDefinition() == definition) return provider;
        }

        return null;
    }

    public Collection<Spell> getSpells(LivingEntity entity) {
        // TODO(Jono): SORT BY SPELL PRIORITY

        var spells = new ArrayList<Spell>();
        var spellProviders = this.spellProviders.getOrDefault(entity.getUniqueId(), new HashSet<>());

        for (var provider : spellProviders) {
            spells.addAll(provider.getSpells());
        }

        spells.sort((s1, s2) -> s2.getPriority() - s1.getPriority());

        return spells;
    }

    public Collection<Spell> getSpells(LivingEntity entity, Class<? extends Trigger> triggerClass) {
        var spells = new HashSet<Spell>();

        for (var spell : getSpells(entity)) {
            var spellClass = spell.getClass();

            if (!triggerClass.isAssignableFrom(spellClass)) continue;

            spells.add(spell);
        }

        return spells;
    }

    public <T extends Trigger> void run(LivingEntity entity, Class<T> trigger, BiFunction<T, CastContext, CastResult> func) {
        var spells = SpellManager.getInstance().getSpells(entity, trigger);

        CastContext castContext = null;
        for (var spell : spells) {
            if (castContext != null && castContext.isCancelled() && !spell.hasOption(SpellOption.IGNORE_CANCELLED)) continue;

            castContext = run(entity, spell, func);
        }
    }

    public <T extends Trigger> CastContext run(LivingEntity entity, Spell spell, BiFunction<T, CastContext, CastResult> func) {
        var castContext = new CastContext(entity, spell);

        try {
            var result = spell.preCast(castContext);

            if (result.isSuccess()) {
                result = func.apply((T) spell, castContext);
            }

            if (!result.shouldContinue()) {
                var message = result.getMessage();
                var lastErrorTime = this.lastErrorTime.getOrDefault(entity.getUniqueId(), 0L);

                if (message != null && !spell.hasOption(SpellOption.HIDE_ERROR_MESSAGES) && System.currentTimeMillis() - lastErrorTime >= 750) {
                    entity.sendMessage(ChatColor.RED + message);

                    this.lastErrorTime.put(entity.getUniqueId(), System.currentTimeMillis());
                }
            }

            if (result.isSuccess()) {
                result = spell.postCast(castContext);
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }

        return castContext;
    }

    public static SpellManager getInstance() {
        return _instance;
    }
}
