package co.runed.merlin.commands;

import co.runed.bolster.commands.CommandBase;
import co.runed.merlin.core.MerlinPermissions;
import co.runed.merlin.gui.GuiItems;
import dev.jorel.commandapi.CommandAPICommand;

public class CommandItemsGUI extends CommandBase {
    public CommandItemsGUI() {
        super("items");
    }

    @Override
    public CommandAPICommand build() {
        return new CommandAPICommand(this.command)
                .withPermission(MerlinPermissions.COMMAND_ITEMS)
                .executesPlayer(((sender, args) -> {
                    new GuiItems(null).show(sender);
                }));
    }
}
