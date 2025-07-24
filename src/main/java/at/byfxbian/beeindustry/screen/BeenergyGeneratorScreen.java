package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.BeeIndustry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BeenergyGeneratorScreen extends AbstractContainerScreen<BeenergyGeneratorMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "textures/gui/beenergy_generator_gui.png");

    public BeenergyGeneratorScreen(BeenergyGeneratorMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(GuiGraphics gg, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        gg.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        if (menu.isBurning()) {
            int progress = menu.getProgress();
            int maxProgress = menu.getMaxProgress();
            int burnHeight = (int) (13 * (1 - ((float)progress / (float)maxProgress)));
            gg.blit(TEXTURE, x + 80, y + 36 + burnHeight, 176, burnHeight, 14, 14 - burnHeight);
        }
        int energy = menu.getEnergy();
        int maxEnergy = menu.getMaxEnergy();
        int energyHeight = (int) (52 * ((float)energy / (float)maxEnergy));
        gg.blit(TEXTURE, x + 152, y + 17 + (52 - energyHeight), 176, 14, 16, energyHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
