package at.byfxbian.beeindustry.event;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.block.entity.BeeIndustryBlockEntities;
import at.byfxbian.beeindustry.command.LocateNestCommand;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.util.BeeIndustryVillagers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;

@EventBusSubscriber(modid = BeeIndustry.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BeeIndustryBlockEntities.ADVANCED_BEEHIVE_BE.get(),
                (blockEntity, side) -> blockEntity.getItemHandler()
        );
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == BeeIndustryVillagers.BEEKEEPER.value()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 2),
                    new ItemStack(BeeIndustryItems.BEE_CONTAINER.get(), 1), 10, 1, 0.02f
            ));

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(BeeIndustryItems.BEE_SMOKER.get(), 1), 1, 2, 0.04f
            ));

            // TODO: RÜSTUNG EINFÜGEN

            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 12),
                    new ItemStack(BeeIndustryBlocks.BEEPOST.get().asItem(), 1), 1, 2, 0.05f
            ));

            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 10),
                    new ItemStack(BeeIndustryBlocks.ADVANCED_BEEHIVE.get().asItem(), 1), 1, 2, 0.05f
            ));
        }
    }

    @SubscribeEvent
    public static void onBeeSetTarget(LivingChangeTargetEvent event) {
        if(event.getNewAboutToBeSetTarget() instanceof Player player && event.getEntity() instanceof Bee) {
            if(isWearingFullBeekeeperSet(player)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent.Pre event) {
        if(event.getEntity() instanceof Player player) {
            DamageSource source = event.getEntity().getLastDamageSource();
            if(source != null) {
                if(source.getDirectEntity() instanceof Bee && isWearingFullBeekeeperSet(player)) {
                    event.setNewDamage(0);
                }
            }
            if(player.getItemBySlot(EquipmentSlot.LEGS).is(BeeIndustryItems.BEEKEEPER_LEGGINGS.get())) {
                applyBonemealOnDamage(player);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(EntityTickEvent.Post event) {
        if(event.getEntity() instanceof Player player && !player.level().isClientSide) {
            if(player.getItemBySlot(EquipmentSlot.FEET).is(BeeIndustryItems.BEEKEEPER_BOOTS.get())) {
                if(player.level().getBlockState(player.blockPosition().below()).is(BlockTags.DIRT)) {
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, true, false, false));
                }
            }
            if(isWearingFullBeekeeperSet(player)) {
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 40, 0, true, false, false));
            }
        }
    }

    private static void applyBonemealOnDamage(Player player) {
        Level world = player.level();
        if(world instanceof ServerLevel serverWorld) {
            BlockPos centerPos = player.blockPosition().below();
            for(BlockPos currentPos : BlockPos.betweenClosed(centerPos.offset(-1, 0, -1), centerPos.offset(1, 0, 1))) {
                BlockState state = world.getBlockState(currentPos);
                if(state.getBlock() instanceof BonemealableBlock fertilizable && fertilizable.isValidBonemealTarget(world, currentPos, state)) {
                    fertilizable.performBonemeal(serverWorld, world.random, currentPos.immutable(), state);
                    return;
                }
            }
        }
    }

    private static boolean isWearingFullBeekeeperSet(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).is(BeeIndustryItems.BEEKEEPER_HELMET.get())
                && player.getItemBySlot(EquipmentSlot.CHEST).is(BeeIndustryItems.BEEKEEPER_CHESTPLATE.get())
                && player.getItemBySlot(EquipmentSlot.LEGS).is(BeeIndustryItems.BEEKEEPER_LEGGINGS.get())
                && player.getItemBySlot(EquipmentSlot.FEET).is(BeeIndustryItems.BEEKEEPER_BOOTS.get());
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        LocateNestCommand.register(event.getDispatcher());
    }


}
