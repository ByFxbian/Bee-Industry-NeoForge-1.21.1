package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.BeeIndustry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class BeenergyGeneratorScreen extends AbstractContainerScreen<BeenergyGeneratorMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "textures/gui/beenergy_generator_gui.png");
    private static final ResourceLocation LIT_PROGRESS = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "container/beenergy_generator/lit_progress");

    public BeenergyGeneratorScreen(BeenergyGeneratorMenu menu, Inventory inv, Component title) {
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
        if (menu.isBurning()) {
            int progress = menu.getProgress();
            int maxProgress = menu.getMaxProgress();
            int spriteHeight = 14;
            //int burnHeight = (int) (13 * (1 - ((float)progress / (float)maxProgress)));
            //int burnHeight = (int) (((float)progress / (float)maxProgress) * spriteHeight);
            //gg.blit(TEXTURE, x + 81, y + 20 + burnHeight, 0, 168, 14, burnHeight);
            int l = (int) (((float)progress / (float)maxProgress) * spriteHeight);
            gg.blitSprite(LIT_PROGRESS, 14, 14, 0, 14 - l, x + 81, y + 20 + 14 - l, 14, l);
        }
        int energy = menu.getEnergy();
        int maxEnergy = menu.getMaxEnergy();
        int energyHeight = (int) (52 * ((float)energy / (float)maxEnergy));
        gg.blit(TEXTURE, x + 164, y + 18 + (52 - energyHeight), 176, 0, 4, energyHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        if (isMouseOver(mouseX, mouseY, 164, 18, 4, 52)) {
            guiGraphics.renderTooltip(this.font, Component.literal(menu.getEnergy() + " / " + menu.getMaxEnergy() + " FE").toFlatList(), Optional.empty(), mouseX, mouseY);
        }
    }

    private boolean isMouseOver(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= (this.leftPos + x) && mouseX <= (this.leftPos + x + width) &&
                mouseY >= (this.topPos + y) && mouseY <= (this.topPos + y + height);
    }
}
