package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.networking.payload.ToggleBeeSlotPayload;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.PacketDistributor;

public class BeepostScreen extends AbstractContainerScreen<BeepostMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "textures/gui/beepost_gui.png");

    private Button bee1Button;
    private Button bee2Button;
    private Button bee3Button;

    public BeepostScreen(BeepostMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        bee1Button = addRenderableWidget(Button.builder(getButtonText(0), button -> {
            PacketDistributor.sendToServer(new ToggleBeeSlotPayload(menu.blockEntity.getBlockPos(), 0));
        }).bounds(x + 8, y + 17, 16, 16).build());

        bee2Button = addRenderableWidget(Button.builder(getButtonText(1), button -> {
            PacketDistributor.sendToServer(new ToggleBeeSlotPayload(menu.blockEntity.getBlockPos(), 1));
        }).bounds(x + 8, y + 35, 16, 16).build());

        bee3Button = addRenderableWidget(Button.builder(getButtonText(2), button -> {
            PacketDistributor.sendToServer(new ToggleBeeSlotPayload(menu.blockEntity.getBlockPos(), 2));
        }).bounds(x + 8, y + 53, 16, 16).build());
    }

    private Component getButtonText(int slotIndex) {
        boolean isActive = this.menu.getData().get(slotIndex) == 1;
        return isActive ? Component.literal("✔").withStyle(ChatFormatting.GREEN) : Component.literal("✖").withStyle(ChatFormatting.RED);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        bee1Button.setMessage(getButtonText(0));
        bee2Button.setMessage(getButtonText(1));
        bee3Button.setMessage(getButtonText(2));
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
