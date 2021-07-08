package co.runed.merlin.commands;

import co.runed.bolster.commands.CommandBase;
import co.runed.bolster.util.registries.Registry;
import co.runed.merlin.classes.MerlinClass;
import co.runed.merlin.core.ClassManager;
import co.runed.merlin.core.MerlinRegistries;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;

public class CommandBecome extends CommandBase
{
    public CommandBecome()
    {
        super("become");
    }

    private String[] getSuggestions(CommandSender sender)
    {
        return MerlinRegistries.CLASSES.getEntries().values().stream().map(Registry.Entry::getId).toArray(String[]::new);
    }

    @Override
    public CommandAPICommand build()
    {
        return new CommandAPICommand(this.command)
                .withPermission("bolster.commands.become")
                .withArguments(new StringArgument("class").overrideSuggestions(this::getSuggestions))
                .executesEntity((sender, args) -> {
                    String id = (String) args[0];
                    if (!(sender instanceof LivingEntity livingEntity)) return;
                    MerlinClass merlinClass = MerlinRegistries.CLASSES.get(id).create();

                    ClassManager.getInstance().setClass(livingEntity, merlinClass);

                    sender.sendMessage("Became " + merlinClass.getName());
                });
    }
}

