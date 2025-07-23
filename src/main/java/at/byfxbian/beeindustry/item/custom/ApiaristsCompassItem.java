package at.byfxbian.beeindustry.item.custom;

import at.byfxbian.beeindustry.command.LocateNestCommand;
import at.byfxbian.beeindustry.networking.payload.NestFoundPayload;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

public class ApiaristsCompassItem extends Item {
    public ApiaristsCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);

        if(level instanceof ServerLevel serverLevel) {
            Optional<BlockPos> nearestNestPos = serverLevel.getPoiManager().findClosest(
                    poiType -> poiType.is(LocateNestCommand.BEE_NESTS_TAG),
                    player.blockPosition(),
                    1024,
                    PoiManager.Occupancy.ANY
            );

            nearestNestPos.ifPresentOrElse(
                    pos -> {
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new NestFoundPayload(pos));
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5F, 1.5F);
                    },
                    () -> {
                        player.sendSystemMessage(Component.translatable("item.beeindustry.apiarists_compass.no_nest_found"));
                    }
            );
        }

        player.getCooldowns().addCooldown(this, 100);
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.beeindustry.apiarists_compass.description")
                .withStyle(ChatFormatting.GRAY));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
