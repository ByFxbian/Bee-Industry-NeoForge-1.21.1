package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.BeeIndustry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class NectarLureScreen extends AbstractContainerScreen<NectarLureMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "textures/gui/nectar_lure_gui.png");

    public NectarLureScreen(NectarLureMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(GuiGraphics gg, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        gg.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            int progress = menu.getScaledProgress();
            gg.blit(TEXTURE, x + 79, y + 35, 176, 0, progress, 17); // Fortschrittspfeil
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float delta) {
        renderBackground(gg, mouseX, mouseY, delta);
        super.render(gg, mouseX, mouseY, delta);
        renderTooltip(gg, mouseX, mouseY);
    }
}
