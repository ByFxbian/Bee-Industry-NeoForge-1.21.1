package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.BeeIndustry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedBeehiveScreen extends AbstractContainerScreen<AdvancedBeehiveMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "textures/gui/advanced_beehive_gui.png");

    public AdvancedBeehiveScreen(AdvancedBeehiveMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 202;
        this.imageHeight = 186;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderProgress(guiGraphics, x, y);
    }

    private void renderProgress(GuiGraphics guiGraphics, int x, int y) {
        if(this.menu.getData().get(0) > 0)  {
            int progress = menu.getData().get(0);
            int maxProgress = menu.getData().get(1);
            int spriteWidth = 162;
            int progressWidth = (int) (((float)progress / (float)maxProgress) * spriteWidth);

            guiGraphics.blit(TEXTURE, x + 7, y + 77, 0, 186, progressWidth, 5);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
