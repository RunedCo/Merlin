package co.runed.merlin.items;

import co.runed.bolster.managers.PlayerManager;
import co.runed.bolster.util.ItemBuilder;
import co.runed.bolster.util.StringUtil;
import co.runed.bolster.util.config.ConfigUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class LevelableItem extends Item
{
    int level = 0;
    int tempLevel = -1;
    boolean mergeLevels = true;

    HashMap<Integer, ConfigurationSection> levels = new HashMap<>();
    HashMap<Integer, ConfigurationSection> unmergedLevels = new HashMap<>();
    HashMap<Integer, MilestoneData> milestones = new HashMap<>();

    private static final String MERGE_LEVELS_KEY = "merge-levels";
    private static final String LEVELS_KEY = "levels";
    private static final String MILESTONE_LEVELS_KEY = "milestones";

    @Override
    public List<Component> getStatsLore()
    {
        List<Component> out = new ArrayList<>();

        out.add(Component.text(this.getLevelDisplayText()));
        out.addAll(super.getStatsLore());

        return out;
    }

    @Override
    public void setConfig(ConfigurationSection config)
    {
        super.setConfig(config);

        this.mergeLevels = config.getBoolean(MERGE_LEVELS_KEY, this.mergeLevels);

        // LOAD BASE
        // LOAD LEVEL SPECIFIC

        this.levels.put(0, ConfigUtil.cloneSection(config));

        if (config.isList(LEVELS_KEY))
        {
            // TODO FIX
            List<LinkedHashMap<String, Object>> levels = (List<LinkedHashMap<String, Object>>) config.getList(LEVELS_KEY);

            if (levels == null) return;

            Map<String, Object> allLevels = config.getValues(false);

            this.levels.put(0, ConfigUtil.fromMap(allLevels));

            for (int i = 0; i < levels.size(); i++)
            {
                ConfigUtil.deepMerge(allLevels, levels.get(i));

                //allLevels.putAll(levels.get(i));

                this.unmergedLevels.put(i + 1, ConfigUtil.fromMap(levels.get(i)));
                this.levels.put(i + 1, ConfigUtil.fromMap(allLevels));
            }
        }

        if (config.isConfigurationSection(MILESTONE_LEVELS_KEY))
        {
            ConfigurationSection milestonesConfig = config.getConfigurationSection(MILESTONE_LEVELS_KEY);

            // get + process milestones from here
            for (String key : milestonesConfig.getKeys(false))
            {
                if (!StringUtil.isInt(key)) continue;

                if (milestonesConfig.isConfigurationSection(key))
                {
                    ConfigurationSection milestoneConfig = ConfigUtil.cloneSection(milestonesConfig.getConfigurationSection(key));
                    ConfigurationSection originalMilestoneConfig = ConfigUtil.cloneSection(milestoneConfig);

                    int milestoneLevel = Integer.parseInt(key);

                    ConfigUtil.parseVariables(milestoneConfig, this.getLevels().get(milestoneLevel));

                    MilestoneData milestoneData = new MilestoneData(milestoneLevel, milestoneConfig, originalMilestoneConfig);

                    this.milestones.put(milestoneLevel, milestoneData);
                }
            }
        }
    }

    @Override
    public void loadConfig(ConfigurationSection config)
    {
        HashMap<Integer, ConfigurationSection> mapToUse = this.levels;

        if (!this.mergeLevels) mapToUse = this.unmergedLevels;

        ConfigurationSection cumulativeLevelConfig = mapToUse.get(Math.max(0, Math.min(this.getLevel(), mapToUse.size())));

        ConfigUtil.merge(config, cumulativeLevelConfig);

        super.loadConfig(config);
    }

    public void clearTemporaryLevel()
    {
        this.tempLevel = -1;

        this.setLevel(this.level);
    }

    public void setLevel(int level)
    {
        this.setLevel(level, false);
    }

    public void setLevel(int level, boolean isTemporary)
    {
        int previousLevel = this.level;

        level = Math.max(0, Math.min(level, this.levels.size() - 1));

        this.level = level;

        if (isTemporary) this.tempLevel = level;

        for (int milestoneLevel : this.milestones.keySet())
        {
            if (this.level > milestoneLevel)
            {
                MilestoneData milestone = this.milestones.get(milestoneLevel);
                ConfigurationSection config = ConfigUtil.parseVariables(ConfigUtil.cloneSection(milestone.getOriginalConfig()), this.getLevels().get(this.level));

                milestone.setConfig(config);
            }
        }

        if (this.getEntity() instanceof Player && !isTemporary)
        {
            Player player = (Player) this.getEntity();

            PlayerManager.getInstance().getPlayerData(player).setItemLevel(this.getId(), this.level);
        }

        if (this.level != previousLevel) this.setDirty();
    }

    public int getLevel()
    {
        return level;
    }

    public int getMaxLevel()
    {
        // Minus 1 because it includes level 0
        return this.levels.size() - 1;
    }

    public String getLevelDisplayText()
    {
        return ChatColor.GRAY + "Level " + this.getLevel();
    }

    public List<String> getUpgradeTooltip(int level)
    {
        List<String> tooltip = new ArrayList<>();

        ConfigurationSection config = this.unmergedLevels.get(level);

        if (config == null) return tooltip;

        if (config.isSet(ATTACK_DAMAGE_KEY))
            tooltip.add(formatIncrease("Attack Damage", ATTACK_DAMAGE_KEY, level));
        if (config.isSet(ATTACK_SPEED_KEY))
            tooltip.add(formatIncrease("Attack Speed", ATTACK_SPEED_KEY, level));
        if (config.isSet(KNOCKBACK_RESISTANCE_KEY))
            tooltip.add(formatIncrease("Knockback Resistance", KNOCKBACK_RESISTANCE_KEY, level));
        if (config.isSet(KNOCKBACK_KEY))
            tooltip.add(formatIncrease("Knockback", KNOCKBACK_KEY, level));
        if (config.isSet(HEALTH_KEY))
            tooltip.add(formatIncrease("Health", HEALTH_KEY, level));

        return tooltip;
    }

    public String formatIncrease(String name, String key, int newLevel)
    {
        ConfigurationSection config = this.getLevels().get(this.getLevel());
        ConfigurationSection newLevelConfig = this.getLevels().get(newLevel);

        String operator = newLevelConfig.getDouble(key) >= config.getDouble(key) ? "increased" : "decreased";
        return name + " " + operator + " to " + ChatColor.AQUA + newLevelConfig.getDouble(key);
    }

    public HashMap<Integer, ConfigurationSection> getLevels()
    {
        return levels;
    }

    public HashMap<Integer, ConfigurationSection> getUnmergedLevels()
    {
        return unmergedLevels;
    }

    public HashMap<Integer, MilestoneData> getMilestones()
    {
        return this.milestones;
    }

    @Override
    public void setEntity(LivingEntity entity, boolean trigger)
    {
        if (entity instanceof Player player)
        {
            int level = PlayerManager.getInstance().getPlayerData(player).getItemLevel(this.getId());

            if (tempLevel > -1) level = tempLevel;

            this.setLevel(level, tempLevel > -1);
        }

        super.setEntity(entity, trigger);
    }

    public static class MilestoneData
    {
        int level;
        String name;
        ItemStack icon;
        List<String> tooltip;
        ConfigurationSection config;
        ConfigurationSection originalConfig;

        public MilestoneData(int level, ConfigurationSection config, ConfigurationSection originalConfig)
        {
            this.level = level;
            this.originalConfig = originalConfig;
            this.setConfig(config);
        }

        public void setConfig(ConfigurationSection config)
        {
            this.config = config;

            this.name = config.getString("name", "Level " + this.level);
            this.icon = ConfigUtil.parseItemStack(this.config.getConfigurationSection("icon"));

            List<String> tooltip = new ArrayList<>();

            if (config.isSet("description")) tooltip.add(config.getString("description"));

            List<String> formattedTooltip = new ArrayList<>();

            for (String line : tooltip)
            {
                formattedTooltip.add(ChatColor.WHITE + line);
            }

            this.tooltip = formattedTooltip;
        }

        public ConfigurationSection getConfig()
        {
            return config;
        }

        public ConfigurationSection getOriginalConfig()
        {
            return originalConfig;
        }

        public String getName()
        {
            return name;
        }

        public int getLevel()
        {
            return level;
        }

        public ItemStack getIcon()
        {
            ItemStack icon = this.icon.getType() == Material.AIR ? new ItemStack(Material.STICK) : this.icon;

            ItemBuilder builder = new ItemBuilder(icon)
                    .setDisplayName(Component.text(name, NamedTextColor.GOLD))
                    .addAllItemFlags();

            if (this.tooltip.size() > 0) builder = builder.addLore(this.tooltip);

            return builder.build();
        }

        public List<String> getTooltip()
        {
            return tooltip;
        }
    }
}
