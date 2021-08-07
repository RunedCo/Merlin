package co.runed.merlin.core;

import co.runed.bolster.Bolster;
import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.game.traits.TraitProvider;
import co.runed.bolster.game.traits.Traits;
import co.runed.bolster.util.ComponentUtil;
import co.runed.bolster.util.IDescribable;
import co.runed.bolster.util.IIdentifiable;
import co.runed.bolster.util.StringUtil;
import co.runed.bolster.util.config.ConfigUtil;
import co.runed.bolster.util.config.IConfigurable;
import co.runed.bolster.util.properties.Properties;
import co.runed.bolster.util.registries.DefinitionRegistry;
import co.runed.bolster.util.registries.Registry;
import co.runed.merlin.abilities.Ability;
import co.runed.merlin.abilities.AbilityTrigger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbilityProvider extends TraitProvider implements IConfigurable, IIdentifiable, IDescribable
{
    String id = null;
    String name = "";
    String description;

    List<AbilityData> abilities = new ArrayList<>();

    ConfigurationSection config;
    boolean dirty = false;

    LivingEntity entity;
    LivingEntity parent;

    public AbilityProvider()
    {
        String id = null;

        for (Registry registry : getType().getRegistries())
        {
            try
            {
                id = registry.getId(this);
            }
            catch (Exception ignored)
            {
                DefinitionRegistry definitionRegistry = (DefinitionRegistry) registry;

                try
                {
                    id = definitionRegistry.getIdFromValue(this);
                }
                catch (Exception ignored2)
                {

                }
            }

            if (id != null) break;
        }

        this.id = id;
    }

    @Override
    public String getId()
    {
        return id;
    }

    public abstract AbilityProviderType getType();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getDescription()
    {
        List<String> abilityDescriptions = new ArrayList<>();

        for (AbilityProvider.AbilityData abilityData : this.getAbilities())
        {
            Ability ability = abilityData.ability;

            if (!ability.isEnabled()) continue;
            if (ability.getDescription() == null) continue;
            if (ability.getTriggers().size() <= 0) continue;

            String abilityName = ChatColor.RED + abilityData.triggers.get(0).getDisplayName() + (ability.getName() != null ? " - " + ability.getName() : "");

            String abilityDesc = abilityName + ": " + ChatColor.YELLOW + ability.getDescription();

            if (ability.getCooldown() > 0)
                abilityDesc += ChatColor.DARK_GRAY + " (" + ability.getCooldown() + "s cooldown)";
            if (ability.getCharges() > 1)
                abilityDesc += ChatColor.BLUE + " (" + ability.getCharges() + " charges";
            if (ability.getCastTime() > 0)
                abilityDesc += ChatColor.BLUE + " (" + ability.getCastTime() + "s cast time)";

            abilityDescriptions.add(abilityDesc + ChatColor.RESET);
        }

        if (abilityDescriptions.size() <= 0) return null;

        return StringUtil.join(StringUtil.NEW_LINE, abilityDescriptions);
    }

    public Collection<Component> getLore()
    {
        return ComponentUtil.wrappedText(this.getDescription());
    }

    public void onEnable()
    {
        if (Bolster.getBolsterConfig().debugMode)
            Bolster.getInstance().getLogger().info(ChatColor.stripColor(this.getEntity().getName()) + " (" + this.getEntity().getUniqueId() + ")" + ": " + this.getId() + " enabled");

        double health = this.getTrait(Traits.MAX_HEALTH);

        this.getEntity().setMaxHealth(this.getEntity().getMaxHealth() + health);
        this.getEntity().setHealth(this.getEntity().getHealth() + health);
    }

    public void onDisable()
    {
        if (Bolster.getBolsterConfig().debugMode)
            Bolster.getInstance().getLogger().info(ChatColor.stripColor(this.getEntity().getName()) + " (" + this.getEntity().getUniqueId() + ")" + ": " + this.getId() + " disabled");

        double health = this.getEntity().getMaxHealth() - this.getTrait(Traits.MAX_HEALTH);

        if (health <= 0) health = 20;

        this.getEntity().setMaxHealth(health);
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        if (this.isEnabled() != enabled && this.getEntity() != null)
        {
            if (!enabled)
            {
                this.onDisable();
                AbilityTrigger.DISABLE.trigger(this.getEntity(), this, new Properties());
            }

            if (enabled)
            {
                this.onEnable();
                AbilityTrigger.ENABLE.trigger(this.getEntity(), this, new Properties());
            }
        }

        super.setEnabled(enabled);
    }

    @Override
    public void setConfig(ConfigurationSection config)
    {
        this.config = ConfigUtil.cloneSection(config);
    }

    @Override
    public ConfigurationSection getConfig()
    {
        return this.config;
    }

    @Override
    public boolean isConfigSet()
    {
        return this.config != null;
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {
        ConfigUtil.parseVariables(config);

        this.setName(ChatColor.translateAlternateColorCodes('&', config.getString("name", "")));
    }

    public LivingEntity getEntity()
    {
        return this.entity;
    }

    public void setEntity(LivingEntity entity)
    {
        this.setEntity(entity, true);
    }

    public void setEntity(LivingEntity entity, boolean trigger)
    {
        boolean firstTime = this.getEntity() == null || (entity != null && !this.getEntity().getUniqueId().equals(entity.getUniqueId()));

        if (entity != null && entity.equals(this.getEntity())) return;

        this.entity = entity;

        this.setDirty();

        if (firstTime && trigger)
        {
            this.rebuild();

            // FIXME move to better place
            this.onEnable();

            AbilityManager.getInstance().trigger(this.getEntity(), this, AbilityTrigger.CREATE, new Properties());
        }
    }

    public LivingEntity getParent()
    {
        return parent;
    }

    public void setParent(LivingEntity parent)
    {
        this.parent = parent;

        this.setDirty();
    }

    public boolean isDirty()
    {
        return this.dirty;
    }

    public void setDirty()
    {
        this.dirty = true;
    }

    public Ability addAbility(Ability ability)
    {
        // TODO: loop through spell triggers and add
        // TODO: check for id and triggers

        if (ability == null) return null;

        ability.setAbilityProvider(this);
        if (this.getEntity() != null) ability.setCaster(this.getEntity());

        if (ability.getId() == null)
        {
            Bolster.getInstance().getLogger().severe("Error adding ability of type " + ability.getClass() + " to provider " + this.getId() + "! Reason: Missing Id");

            return ability;
        }

        this.abilities.add(new AbilityData(ability.getTriggers(), ability));

        return ability;
    }

    public List<AbilityData> getAbilities()
    {
        return abilities;
    }

    public abstract void onCastAbility(Ability ability, boolean success);

    public abstract void onToggleCooldown(Ability ability);

    public boolean rebuild()
    {
        if (!this.isDirty()) return false;

        this.dirty = false;

        this.destroy(false);
        this.loadConfig(this.config);
        this.create();

        if (this.getEntity() != null)
        {
            BolsterEntity.from(entity).addTraitProvider(this);

            for (var abilityData : this.abilities)
            {
                var ability = abilityData.ability;

                if (ability.getCaster() == entity) continue;

                ability.setCaster(entity);
                // TODO
                // AbilityManager.getInstance().add(entity, abilityData.trigger, ability);
            }
        }

        return true;
    }

    public void destroy()
    {
        this.destroy(true);
    }

    // FIXME this whole destroying every time the entity is set is a pain in the ass and needs work
    public void destroy(boolean trigger)
    {
        if (this.getEntity() != null)
        {
            if (trigger)
            {
                this.onDisable();

                AbilityManager.getInstance().trigger(this.getEntity(), this, AbilityTrigger.DESTROY, new Properties());
            }

            BolsterEntity.from(this.getEntity()).removeTraitProvider(this);
        }

        var abilityList = new ArrayList<>(this.abilities);
        for (var abilityData : abilityList)
        {
            abilityData.destroy();
        }

        this.abilities.clear();
    }

    public static class AbilityData
    {
        public List<AbilityTrigger> triggers = new ArrayList<>();
        public Ability ability;
        public BukkitTask task = null;

        public AbilityData(Collection<AbilityTrigger> triggers, Ability ability)
        {
            this.ability = ability;
            this.triggers.addAll(triggers);

            if (this.triggers.contains(AbilityTrigger.TICK))
            {
                this.task = Bukkit.getServer().getScheduler().runTaskTimer(Bolster.getInstance(), this::run, 0L, 1L);
            }
        }

        protected void run()
        {
            if (ability.getCaster() == null) return;
            if (ability.isOnCooldown()) return;

            Properties properties = new Properties();

            AbilityManager.getInstance().trigger(ability.getCaster(), null, AbilityTrigger.TICK, properties);
        }

        public void destroy()
        {
            ability.destroy();

            if (this.task != null)
            {
                this.task.cancel();
            }
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof AbilityData data)
            {
                return data.ability.equals(this.ability);
            }

            return super.equals(obj);
        }
    }
}
