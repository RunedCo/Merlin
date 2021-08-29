package co.runed.merlin.commands;

import co.runed.bolster.commands.CommandBase;
import co.runed.merlin.concept.MerlinRegistries;
import co.runed.merlin.concept.classes.ClassImpl;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.paginate.PaginatedMenuBuilder;
import org.ipvp.canvas.slot.SlotSettings;
import org.ipvp.canvas.template.StaticItemTemplate;
import org.ipvp.canvas.type.ChestMenu;

import java.util.ArrayList;

public class CommandBecomeGUI extends CommandBase {
    public CommandBecomeGUI() {
        super("become");
    }

    @Override
    public CommandAPICommand build() {
        return new CommandAPICommand(this.command)
                .withPermission("bolster.commands.become")
                .executesPlayer(((sender, args) -> {
                    this.openMenu(sender);
                }));
    }

    private void openMenu(Player player) {
        var classes = new ArrayList<ClassImpl>();

        for (var id : MerlinRegistries.CLASSES.getEntries().keySet()) {
            classes.add(MerlinRegistries.CLASSES.get(id).create());
        }

        var pageTemplate = ChestMenu.builder(6).title("Classes").redraw(true);
        var itemSlots = BinaryMask.builder(pageTemplate.getDimensions())
                .pattern("111111111")
                .pattern("111111111")
                .pattern("111111111")
                .pattern("111111111")
                .pattern("111111111")
                .pattern("000000000")
                .build();

        var builder = PaginatedMenuBuilder.builder(pageTemplate)
                .slots(itemSlots)
                .nextButton(new ItemStack(Material.ARROW))
                .nextButtonSlot(51)
                .previousButton(new ItemStack(Material.ARROW))
                .previousButtonSlot(47);

        for (var clazz : classes) {
            if (clazz == null) continue;

            var settings = SlotSettings.builder()
                    .itemTemplate(new StaticItemTemplate(clazz.getIcon()))
                    .clickHandler((p, i) -> {
                        p.performCommand("become " + clazz.getId());
                        p.closeInventory();
                    })
                    .build();

            builder.addItem(settings);
        }

        var pages = builder.build();

        pages.get(0).open(player);
    }
}
