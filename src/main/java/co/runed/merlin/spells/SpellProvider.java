package co.runed.merlin.spells;

import co.runed.bolster.Bolster;
import co.runed.bolster.damage.DamageSource;
import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.game.traits.TraitProvider;
import co.runed.bolster.game.traits.Traits;
import co.runed.bolster.managers.PlayerManager;
import co.runed.bolster.util.IconPreview;
import co.runed.bolster.util.Owned;
import co.runed.bolster.util.config.ConfigEntry;
import co.runed.bolster.util.config.ConfigUtil;
import co.runed.bolster.util.config.Configurable;
import co.runed.bolster.util.lang.Lang;
import co.runed.bolster.util.lang.LangProvider;
import co.runed.bolster.util.registries.Registries;
import co.runed.dayroom.properties.Properties;
import co.runed.dayroom.properties.Property;
import co.runed.dayroom.util.Describable;
import co.runed.dayroom.util.Enableable;
import co.runed.dayroom.util.Identifiable;
import co.runed.dayroom.util.Nameable;
import co.runed.merlin.core.SpellProviderType;
import co.runed.merlin.triggers.Trigger;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.time.ZonedDateTime;
import java.util.*;

public abstract class SpellProvider extends TraitProvider implements Identifiable, Nameable, Describable, Configurable, Owned, Enableable, DamageSource, IconPreview, LangProvider {
    public static final String CONFIG_KEY_HEALTH = "health";

    @ConfigEntry(key = "enabled")
    private boolean enabled = false;
    @ConfigEntry(key = "name")
    private String name;
    @ConfigEntry(key = "description")
    private String description;

    private final SpellProviderDefinition<?> definition;
    private LivingEntity entity;
    private final List<Spell> spells = new ArrayList<>();
    private int level = -1;
    private int temporaryLevel = -1;
    private AttributeModifier maxHealthModifier;
    private Map<String, String> langSource = new HashMap<>();
    private final Map<String, String> langReplacements = new HashMap<>();

    private final Properties properties = new Properties();

    public SpellProvider(SpellProviderDefinition<?> definition) {
        this.definition = definition;
    }

    public SpellProviderDefinition<?> getDefinition() {
        return definition;
    }

    public abstract SpellProviderType getType();

    @Override
    public String getId() {
        return getDefinition().getId();
    }

    @Override
    public String getName() {
        return new Lang(getType().getId() + "." + getId() + ".name").withDefault(name).with(this).toLegacyString();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /* Lifecycle */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        Bolster.debug("Provider " + getId() + " enabled=" + enabled + " prev-enabled=" + this.enabled + " owner=" + getOwner());

        if (this.enabled != enabled && getOwner() != null) {
            // On Disable
            if (!enabled) {
                onDisable();
            }

            // On Enable
            if (enabled) {
                onEnable();
            }
        }

        this.enabled = enabled;
    }

    public void onEnable() {
        var attribute = getOwner().getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (attribute != null) {
            if (maxHealthModifier != null) {
                attribute.removeModifier(maxHealthModifier);
            }

            maxHealthModifier = new AttributeModifier(UUID.randomUUID(), getId() + "_max_health", getTrait(Traits.MAX_HEALTH), AttributeModifier.Operation.ADD_NUMBER);

            attribute.addModifier(maxHealthModifier);

            getOwner().setHealth(attribute.getValue());
        }
    }

    public void onDisable() {
        var attribute = getOwner().getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (attribute != null) {
            if (maxHealthModifier != null) {
                attribute.removeModifier(maxHealthModifier);
            }

            maxHealthModifier = null;
        }
    }

