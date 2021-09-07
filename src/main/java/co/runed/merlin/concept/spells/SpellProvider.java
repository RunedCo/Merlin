package co.runed.merlin.concept.spells;

import co.runed.bolster.damage.DamageSource;
import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.game.traits.TraitProvider;
import co.runed.bolster.game.traits.Traits;
import co.runed.bolster.managers.PlayerManager;
import co.runed.bolster.util.IconPreview;
import co.runed.bolster.util.Owned;
import co.runed.bolster.util.config.ConfigUtil;
import co.runed.bolster.util.config.Configurable;
import co.runed.dayroom.util.Describable;
import co.runed.dayroom.util.Enableable;
import co.runed.dayroom.util.Identifiable;
import co.runed.dayroom.util.Nameable;
import co.runed.merlin.concept.CastContext;
import co.runed.merlin.concept.definitions.SpellProviderDefinition;
import co.runed.merlin.core.SpellProviderType;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class SpellProvider extends TraitProvider implements Identifiable, Nameable, Describable, Configurable, Owned, Enableable, DamageSource, IconPreview {
    public static final String CONFIG_KEY_HEALTH = "health";

    private boolean enabled = false;
    private final SpellProviderDefinition<?> definition;
    private LivingEntity entity;
    private final List<Spell> spells = new ArrayList<>();
    private int level = -1;
    private int temporaryLevel = -1;
    private String name;
    private String description;
    private AttributeModifier maxHealthModifier;

    public SpellProvider(SpellProviderDefinition<?> definition) {
        this.definition = definition;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        System.out.println("Provider " + getId() + " enabled=" + enabled + " owner=" + getOwner());

        if (isEnabled() != enabled && getOwner() != null) {
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

    public void addSpell(Spell spell) {
        spell.setParent(this);
        spells.add(spell);
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public CastResult preCast(CastContext context) {
        if (!isEnabled()) return CastResult.fail();

        return CastResult.success();
    }

    public void postCast(CastContext context) {

    }

    @Override
    public void loadConfig(ConfigurationSection config) {
        ConfigUtil.parseVariables(config);

        description = config.getString("description", description);
        name = config.getString("name", name);
        setTrait(Traits.MAX_HEALTH, config.getDouble(CONFIG_KEY_HEALTH, getTrait(Traits.MAX_HEALTH)));

        for (var spell : spells) {
            if (config.isConfigurationSection(spell.getId())) {
                spell.loadConfig(Objects.requireNonNull(config.getConfigurationSection(spell.getId())));
            }
        }
    }

    @Override
    public String getId() {
        return getDefinition().getId();
    }

    @Override
    public String getName() {
        if (name == null) return getId();

        return name;
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

    @Override
    public LivingEntity getOwner() {
        return entity;
    }

    @Override
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

    public SpellProviderDefinition<?> getDefinition() {
        return definition;
    }

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
                var config = ConfigUtil.parseVariables(ConfigUtil.cloneSection(milestone.getOriginalConfig()), levels.get(level));

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

    public abstract SpellProviderType getType();

    public void destroy() {
        setOwner(null);
    }
}
