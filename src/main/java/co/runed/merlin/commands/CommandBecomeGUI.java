package co.runed.merlin.commands;

import co.runed.bolster.commands.CommandBase;
import co.runed.merlin.classes.MerlinClass;
import co.runed.merlin.core.MerlinRegistries;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.mask.Mask;
import org.ipvp.canvas.paginate.PaginatedMenuBuilder;
import org.ipvp.canvas.slot.SlotSettings;
import org.ipvp.canvas.template.StaticItemTemplate;
import org.ipvp.canvas.type.ChestMenu;

import java.util.ArrayList;
import java.util.List;

public class CommandBecomeGUI extends CommandBase
{
    public CommandBecomeGUI()
    {
        super("become");
    }

    @Override
    public CommandAPICommand build()
    {
        return new CommandAPICommand(this.command)
                .withPermission("bolster.commands.become")
                .executesPlayer(((sender, args) -> {
                    this.openMenu(sender);
                }));
    }

    private void openMenu(Player player)
    {
        List<MerlinClass> classes = new ArrayList<>();

        for (String id : MerlinRegistries.CLASSES.getEntries().keySet())
        {
            classes.add(MerlinRegistries.CLASSES.get(id).create());
        }

        ChestMenu.Builder pageTemplate = ChestMenu.builder(6).title("Classes").redraw(true);
        Mask itemSlots = BinaryMask.builder(pageTemplate.getDimensions())
                .pattern("111111111")
                .pattern("111111111")
                .pattern("111111111")
                .pattern("111111111")
                .pattern("111111111")
                .pattern("000000000")
                .build();

        PaginatedMenuBuilder builder = PaginatedMenuBuilder.builder(pageTemplate)
                .slots(itemSlots)
                .nextButton(new ItemStack(Material.ARROW))
                .nextButtonSlot(51)
                .previousButton(new ItemStack(Material.ARROW))
                .previousButtonSlot(47);

        for (MerlinClass clazz : classes)
        {
            if (clazz == null) continue;

            SlotSettings settings = SlotSettings.builder()
                    .itemTemplate(new StaticItemTemplate(clazz.getIcon()))
                    .clickHandler((p, i) -> {
                        p.performCommand("become " + clazz.getId());
                        p.closeInventory();
                    })
                    .build();

            builder.addItem(settings);
        }

        List<Menu> pages = builder.build();

        pages.get(0).open(player);
    }
}
