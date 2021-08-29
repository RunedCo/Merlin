package co.runed.merlin.concept.definitions;

import co.runed.bolster.managers.PlayerManager;
import co.runed.bolster.util.ItemBuilder;
import co.runed.bolster.util.StringUtil;
import co.runed.bolster.util.config.ConfigUtil;
import co.runed.bolster.util.registries.Definition;
import co.runed.merlin.Merlin;
import co.runed.merlin.concept.items.ItemImpl;
import co.runed.merlin.concept.spells.SpellDefinition;
import co.runed.merlin.concept.spells.SpellProvider;
import co.runed.merlin.concept.spells.SpellType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;

public abstract class SpellProviderDefinition<T extends SpellProvider> extends Definition<T> {
    private static final String CONFIG_KEY_LEVELS = "levels";
    private static final String CONFIG_KEY_MILESTONES = "milestones";

    private final Map<Integer, ConfigurationSection> levelConfigs = new HashMap<>();
    private final Map<Integer, ConfigurationSection> unmergedLevelConfigs = new HashMap<>();
    private final Map<Integer, MilestoneData> milestones = new HashMap<>();

    private final Set<SpellData> spells = new HashSet<>();

    private int maxLevel = 0;

    public SpellProviderDefinition(String id, Function<SpellProviderDefinition<T>, T> supplier) {
        super(id, (def) -> supplier.apply((SpellProviderDefinition<T>) def));
    }

    @Override
    public ConfigurationSection getConfig() {
        var config = super.getConfig();

        if (config.isConfigurationSection(CONFIG_KEY_LEVELS)) {
            var levelsConfig = config.getConfigurationSection(CONFIG_KEY_LEVELS);
            var allLevels = config.getValues(false);

            var baseLevel = ConfigUtil.fromMap(allLevels);
            levelConfigs.put(0, baseLevel);
            unmergedLevelConfigs.put(0, baseLevel);

            var keys = levelsConfig.getKeys(false);

            for (var level : keys) {
                try {
                    var levelNum = Integer.parseInt(level);
                    var levelConfig = levelsConfig.getConfigurationSection(level);
                    if (levelNum > maxLevel) maxLevel = levelNum;

                    ConfigUtil.deepMerge(allLevels, levelConfig.getValues(true));

                    unmergedLevelConfigs.put(levelNum, levelConfig);

                    var outConfig = ConfigUtil.fromMap(allLevels);
                    outConfig = ConfigUtil.parseVariables(outConfig);

                    System.out.println(getId() + " Loaded config for level " + level + ": " + outConfig);

                    levelConfigs.put(levelNum, outConfig);
                }
                catch (NumberFormatException e) {
                    Merlin.getInstance().getLogger().severe("Invalid level key '" + level + "' for " + getId() + "! A level key must be a number!");
                }
            }
        }

        if (config.isConfigurationSection(CONFIG_KEY_MILESTONES)) {
            var milestonesConfig = config.getConfigurationSection(CONFIG_KEY_MILESTONES);

            // get + process milestones from here
            for (var key : milestonesConfig.getKeys(false)) {
                if (!StringUtil.isInt(key)) continue;

                if (milestonesConfig.isConfigurationSection(key)) {
                    var milestoneConfig = ConfigUtil.cloneSection(milestonesConfig.getConfigurationSection(key));
                    var originalMilestoneConfig = ConfigUtil.cloneSection(milestoneConfig);

                    var milestoneLevel = Integer.parseInt(key);

                    ConfigUtil.parseVariables(milestoneConfig, this.getMergedLevels().get(milestoneLevel));

                    var milestoneData = new MilestoneData(milestoneLevel, milestoneConfig, originalMilestoneConfig);

                    this.milestones.put(milestoneLevel, milestoneData);
                }
            }
        }

        return config;
    }

    public Map<Integer, ConfigurationSection> getMergedLevels() {
        return levelConfigs;
    }

    public Map<Integer, ConfigurationSection> getUnmergedLevels() {
        return unmergedLevelConfigs;
    }

    public Map<Integer, MilestoneData> getMilestones() {
        return this.milestones;
    }

    public SpellProviderDefinition<T> addSpell(SpellDefinition spell, SpellType spellType) {
        return addSpell(spell, spellType, 0);
    }

