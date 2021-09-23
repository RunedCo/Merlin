package co.runed.merlin.concept.spells;

import co.runed.bolster.managers.Manager;
import co.runed.bolster.util.TimeUtil;
import co.runed.bolster.util.lang.Lang;
import co.runed.bolster.util.registries.Definition;
import co.runed.merlin.Merlin;
import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.definitions.SpellProviderDefinition;
import co.runed.merlin.concept.spells.type.RepeatingSpellType;
import co.runed.merlin.concept.triggers.Trigger;
import co.runed.merlin.concept.triggers.interact.PlayerInteractListener;
import co.runed.merlin.concept.triggers.inventory.PlayerInventoryListener;
import co.runed.merlin.concept.triggers.lifecycle.OnTriggerTrigger;
import co.runed.merlin.concept.triggers.lifecycle.TickTrigger;
import co.runed.merlin.concept.triggers.lifecycle.TriggerParams;
import co.runed.merlin.concept.triggers.movement.EntityMovementListener;
import co.runed.merlin.concept.triggers.projectile.EntityProjectileListener;
import co.runed.merlin.concept.util.task.Task;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.function.BiFunction;

public class SpellManager extends Manager {
    public static final Sound ALERT_READY_SOUND = Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1, 1.5F);

    private final Map<UUID, Collection<SpellProvider>> spellProviders = new HashMap<>();
    private final Map<UUID, Collection<PassiveSpellContainer>> passives = new HashMap<>();
    private final Map<UUID, Long> lastErrorTime = new HashMap<>();

    private static SpellManager _instance;

    public SpellManager(Plugin plugin) {
        super(plugin);

        Bukkit.getPluginManager().registerEvents(new PlayerInventoryListener(), Merlin.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), Merlin.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityProjectileListener(), Merlin.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityMovementListener(), Merlin.getInstance());

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
        var uuid = entity.getUniqueId();

        spellProviders.putIfAbsent(uuid, new HashSet<>());
        passives.putIfAbsent(uuid, new HashSet<>());

        var providers = spellProviders.get(uuid);

        // TODO check if provider is solo (e.g class) and disable others of same type if true

        var providerType = provider.getType();

        if (providerType.isSolo()) {
            providers.forEach(p -> {
                if (p.getType() == providerType) {
                    removeProvider(entity, p);
                }
            });
        }

        if (!providers.contains(provider)) {
            providers.add(provider);
        }

        for (var spell : provider.getSpells()) {
            if (spell.getType() instanceof RepeatingSpellType repeating && repeating.getFrequency() > 0L) {
                passives.putIfAbsent(uuid, new HashSet<>());

                var passive = new PassiveSpellContainer(spell, repeating.getFrequency());

                passive.start();

                passives.get(uuid).add(passive);
            }
        }

        provider.setOwner(entity);
        provider.setEnabled(true);
    }

    public void removeProvider(LivingEntity entity, SpellProvider provider) {
        var providers = spellProviders.getOrDefault(entity.getUniqueId(), new HashSet<>());

        if (!providers.contains(provider)) return;

        var entityPassives = passives.getOrDefault(entity.getUniqueId(), new HashSet<>());

        for (var passive : new HashSet<>(entityPassives)) {
            if (passive.spell.getParent() == provider) {
                entityPassives.remove(passive);
                passive.stop();
            }
        }

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

        removeProvider(entity, provider);

        provider.destroy();

        providers.remove(provider);
    }

    public Collection<SpellProvider> getProviders(LivingEntity entity) {
        return spellProviders.getOrDefault(entity.getUniqueId(), new HashSet<>()).stream().filter(SpellProvider::isEnabled).toList();
    }

    public SpellProvider getProvider(LivingEntity entity, Definition<?> definition) {
        var entityProviders = spellProviders.getOrDefault(entity.getUniqueId(), new HashSet<>());

        for (var provider : entityProviders) {
            if (provider.getDefinition() == definition) {

                provider.setOwner(entity);
                return provider;
            }
        }

        return null;
    }

    public Collection<Spell> getSpells(LivingEntity entity) {
        // TODO(Jono): SORT BY SPELL PRIORITY

        var spells = new ArrayList<Spell>();
        var spellProviders = this.spellProviders.getOrDefault(entity.getUniqueId(), new HashSet<>());

        for (var provider : spellProviders) {
            provider.setOwner(entity);

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

            castContext = run(spell, trigger, func);
        }
    }

    public <T extends Trigger> CastContext run(Spell spell, Class<T> trigger, BiFunction<T, CastContext, CastResult> func) {
        if (spell == null) return null;

        var entity = spell.getOwner();
        var castContext = new CastContext(entity, spell);

        try {
            if (trigger != OnTriggerTrigger.class) run(entity, OnTriggerTrigger.class, (spl, context) -> spl.onTrigger(context, new TriggerParams(trigger, castContext)));

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

                return castContext;
            }

            if (result.isSuccess()) {
                result = spell.postCast(castContext);

                if (spell.hasOption(SpellOption.ALERT_WHEN_READY) && spell.isOnCooldown()) {
                    var alertTask = new Task()
                            .delay(TimeUtil.toTicks(TimeUtil.fromSeconds(spell.getRemainingCooldown())))
                            .run(() -> {
                                entity.sendMessage(Lang.key("spell." + spell.getId() + ".msg.ready", "spell.msg.ready").with(spell).toComponent());
                                entity.playSound(ALERT_READY_SOUND);
                            });
                }
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

    private static class PassiveSpellContainer {
        BukkitTask task;
        Spell spell;
        long frequency;

        public PassiveSpellContainer(Spell spell, long frequency) {
            this.spell = spell;
            this.frequency = frequency;
        }

        public void start() {
            task = Bukkit.getScheduler().runTaskTimer(Merlin.getInstance(), this::run, 0L, frequency);
        }

        public void stop() {
            if (task == null) return;

            task.cancel();
        }

        public void run() {
            SpellManager.getInstance().run(spell, TickTrigger.class, TickTrigger::onTick);
        }
    }
}
