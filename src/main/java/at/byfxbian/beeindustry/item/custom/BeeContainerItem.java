package at.byfxbian.beeindustry.item.custom;

import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

public class BeeContainerItem extends Item {

    public BeeContainerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if(interactionTarget instanceof CustomBeeEntity bee && stack.get(BeeIndustryDataComponents.STORED_BEE_ID.get()) == null) {
            if(!player.level().isClientSide()) {
                ItemStack filledContainer = new ItemStack(this);

                filledContainer.set(BeeIndustryDataComponents.STORED_BEE_ID.get(), bee.getBeeType());

                if (bee.isBaby()) {
                    filledContainer.set(BeeIndustryDataComponents.IS_BEE_BABY.get(), true);
                }

                player.setItemInHand(usedHand, filledContainer);

                bee.discard();
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        ResourceLocation storedBeeId = stack.get(BeeIndustryDataComponents.STORED_BEE_ID.get());

        if (storedBeeId != null) {
            String beeName = WordUtils.capitalizeFully(storedBeeId.getPath().replace('_', ' '));
            CustomBee beeData = BeeDefinitionManager.getBee(storedBeeId);
            if (beeData == null) {
                super.appendHoverText(stack, context, tooltip, flag);
                return;
            }

            tooltip.add(Component.translatable("item.beeindustry.bee_container.tooltip.contains", beeName)
                    .withStyle(ChatFormatting.GRAY));

            if (stack.getOrDefault(BeeIndustryDataComponents.IS_BEE_WORKING.get(), false)) {
                tooltip.add(Component.translatable("item.beeindustry.bee_container.tooltip.working")
                        .withStyle(ChatFormatting.RED));
            }

            if (Screen.hasShiftDown()) {
                tooltip.add(Component.literal(""));
                tooltip.add(Component.literal("Attributes:").withStyle(ChatFormatting.GOLD));
                tooltip.add(Component.literal(" - Max Health: " + beeData.attributes().maxHealth()).withStyle(ChatFormatting.AQUA));
                tooltip.add(Component.literal(" - Attack Damage: " + beeData.attributes().attackDamage()).withStyle(ChatFormatting.AQUA));
                tooltip.add(Component.literal(" - Productivity: " + beeData.attributes().productivity()).withStyle(ChatFormatting.AQUA));
                tooltip.add(Component.literal(" - Speed: " + beeData.attributes().speed()).withStyle(ChatFormatting.AQUA));
                tooltip.add(Component.literal(" - Temper: " + beeData.attributes().temper()).withStyle(ChatFormatting.AQUA));
                if (stack.getOrDefault(BeeIndustryDataComponents.IS_BEE_BABY.get(), false)) {
                    tooltip.add(Component.literal(" - Age: Baby").withStyle(ChatFormatting.GREEN));
                } else {
                    tooltip.add(Component.literal(" - Age: Adult").withStyle(ChatFormatting.GREEN));
                }
            } else {
                tooltip.add(Component.translatable("tooltip.beeindustry.hold_shift_for_info").withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            tooltip.add(Component.translatable("item.beeindustry.bee_container.tooltip.empty")
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if(!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = context.getItemInHand();
        ResourceLocation beeId = stack.get(BeeIndustryDataComponents.STORED_BEE_ID.get());

        if(beeId != null) {
            BlockPos spawnPos = context.getClickedPos().relative(context.getClickedFace());

            CustomBeeEntity spawnedEntity = BeeIndustryEntities.CUSTOM_BEE_ENTITY.get().create(serverLevel);
            if(spawnedEntity != null) {
                spawnedEntity.setPos(spawnPos.getX() + 0.5, spawnPos.getY() + 0.5, spawnPos.getZ() + 0.5);
                spawnedEntity.setBeeType(beeId);

                if(stack.getOrDefault(BeeIndustryDataComponents.IS_BEE_BABY.get(), false)) {
                    spawnedEntity.setBaby(true);
                }

                serverLevel.addFreshEntity(spawnedEntity);

                context.getPlayer().setItemInHand(context.getHand(), new ItemStack(this));
                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(context);
    }
}
