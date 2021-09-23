package co.runed.merlin.gui;

import co.runed.bolster.gui.Gui;
import co.runed.bolster.gui.GuiConstants;
import co.runed.bolster.util.Category;
import co.runed.merlin.core.MerlinRegistries;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.paginate.PaginatedMenuBuilder;
import org.ipvp.canvas.slot.SlotSettings;
import org.ipvp.canvas.template.StaticItemTemplate;
import org.ipvp.canvas.type.ChestMenu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GuiItems extends Gui {
    public GuiItems(Gui previousGui) {
        super(previousGui);
    }

    @Override
    public String getTitle(Player player) {
        return "Items";
    }

    @Override
    public Menu draw(Player player) {
        var itemCategories = new HashMap<Category, List<ItemStack>>();

        var pageTemplate = ChestMenu.builder(6)
                .title(this.getTitle(player))
                .redraw(true);

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
                .nextButton(GuiConstants.GUI_ARROW_RIGHT)
                .nextButtonSlot(51)
                .previousButton(GuiConstants.GUI_ARROW_LEFT)
                .previousButtonSlot(47);

        var items = MerlinRegistries.ITEMS.getEntries();

        for (var entry : items.values()) {
            var item = entry.create().create();

            for (var category : entry.getCategories()) {
                itemCategories.putIfAbsent(category, new ArrayList<>());

                item.setOwner(player);
                item.setOwner(null);
                item.rebuild();

                itemCategories.get(category).add(item.toItemStack());
            }

            item.destroy();
        }

        var sortedCategories = new ArrayList<>(itemCategories.keySet());
        sortedCategories.sort(Comparator.comparing(Category::getName));

        SlotSettings allItems = null;

        for (var category : sortedCategories) {
            var categoryItems = itemCategories.get(category);

            var settings = SlotSettings.builder()
                    .itemTemplate(new StaticItemTemplate(category.getIcon()))
                    .clickHandler((p, info) -> {
                        new GuiCategory(this, category, categoryItems).show(player);
                    })
                    .build();

            if (category == Category.ALL) {
                allItems = settings;

                continue;
            }

            builder.addItem(settings);
        }

        if (allItems != null) builder.addItem(allItems);

        var pages = builder.build();

        return pages.get(0);
    }
}