    public SpellProviderDefinition<T> addSpell(SpellDefinition spell, SpellType spellType, long frequency) {
        spells.add(new SpellData(spell, spellType, frequency));

        return this;
    }

    @Override
    public void loadConfig(ConfigurationSection config) {

    }

    @Override
    protected T preCreate(T output) {
        var out = super.preCreate(output);

        out.setName(getName());

        return out;
    }

    @Override
    public T create() {
        var out = super.create();

        for (var data : spells) {
            var spell = data.definition.create();
            out.addSpell(spell);
        }

        return out;
    }

    public T createAtLevel(int level) {
        var out = this.create();

        out.setLevel(level);

        return out;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getLevel(Player player) {
        return PlayerManager.getInstance().getPlayerData(player).getProviderLevel(getId());
    }

    public List<String> getUpgradeTooltip(int level) {
        var tooltip = new ArrayList<String>();

        var config = unmergedLevelConfigs.get(level);

        if (config == null) return tooltip;

        if (config.isSet(ItemImpl.ATTACK_DAMAGE_KEY))
            tooltip.add(formatIncrease("Attack Damage", ItemImpl.ATTACK_DAMAGE_KEY, level));
        if (config.isSet(ItemImpl.ATTACK_SPEED_KEY))
            tooltip.add(formatIncrease("Attack Speed", ItemImpl.ATTACK_SPEED_KEY, level));
        if (config.isSet(ItemImpl.KNOCKBACK_RESISTANCE_KEY))
            tooltip.add(formatIncrease("Knockback Resistance", ItemImpl.KNOCKBACK_RESISTANCE_KEY, level));
        if (config.isSet(ItemImpl.KNOCKBACK_KEY))
            tooltip.add(formatIncrease("Knockback", ItemImpl.KNOCKBACK_KEY, level));
        if (config.isSet(SpellProvider.CONFIG_KEY_HEALTH))
            tooltip.add(formatIncrease("Health", SpellProvider.CONFIG_KEY_HEALTH, level));

        return tooltip;
    }

    public String formatIncrease(String name, String key, int newLevel) {
        var config = getMergedLevels().getOrDefault(Math.max(0, newLevel - 1), ConfigUtil.create());
        var newLevelConfig = getMergedLevels().getOrDefault(newLevel, config);

        var operator = newLevelConfig.getDouble(key) >= config.getDouble(key) ? "increased" : "decreased";
        return name + " " + operator + " to " + ChatColor.AQUA + newLevelConfig.getDouble(key);
    }

    private static class SpellData {
        private final SpellDefinition definition;
        private final SpellType spellType;
        private final long frequency;

        public SpellData(SpellDefinition definition, SpellType type, long frequency) {
            this.definition = definition;
            this.spellType = type;
            this.frequency = frequency;
        }
    }

    public static class MilestoneData {
        private final int level;
        private String name;
        private ItemStack icon;
        private List<String> tooltip;
        private ConfigurationSection config;
        private final ConfigurationSection originalConfig;

        public MilestoneData(int level, ConfigurationSection config, ConfigurationSection originalConfig) {
            this.level = level;
            this.originalConfig = originalConfig;
            this.setConfig(config);
        }

        public void setConfig(ConfigurationSection config) {
            this.config = config;

            this.name = config.getString("name", "Level " + this.level);
            this.icon = ConfigUtil.parseItemStack(this.config.getConfigurationSection("icon"));

            var tooltip = new ArrayList<String>();

            if (config.isSet("description")) tooltip.add(config.getString("description"));

            var formattedTooltip = new ArrayList<String>();

            for (var line : tooltip) {
                formattedTooltip.add(ChatColor.WHITE + line);
            }

            this.tooltip = formattedTooltip;
        }

        public ConfigurationSection getConfig() {
            return config;
        }

        public ConfigurationSection getOriginalConfig() {
            return originalConfig;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }

        public ItemStack getIcon() {
            var icon = this.icon.getType() == Material.AIR ? new ItemStack(Material.STICK) : this.icon;

            var builder = new ItemBuilder(icon)
                    .setDisplayName(Component.text(name, NamedTextColor.GOLD))
                    .addAllItemFlags();

            if (this.tooltip.size() > 0) builder = builder.addLore(this.tooltip);

            return builder.build();
        }

        public List<String> getTooltip() {
            return tooltip;
        }
    }
}
