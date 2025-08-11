package at.byfxbian.beeindustry.screen;

import at.byfxbian.beeindustry.block.entity.custom.AdvancedBeehiveBlockEntity;
import at.byfxbian.beeindustry.block.entity.custom.BeepostBlockEntity;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class BeeContainerSlot extends SlotItemHandler {
    private final BlockEntity blockEntity;

    public BeeContainerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, BlockEntity blockEntity) {
        super(itemHandler, index, xPosition, yPosition);
        this.blockEntity = blockEntity;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if(!(blockEntity instanceof BeepostBlockEntity)) return stack.is(BeeIndustryItems.BEE_CONTAINER.get());
        if(!stack.is(BeeIndustryItems.BEE_CONTAINER.get())) return false;
        ResourceLocation beeId = stack.get(BeeIndustryDataComponents.STORED_BEE_ID.get());
        if(beeId == null) return false;
        String p = beeId.getPath();
        return p.equals("farming_bee") || p.equals("mining_bee") || p.equals("lumber_bee");
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return true;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        if(stack.getOrDefault(BeeIndustryDataComponents.IS_BEE_WORKING.get(), false)) {
            if (this.blockEntity instanceof AdvancedBeehiveBlockEntity beehive) {
                beehive.forceStopWorkingBee();
            } else if (this.blockEntity instanceof BeepostBlockEntity beepost) {
                beepost.forceStopWorkingBee(this.getSlotIndex());
            }
            stack.remove(BeeIndustryDataComponents.IS_BEE_WORKING.get());
        }
        super.onTake(player, stack);
    }
}
