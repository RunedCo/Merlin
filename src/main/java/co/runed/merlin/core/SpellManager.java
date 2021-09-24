package co.runed.merlin.core;

import co.runed.bolster.events.entity.EntitySetCooldownEvent;
import co.runed.bolster.managers.Manager;
import co.runed.bolster.util.lang.Lang;
import co.runed.bolster.util.registries.Definition;
import co.runed.dayroom.util.ReflectionUtil;
import co.runed.merlin.Merlin;
import co.runed.merlin.spells.*;
import co.runed.merlin.spells.type.RepeatingSpellType;
import co.runed.merlin.triggers.SpellTrigger;
import co.runed.merlin.triggers.Trigger;
import co.runed.merlin.triggers.interact.PlayerInteractListener;
import co.runed.merlin.triggers.inventory.PlayerInventoryListener;
import co.runed.merlin.triggers.lifecycle.OnTriggerTrigger;
import co.runed.merlin.triggers.lifecycle.TickTrigger;
import co.runed.merlin.triggers.movement.EntityMovementListener;
import co.runed.merlin.triggers.projectile.EntityProjectileListener;
import co.runed.merlin.util.task.RepeatingTask;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.*;

public class SpellManager extends Manager {
    public static final Sound ALERT_READY_SOUND = Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1, 1.5F);

    private final Map<UUID, Collection<SpellProvider>> spellProviders = new HashMap<>();
    private final Map<UUID, Collection<PassiveSpellContainer>> passives = new HashMap<>();
    private final Collection<Spell> alertingSpells = new HashSet<>();
    private final Map<UUID, Long> lastErrorTime = new HashMap<>();

    private final RepeatingTask alertTask = new RepeatingTask(5L);

    private Map<Class<? extends Spell>, Map<Class<? extends Trigger>, List<Method>>> spellMethods = new HashMap<>();

    private static SpellManager _instance;

    public SpellManager(Plugin plugin) {
        super(plugin);

        Bukkit.getPluginManager().registerEvents(new PlayerInventoryListener(), Merlin.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), Merlin.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityProjectileListener(), Merlin.getInstance());
        Bukkit.getPluginManager().registerEvents(new EntityMovementListener(), Merlin.getInstance());

        alertTask.run(this::doAlertTask);

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
            var spellClass = spell.getClass();

            parseMethods(spellClass);

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

    private void parseMethods(Class<? extends Spell> spellClass) {
        if (!spellMethods.containsKey(spellClass)) {
            spellMethods.put(spellClass, new HashMap<>());

            var methodMap = spellMethods.get(spellClass);

            for (var method : ReflectionUtil.getMethodsAnnotatedWith(spellClass, SpellTrigger.class)) {
                var paramTypes = method.getParameterTypes();

                if (paramTypes.length != 1 || method.getReturnType() != CastResult.class) break;

                var param = paramTypes[0];

                if (!Trigger.class.isAssignableFrom(param)) break;

                methodMap.putIfAbsent((Class<? extends Trigger>) param, new ArrayList<>());

                var methodList = methodMap.get(param);

                methodList.add(method);
            }
        }
    }

    private List<Method> getMethods(Class<? extends Spell> spellClass, Class<? extends Trigger> triggerClass) {
        return spellMethods.getOrDefault(spellClass, new HashMap<>())
                .getOrDefault(triggerClass, new ArrayList<>());
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

        if (entity == null) return new HashSet<>();

        var spells = new ArrayList<Spell>();
        var spellProviders = this.spellProviders.getOrDefault(entity.getUniqueId(), new HashSet<>());

        for (var provider : spellProviders) {
            provider.setOwner(entity);

            spells.addAll(provider.getSpells());
        }

        spells.sort((s1, s2) -> s2.getPriority() - s1.getPriority());

        return spells;
    }

    private boolean hasTrigger(Spell spell, Trigger trigger) {
        return spellMethods.getOrDefault(spell.getClass(), new HashMap<>()).containsKey(trigger.getClass());
    }

    public Collection<Spell> getSpells(LivingEntity entity, Trigger trigger) {
        var spells = new HashSet<Spell>();

        for (var spell : getSpells(entity)) {
            var spellClass = spell.getClass();

            if (!hasTrigger(spell, trigger)) continue;

            spells.add(spell);
        }

        return spells;
    }

    public void run(LivingEntity entity, Trigger trigger) {
        var spells = SpellManager.getInstance().getSpells(entity, trigger);

        for (var spell : spells) {
            if (trigger.isCancelled() && !spell.hasOption(SpellOption.IGNORE_CANCELLED)) continue;

            for (var method : getMethods(spell.getClass(), trigger.getClass())) {
                run(spell, trigger, method);
            }

        }
    }

    public CastResult run(Spell spell, Trigger trigger, Method method) {
        if (spell == null) return null;

        var entity = spell.getOwner();
        var castContext = new CastContext(entity, spell);
        trigger.setContext(castContext);

        try {
            if (!(trigger instanceof OnTriggerTrigger)) run(entity, new OnTriggerTrigger(trigger, method));

            var result = spell.preCast(trigger);

            if (result.isSuccess()) {
                result = (CastResult) method.invoke(spell, trigger);
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
                result = spell.postCast(trigger);

                scheduleReadyAlert(spell, entity);
            }

            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return CastResult.fail();
    }

    private void scheduleReadyAlert(Spell spell, LivingEntity entity) {
        if (spell.hasOption(SpellOption.ALERT_WHEN_READY) && spell.isOnCooldown()) {
            alertingSpells.add(spell);
        }
    }

    private void doAlertTask() {
        for (var spell : new HashSet<>(alertingSpells)) {
            var owner = spell.getOwner();

            if (!spell.isOnCooldown() && owner != null) {
                owner.sendMessage(Lang.key("spell." + spell.getId() + ".msg.ready", "spell.msg.ready").with(spell).toComponent());
                owner.playSound(ALERT_READY_SOUND);

                alertingSpells.remove(spell);
            }
        }
    }

    @EventHandler
    private void onCooldownChange(EntitySetCooldownEvent event) {
        var cooldown = event.getCooldownId();
        var cooldownLength = event.getCooldown();
        var entity = event.getEntity();

        if (!(event instanceof Player player)) return;
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
            SpellManager.getInstance().run(spell.getOwner(), new TickTrigger());
        }
    }
}
