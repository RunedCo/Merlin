package co.runed.merlin.gui;

import co.runed.bolster.game.PlayerData;
import co.runed.bolster.game.currency.Currency;
import co.runed.bolster.gui.Gui;
import co.runed.bolster.gui.GuiConstants;
import co.runed.bolster.managers.PlayerManager;
import co.runed.bolster.util.ItemBuilder;
import co.runed.merlin.concept.items.ItemDefinition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.paginate.PaginatedMenuBuilder;
import org.ipvp.canvas.slot.SlotSettings;
import org.ipvp.canvas.template.StaticItemTemplate;
import org.ipvp.canvas.type.ChestMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GuiMilestones extends Gui {
    PlayerData playerData;
    Player player;
    ItemDefinition item;
    Map<Currency, Integer> costs = new HashMap<>();

    public GuiMilestones(Gui prevGui, ItemDefinition item) {
        super(prevGui);

        this.item = item;
    }

    @Override
    public String getTitle(Player player) {
        return "Milestones for " + item.getName();
    }

    @Override
    protected Menu draw(Player player) {
        this.player = player;

        var pageTemplate = ChestMenu.builder(6)
                .title(getTitle(player))
                .redraw(true);

        var milestoneMask = BinaryMask.builder(pageTemplate.getDimensions())
                .pattern("000000000")
                .pattern("000000000")
                .pattern("000000000")
                .pattern("010101010")
                .pattern("010101010")
                .pattern("000000000")
                .build();

        var builder = PaginatedMenuBuilder.builder(pageTemplate)
                .slots(milestoneMask);

        playerData = PlayerManager.getInstance().getPlayerData(player);

        var milestones = item.getMilestones().values().stream().sorted((m, m2) -> m.getLevel() - m2.getLevel()).collect(Collectors.toList());

        for (var milestone : milestones) {
            var milestoneIcon = new ItemBuilder(milestone.getIcon());

            if (item.getMaxLevel() < milestone.getLevel()) {
                milestoneIcon = new ItemBuilder(GuiConstants.GUI_LOCK)
                        .setDisplayName(Component.text(milestone.getName(), NamedTextColor.GOLD))
                        .setLoreComponent(milestone.getIcon().getItemMeta().lore())
                        .addLoreComponent(Component.newline())
                        .addLoreComponent(Component.text("Unlocks at Level " + milestone.getLevel(), NamedTextColor.RED, TextDecoration.BOLD));
            }

            var settings = SlotSettings.builder()
                    .itemTemplate(new StaticItemTemplate(milestoneIcon.build()))
                    .build();

            builder.addItem(settings);
        }

        var pages = builder.build();

        for (var page : pages) {
            drawBase(page);
        }

        return pages.get(0);
    }

    public void drawBase(Menu menu) {
        var milestoneMask = BinaryMask.builder(menu.getDimensions())
                .pattern("111111111")
                .pattern("111111111")
                .item(GuiConstants.GUI_DIVIDER)
                .build();

        var level = item.getLevel(player);
        var itemInstance = item.createAtLevel(level);

        var baseIcon = itemInstance.getIcon();
        var stats = itemInstance.getStatsLore();

        var builder = new ItemBuilder(baseIcon.getType())
                .setDisplayName(baseIcon.getItemMeta().displayName())
                .addItemFlags(baseIcon.getItemMeta().getItemFlags());

        if (stats.size() > 0) {
            builder = builder.addLoreComponent(stats)
                    .addLore("");
        }

        builder = builder.addLore(ChatColor.WHITE + item.getConfig().getString("unlock-tooltip", ""));

        if (canUpgrade()) {
            var stringCosts = item.getUnmergedLevels()
                    .get(item.getLevel(player) + 1)
                    .getStringList("cost");

            costs = Currency.fromList(stringCosts);

            builder = builder.addLore("")
                    .addLore(ChatColor.WHITE + "Next Level:");

            for (var tip : item.getUpgradeTooltip(level + 1)) {
                builder = builder.addBullet(ChatColor.GRAY + tip);
            }

            builder = builder.addLore("")
                    .addLore(ChatColor.WHITE + "Cost to Level Up:");

            for (var cost : costs.entrySet()) {
                builder = builder.addBullet((canAfford(player, cost.getKey(), cost.getValue()) ? ChatColor.GREEN : ChatColor.RED) + (cost.getValue() + " " + cost.getKey().getPluralisedName()));
            }

            builder = builder.addLore("");

            if (!canAfford()) {
                builder = builder.addLore(GuiConstants.CANNOT_AFFORD_TO + "to level up!");
            }
            else {
                builder = builder.addLore(GuiConstants.CLICK_TO + "level up!");
            }
        }

        var settings = SlotSettings.builder()
                .itemTemplate(new StaticItemTemplate(builder.build()))
                .clickHandler((p, info) -> {
                    if (canAfford() && canUpgrade()) {
                        PlayerManager.getInstance().getPlayerData(p).setProviderLevel(item.getId(), item.getLevel(p) + 1);

                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1, 1);

                        show(p);
                    }
                })
                .build();

        milestoneMask.apply(menu);

        menu.getSlot(4).setSettings(settings);
    }

    private boolean canUpgrade() {
        return item.getLevel(player) < item.getMaxLevel();
    }

    private boolean canAfford() {
        return costs.entrySet().stream().allMatch((cost) -> canAfford(player, cost.getKey(), cost.getValue()));
    }

    public boolean canAfford(Player player, Currency currency, int amount) {
        return PlayerManager.getInstance().getPlayerData(player).getCurrency(currency) >= amount;
    }
}
