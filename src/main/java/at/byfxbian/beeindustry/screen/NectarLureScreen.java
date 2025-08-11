package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.BeeIndustry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class NectarLureScreen extends AbstractContainerScreen<NectarLureMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "textures/gui/nectar_lure_gui.png");

    public NectarLureScreen(NectarLureMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 168;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics gg, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        gg.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderProgress(gg, x, y);
    }

    private void renderProgress(GuiGraphics guiGraphics, int x, int y) {
        if(this.menu.data.get(0) > 0)  {
            int progress = menu.data.get(0);
            int maxProgress = menu.data.get(1);
            System.out.println(progress);
            System.out.println(maxProgress);
            int spriteWidth = 162;
            int progressWidth = (int) ((progress / (float)maxProgress) * spriteWidth);
            guiGraphics.blit(TEXTURE, x + 7, y + 62, 0, 168, progressWidth, 5);
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float delta) {
        renderBackground(gg, mouseX, mouseY, delta);
        super.render(gg, mouseX, mouseY, delta);
        renderTooltip(gg, mouseX, mouseY);
    }
}
