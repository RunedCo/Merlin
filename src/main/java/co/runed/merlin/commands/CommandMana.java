package co.runed.merlin.commands;

import co.runed.bolster.commands.CommandBase;
import co.runed.merlin.core.ManaManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandMana extends CommandBase {
    List<String> operations = new ArrayList<>();
    List<String> types = new ArrayList<>();

    public CommandMana() {
        super("mana");
    }

    public void run(CommandSender sender, Object[] args) {
        var player = (Player) args[0];
        var operation = (String) args[1];
        var type = (String) args[2];

        if (operation.equals("get")) {
            if (type.equals("max")) {
                player.sendMessage("Your maximum mana is " + ManaManager.getInstance().getMaximumMana(player));
            }
            else {
                player.sendMessage("You have " + ManaManager.getInstance().getCurrentMana(player) + " mana");
            }

            return;
        }

        var amount = (float) args[3];

        if (operation.equals("subtract")) amount = amount * -1;

        if (type.equals("max")) {
            if (operation.equals("add") || operation.equals("subtract")) {
                amount = ManaManager.getInstance().getMaximumMana(player) + amount;
            }

            ManaManager.getInstance().setMaximumMana(player, amount);
        }
        else {
            if (operation.equals("add") || operation.equals("subtract")) {
                amount = ManaManager.getInstance().getCurrentMana(player) + amount;
            }

            ManaManager.getInstance().setCurrentMana(player, amount);
        }
    }

    @Override
    public CommandAPICommand build() {
        operations.add("set");
        operations.add("add");
        operations.add("subtract");
        operations.add("get");

        types.add("current");
        types.add("max");

        List<Argument> arguments = new ArrayList<>();
        arguments.add(new PlayerArgument("player"));
        arguments.add(new StringArgument("operation").overrideSuggestions(operations.toArray(new String[0])));
        arguments.add(new StringArgument("type").overrideSuggestions(types.toArray(new String[0])));
        arguments.add(new FloatArgument("amount"));

        return new CommandAPICommand(this.command)
                .withPermission("bolster.commands.mana")
                .withArguments(arguments)
                .executes(this::run);

        /* return new CommandAPICommand(this.command)
                .withPermission("bolster.commands.mana")
                .withSubcommand(new CommandAPICommand("set")); */
    }
}