    /* Config */
    @Override
    public void loadConfig(ConfigurationSection config) {
        ConfigUtil.loadAnnotatedConfig(config, this);
        ConfigUtil.parseVariables(config);

        var langConfig = ConfigUtil.create();

        if (config.isConfigurationSection("lang")) {
            langConfig = config.getConfigurationSection("lang");

            langSource = ConfigUtil.toStringMap(langConfig, true);
        }

        if (config.isConfigurationSection("traits")) {
            var traitSection = config.getConfigurationSection("traits");

            ConfigUtil.loadProperties(Registries.TRAITS, traitSection, getTraits());
        }

        if (config.isConfigurationSection("spells")) {
            var spellsConfig = config.getConfigurationSection("spells");

            for (var spell : spells) {
                if (spellsConfig.isConfigurationSection(spell.getId())) {
                    var spellConfig = spellsConfig.getConfigurationSection(spell.getId());

                    spellConfig.set("lang", langConfig);

                    spell.setConfig(spellConfig);
                    spell.loadConfig(spellConfig);
                }
            }
        }
    }


    /* Spells */
    public void addSpell(Spell spell) {
        spell.setParent(this);
        spells.add(spell);
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public ZonedDateTime getLastCastTime(SpellDefinition spell) {
        for (var spellInstance : getSpells()) {
            if (spellInstance.getDefinition().equals(spell)) return spellInstance.getLastCastTimestamp();
        }

        return null;
    }

    public CastResult preCast(Trigger trigger) {
        if (!isEnabled()) return CastResult.fail();

        return CastResult.success();
    }

    public void postCast(Trigger trigger) {

    }

    /* Language */
    @Override
    public Map<String, String> getLangReplacements() {
        return langReplacements;
    }

    public void setLangReplacement(String key, String value) {
        getLangReplacements().put(key, value);
    }

    @Override
    public Map<String, String> getLangSource() {
        return langSource;
    }

    /* Ownership */
    @Override
    public LivingEntity getOwner() {
        return entity;
    }

    public void setOwner(LivingEntity entity) {
        if (entity != this.entity && this.entity != null) {
            BolsterEntity.from(this.entity).removeTraitProvider(this);
        }

        this.entity = entity;

        for (var spell : spells) {
            spell.setOwner(entity);
        }

        if (entity == null) return;

        BolsterEntity.from(entity).addTraitProvider(this);

        if (entity instanceof Player player) {
            var level = PlayerManager.getInstance().getPlayerData(player).getProviderLevel(getId());

            if (temporaryLevel > -1) level = temporaryLevel;

            if (level != getLevel()) setLevel(level, temporaryLevel > -1);
        }
    }

    /* Levelling */
    public void setLevel(int level) {
        this.setLevel(level, false);
    }

    public void setLevel(int level, boolean isTemporary) {
        var definition = getDefinition();
        var levels = definition.getMergedLevels();
        var milestones = definition.getMilestones();

        level = Math.max(0, Math.min(level, definition.getMaxLevel()));

        this.level = level;

        if (isTemporary) this.temporaryLevel = level;

        for (int milestoneLevel : milestones.keySet()) {
            if (this.level > milestoneLevel) {
                var milestone = milestones.get(milestoneLevel);
                var originalClone = ConfigUtil.cloneSection(milestone.getOriginalConfig());
                var levelConfig = levels.get(level);
                var config = ConfigUtil.parseVariables(originalClone, levelConfig);

                milestone.setConfig(config);
            }
        }

        var levelConfig = levels.get(level);

        if (levelConfig != null) loadConfig(levelConfig);
    }

    public void setTemporaryLevel(int level) {
        setLevel(level, true);
    }

    public int getLevel() {
        return temporaryLevel > -1 ? temporaryLevel : level;
    }

    /* Damage Source */
    @Override
    public DamageSource next() {
        return BolsterEntity.from(getOwner());
    }

    /* Properties */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Gets a property valye
     *
     * @param key the key
     * @return the property value
     */
    public <T> T getProperty(Property<T> key) {
        return getProperties().get(key);
    }

    /**
     * Sets a property valye
     *
     * @param key   the key
     * @param value the value
     */
    public <T> void setProperty(Property<T> key, T value) {
        getProperties().set(key, value);
    }

    public void destroy() {
        this.setEnabled(false);
        setOwner(null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpellProvider provider) {
            return provider.getDefinition().equals(getDefinition());
        }

        return super.equals(obj);
    }
}
