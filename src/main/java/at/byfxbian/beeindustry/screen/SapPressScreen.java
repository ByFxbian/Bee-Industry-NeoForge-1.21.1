package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.BeeIndustry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class SapPressScreen extends AbstractContainerScreen<SapPressMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "textures/gui/sap_press_gui.png");
    private static final ResourceLocation CRAFT_PROGRESS = ResourceLocation.fromNamespaceAndPath(BeeIndustry.MOD_ID, "container/sap_press/craft_progress");


    public SapPressScreen(SapPressMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(GuiGraphics gg, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        gg.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            int progress = menu.getScaledProgress();
            //gg.blit(TEXTURE, x + 85, y + 26, 176, 0, progress, 17);
            gg.blitSprite(CRAFT_PROGRESS, 24, 16, 0, 0, x + 76, y + 27, progress, 16);
        }

        int energy = menu.getEnergy();
        int maxEnergy = menu.getMaxEnergy();
        int energyHeight = (int) (52 * ((float)energy / (float)maxEnergy));
        gg.blit(TEXTURE, x + 164, y + 18 + (52 - energyHeight), 176, 17, 4, energyHeight);
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float delta) {
        renderBackground(gg, mouseX, mouseY, delta);
        super.render(gg, mouseX, mouseY, delta);
        renderTooltip(gg, mouseX, mouseY);

        if (isMouseOver(mouseX, mouseY, 164, 18, 4, 52)) {
            gg.renderTooltip(this.font, Component.literal(menu.getEnergy() + " / " + menu.getMaxEnergy() + " FE").toFlatList(), Optional.empty(), mouseX, mouseY);
        }
    }

    private boolean isMouseOver(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= (this.leftPos + x) && mouseX <= (this.leftPos + x + width) &&
                mouseY >= (this.topPos + y) && mouseY <= (this.topPos + y + height);
    }
}
