package ru.nekostul.blocktorch.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import ru.nekostul.blocktorch.BlockTorchState;
import net.minecraft.client.gui.components.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockTorchScreen extends Screen {

    private EditBox searchBox;

    private final List<Item> allItems = new ArrayList<>();
    private List<Item> visibleItems = new ArrayList<>();

    private int scrollOffset = 0;

    private static final int ITEMS_PER_ROW = 9;
    private static final int SLOT_SIZE = 20;
    private static final int ROWS_VISIBLE = 7;

    public BlockTorchScreen() {
        super(Component.literal("BlockTorch"));
    }

    @Override
    protected void init() {
        allItems.clear();
        BuiltInRegistries.ITEM.forEach(allItems::add);
        visibleItems = new ArrayList<>(allItems);

        searchBox = new EditBox(
                this.font,
                this.width / 2 - 80,
                10,
                160,
                18,
                Component.translatable("blocktorch.gui.title")
        );

        searchBox.setResponder(this::updateFilter);
        this.addRenderableWidget(searchBox);
        int gridStartY = 55; // где начинается сетка (у тебя уже есть startY = 55)
        int gridHeight = ROWS_VISIBLE * SLOT_SIZE;

        int closeButtonY = gridStartY + gridHeight + 10;

        this.addRenderableWidget(
                Button.builder(
                        Component.translatable("blocktorch.gui.close"),
                        btn -> this.onClose()
                ).bounds(
                        this.width / 2 - 50,
                        closeButtonY,
                        100,
                        20
                ).build()
        );

    }

    private void updateFilter(String text) {
        String q = text.toLowerCase();

        visibleItems = allItems.stream()
                .filter(item ->
                        BuiltInRegistries.ITEM.getKey(item)
                                .toString()
                                .toLowerCase()
                                .contains(q)
                )
                .collect(Collectors.toList());

        scrollOffset = 0;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int maxRows = Math.max(
                0,
                (visibleItems.size() + ITEMS_PER_ROW - 1) / ITEMS_PER_ROW - ROWS_VISIBLE
        );

        scrollOffset -= delta > 0 ? 1 : -1;
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxRows));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startX = (this.width - ITEMS_PER_ROW * SLOT_SIZE) / 2;
        int startY = 55;

        int startIndex = scrollOffset * ITEMS_PER_ROW;
        int endIndex = Math.min(
                visibleItems.size(),
                startIndex + ITEMS_PER_ROW * ROWS_VISIBLE
        );

        for (int i = startIndex; i < endIndex; i++) {
            int index = i - startIndex;
            int row = index / ITEMS_PER_ROW;
            int col = index % ITEMS_PER_ROW;

            int x = startX + col * SLOT_SIZE;
            int y = startY + row * SLOT_SIZE;

            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
                Item item = visibleItems.get(i);

                BlockTorchState.toggleItem(item);

                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null) {
                    mc.player.playSound(
                            net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK.value(),
                            1.0F,
                            1.0F
                    );
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);

        graphics.drawCenteredString(
                this.font,
                Component.translatable("blocktorch.gui.title"),
                this.width / 2,
                37,
                0xFFFFFF
        );

        int startX = (this.width - ITEMS_PER_ROW * SLOT_SIZE) / 2;
        int startY = 55;

        int startIndex = scrollOffset * ITEMS_PER_ROW;
        int endIndex = Math.min(
                visibleItems.size(),
                startIndex + ITEMS_PER_ROW * ROWS_VISIBLE
        );

        Item hoveredItem = null;

        for (int i = startIndex; i < endIndex; i++) {
            int index = i - startIndex;
            int row = index / ITEMS_PER_ROW;
            int col = index % ITEMS_PER_ROW;

            int x = startX + col * SLOT_SIZE;
            int y = startY + row * SLOT_SIZE;

            Item item = visibleItems.get(i);
            ItemStack stack = new ItemStack(item);

            graphics.renderItem(stack, x, y);

            if (BlockTorchState.isBlocked(item)) {
                RenderSystem.disableDepthTest();
                graphics.fill(x, y, x + 16, y + 16, 0x6600FF00);
                RenderSystem.enableDepthTest();
            }

            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
                hoveredItem = item;
            }
        }

        if (hoveredItem != null) {
            graphics.renderTooltip(
                    this.font,
                    Component.translatable(hoveredItem.getDescriptionId()),
                    mouseX,
                    mouseY
            );
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}