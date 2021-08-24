package co.runed.merlin.concept.spells;

import co.runed.bolster.util.Manager;
import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.costs.ItemCost;
import co.runed.merlin.concept.items.runeblade.Runedash;
import co.runed.merlin.concept.triggers.Trigger;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.BiFunction;

public class Spells extends Manager {
    public static final SpellDefinition RUNEBLADE_RUNEDASH = new SpellDefinition("runeblade_runedash", Runedash::new)
            .options(SpellOption.ALERT_WHEN_READY)
            .priority(1000)
            .cost(new ItemCost(1));

    Map<String, SpellDefinition> spells = new HashMap<>();

    Map<UUID, Collection<Spell>> entitySpells = new HashMap<>();
    Map<UUID, Long> lastErrorTime = new HashMap<>();

    private static Spells _instance;

    public Spells(Plugin plugin) {
        super(plugin);

        _instance = this;
    }

    public SpellDefinition get(String id) {
        return spells.getOrDefault(id, null);
    }

    public boolean exists(String id) {
        return spells.containsKey(id);
    }

    public boolean register(String id, SpellDefinition spellDefinition) {
        if (spells.containsKey(id)) return false;

        spells.put(id, spellDefinition);

        return true;
    }

    public Collection<Spell> getSpells(LivingEntity entity) {
        // TODO(Jono): SORT BY SPELL PRIORITY

        return entitySpells.getOrDefault(entity.getUniqueId(), new HashSet<>());
    }

    public Collection<Spell> getSpells(LivingEntity entity, Class<? extends Trigger> triggerClass) {
        Collection<Spell> spells = new HashSet<>();

        for (var spell : getSpells(entity)) {
            if (!spell.getClass().isAssignableFrom(triggerClass)) continue;

            spells.add(spell);
        }

        return spells;
    }

    public <T extends Trigger> void run(LivingEntity entity, Class<T> trigger, BiFunction<T, CastContext, CastResult> func) {
        var spells = Spells.getInstance().getSpells(entity, trigger);

        for (Spell spell : spells) {
            run(entity, spell, func);
        }
    }

    public <T extends Trigger> CastResult run(LivingEntity entity, Spell spell, BiFunction<T, CastContext, CastResult> func) {
        var castContext = new CastContext(entity, spell);

        var result = spell.preCast(castContext);

        if (result.isSuccess()) {
            result = func.apply((T) spell, castContext);
        }

        if (!result.shouldContinue()) {
            var message = result.getMessage();

            if (message != null && System.currentTimeMillis() - this.lastErrorTime.getOrDefault(entity.getUniqueId(), 0L) >= 750) {
                entity.sendMessage(ChatColor.RED + message);
                this.lastErrorTime.put(entity.getUniqueId(), System.currentTimeMillis());
            }
        }

        if (result.isSuccess()) {
            result = spell.postCast(castContext);
        }

        return result;
    }

    //    public void run(LivingEntity entity, Class<? extends Trigger> trigger, BiFunction<Spell, CastContext, CastResult> func) {
//        var spells = Spells.getInstance().getSpells(entity, trigger);
//
//        for (var spell : spells) {
//            run(entity, spell, func);
//        }
//    }

//    public CastResult run(LivingEntity entity, Spell spell, BiFunction<Spell, CastContext, CastResult> func) {
//        var castContext = new CastContext(entity, spell);
//
//        var result = spell.preCast(castContext);
//
//        if (result.isSuccess()) {
//            result = func.apply(spell, castContext);
//        }
//
//        if (!result.shouldContinue()) {
//            var message = result.getMessage();
//
//            if (message != null && System.currentTimeMillis() - this.lastErrorTime.getOrDefault(entity.getUniqueId(), 0L) >= 750) {
//                entity.sendMessage(ChatColor.RED + message);
//                this.lastErrorTime.put(entity.getUniqueId(), System.currentTimeMillis());
//            }
//        }
//
//        if (result.isSuccess()) {
//            result = spell.postCast(castContext);
//        }
//
//        return result;
//    }

    public static Spells getInstance() {
        return _instance;
    }
}
