package co.runed.merlin.commands;

import co.runed.bolster.commands.CommandBase;
import co.runed.merlin.classes.TargetDummyClass;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Location;

public class CommandSummonDummy extends CommandBase
{
    public CommandSummonDummy()
    {
        super("dummy");
    }

    @Override
    public CommandAPICommand build()
    {
        return new CommandAPICommand(this.command)
                .withPermission("bolster.commands.dummy")
                .executesPlayer((sender, args) -> {
                    Location loc = sender.getLocation();

                    TargetDummyClass.summon(loc);

                    sender.sendMessage("Summoned Target Dummy");
                });
    }
}
