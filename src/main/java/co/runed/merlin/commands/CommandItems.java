package co.runed.merlin.commands;

import co.runed.bolster.commands.CommandBase;
import co.runed.bolster.managers.PlayerManager;
import co.runed.bolster.util.Category;
import co.runed.bolster.util.registries.Registry;
import co.runed.merlin.core.MerlinPermissions;
import co.runed.merlin.core.MerlinRegistries;
import co.runed.merlin.gui.GuiMilestones;
import co.runed.merlin.items.ItemDefinition;
import co.runed.merlin.items.ItemManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandItems extends CommandBase {
    public CommandItems() {
        super("items");
    }

    private String[] getSuggestions(CommandSender sender) {
        return MerlinRegistries.ITEMS.getEntries().values().stream().map(Registry.Entry::getId).toArray(String[]::new);
    }

    private String[] getLevelableSuggestions(CommandSender sender) {
        var items = new ArrayList<>();

        for (var item : MerlinRegistries.ITEMS.getEntries().values()) {
            if (!item.getCategories().contains(Category.LEVELABLE)) continue;

            items.add(item.getId());
        }

        return items.toArray(new String[0]);
    }

    @Override
    public CommandAPICommand build() {
        return new CommandAPICommand(this.command)
                .withPermission(MerlinPermissions.COMMAND_ITEMS)
                .withSubcommand(new CommandAPICommand("give")
                        .withArguments(
                                new PlayerArgument("player"),
                                new StringArgument("item_id").overrideSuggestions(this::getSuggestions),
                                new IntegerArgument("amount", 0)
                        )
                        .executes((sender, args) -> {
                            var player = (Player) args[0];
                            var id = (String) args[1];
                            var amount = (int) args[2];
                            var itemDef = MerlinRegistries.ITEMS.get(id);

                            var item = ItemManager.getInstance().giveItem(player, player.getInventory(), itemDef, amount);

                            sender.sendMessage("Gave " + amount + " " + item.getName() + " to " + player.getDisplayName());
                        })
                )
                .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(
                                new PlayerArgument("player"),
                                new StringArgument("item_id").overrideSuggestions(this::getSuggestions),
                                new IntegerArgument("amount", 0)
                        )
                        .executes((sender, args) -> {
                            var player = (Player) args[0];
                            var id = (String) args[1];
                            var amount = (int) args[2];
                            var itemDef = MerlinRegistries.ITEMS.get(id);

                            var amountInInv = ItemManager.getInstance().getItemCount(player.getInventory(), itemDef);

                            ItemManager.getInstance().removeItem(player.getInventory(), itemDef, amount);

                            sender.sendMessage("Removed " + Math.min(amountInInv, amount) + " " + itemDef.getName() + " from " + player.getDisplayName());
                        })
                )

                .withSubcommand(new CommandAPICommand("clear")
                        .withArguments(
                                new PlayerArgument("player"),
                                new StringArgument("item_id").overrideSuggestions(this::getSuggestions)
                        )
                        .executes((sender, args) -> {
                            var player = (Player) args[0];
                            var id = (String) args[1];
                            var itemDef = MerlinRegistries.ITEMS.get(id);

                            ItemManager.getInstance().removeItem(player.getInventory(), itemDef, Integer.MAX_VALUE);

                            sender.sendMessage("Cleared all " + itemDef.getName() + " from " + player.getDisplayName());
                        })
                )
                .withSubcommand(new CommandAPICommand("milestones")
                        .withArguments(
                                new PlayerArgument("player"),
                                new StringArgument("item_id").overrideSuggestions(this::getSuggestions)
                        )
                        .executes((sender, args) -> {
                            var player = (Player) args[0];
                            var id = (String) args[1];
                            var itemDef = MerlinRegistries.ITEMS.get(id);

                            if (!(itemDef instanceof ItemDefinition realItemDef)) {
                                sender.sendMessage("Invalid item '" + id + "'");
                                return;
                            }

                            new GuiMilestones(null, realItemDef).show(player);
                        })
                )
                .withSubcommand(new CommandAPICommand("setlevel")
                        .withArguments(
                                new PlayerArgument("player"),
                                new StringArgument("item_id").overrideSuggestions(this::getLevelableSuggestions),
                                new IntegerArgument("amount", 0)
                        )
                        .executes((sender, args) -> {
                            var player = (Player) args[0];
                            var id = (String) args[1];
                            var level = (int) args[2];

                            if (!MerlinRegistries.ITEMS.contains(id)) {
                                sender.sendMessage("Invalid item id '" + id + "'");
                                return;
                            }

                            var plainDef = MerlinRegistries.ITEMS.get(id);

                            if (plainDef instanceof ItemDefinition itemDef && itemDef.getMaxLevel() > 0) {
                                var fetchedItem = ItemManager.getInstance().getOrCreate(player, itemDef);

                                PlayerManager.getInstance().getPlayerData(player).setProviderLevel(id, level);

                                if (fetchedItem != null) {
                                    fetchedItem.setLevel(level);
                                    fetchedItem.rebuild();
                                }

                                sender.sendMessage("Set " + itemDef.getName() + "'s level to " + Math.min(level, itemDef.getMaxLevel()) + " for player " + player.getDisplayName());
                            }
                            else {
                                sender.sendMessage(id + ChatColor.WHITE + " is not an item that can be leveled.");
                            }
                        })
                );
    }
}
