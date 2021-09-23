package co.runed.merlin.gui;

import co.runed.bolster.entity.BolsterEntity;
import co.runed.bolster.gui.Gui;
import co.runed.bolster.gui.GuiConstants;
import co.runed.bolster.util.Category;
import co.runed.bolster.v1_16_R3.CraftUtil;
import co.runed.merlin.items.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.ipvp.canvas.ClickInformation;
import org.ipvp.canvas.Menu;
import org.ipvp.canvas.mask.BinaryMask;
import org.ipvp.canvas.paginate.PaginatedMenuBuilder;
import org.ipvp.canvas.slot.SlotSettings;
import org.ipvp.canvas.template.StaticItemTemplate;
import org.ipvp.canvas.type.ChestMenu;

import java.util.List;

public class GuiCategory extends Gui {
    Category category;
    List<ItemStack> items;

    public GuiCategory(Gui previousGui, Category category, List<ItemStack> items) {
        super(previousGui);

        this.previousGui = previousGui;
        this.category = category;
        this.items = items;
    }

    @Override
    public String getTitle(Player player) {
        var title = this.category.getName();

        if (this.previousGui != null) {
            title = this.previousGui.getTitle(player) + " - " + title;
        }

        return title;
    }

    @Override
    public Menu draw(Player player) {
        var pageTemplate = ChestMenu.builder(6).title("Items - " + category.getName());
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

        for (var item : items) {
            var settings = SlotSettings.builder()
                    .itemTemplate(new StaticItemTemplate(item))
                    .clickHandler(this::givePlayerItem)
                    .build();

            builder.addItem(settings);
        }

        var pages = builder.build();

        return pages.get(0);
    }

    private void givePlayerItem(Player player, ClickInformation info) {
        var stackAmount = info.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || info.getAction() == InventoryAction.DROP_ALL_SLOT ? 64 : 1;
        var stack = info.getClickedSlot().getItem(player);
        stackAmount = Math.min(stackAmount, stack.getMaxStackSize());
        var itemDef = ItemManager.getInstance().getDefFrom(stack);

        if (info.getAction() == InventoryAction.DROP_ALL_SLOT || info.getAction() == InventoryAction.DROP_ONE_SLOT) {
            var item = ItemManager.getInstance().getOrCreate(player, itemDef);

            var itemStack = item.toItemStack();
            itemStack.setAmount(stackAmount);

            CraftUtil.dropItem(player, itemStack);
            return;
        }

        ItemManager.getInstance().giveItem(player, BolsterEntity.from(player).getPlayerInventory(), itemDef, stackAmount);
    }
}
