package at.byfxbian.beeindustry.event;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.block.entity.BeeIndustryBlockEntities;
import at.byfxbian.beeindustry.block.entity.custom.AdvancedBeehiveBlockEntity;
import at.byfxbian.beeindustry.block.entity.custom.BeepostBlockEntity;
import at.byfxbian.beeindustry.command.LocateNestCommand;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.custom.CustomBeeEntity;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.util.BeeIndustryVillagers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = BeeIndustry.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BeeIndustryBlockEntities.ADVANCED_BEEHIVE_BE.get(),
                (blockEntity, side) -> {
                    if(side == Direction.DOWN) {
                        return blockEntity.getSidedOutputHandler();
                    }
                    return blockEntity.getSidedInputHandler();
                }
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BeeIndustryBlockEntities.BEEPOST_BE.get(),
                (blockEntity, side) -> {
                    if(side == Direction.DOWN) {
                        return blockEntity.getSidedOutputHandler();
                    }
                    return blockEntity.getSidedInputHandler();
                }
        );

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BeeIndustryBlockEntities.BEENERGY_GENERATOR_BE.get(),
                (blockEntity, side) -> {
                    if(side == Direction.DOWN) {
                        return blockEntity.getSidedOutputHandler();
                    }
                    return blockEntity.getSidedInputHandler();
                }
        );
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                BeeIndustryBlockEntities.BEENERGY_GENERATOR_BE.get(),
                (blockEntity, pos) -> blockEntity.getEnergyStorage()
        );

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                BeeIndustryBlockEntities.SAP_PRESS_BE.get(),
                (blockEntity, side) -> blockEntity.getEnergyStorage()
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BeeIndustryBlockEntities.SAP_PRESS_BE.get(),
                (blockEntity, side) -> {
                    if(side == Direction.DOWN) {
                        return blockEntity.getSidedOutputHandler();
                    }
                    return blockEntity.getSidedInputHandler();
                }
        );

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BeeIndustryBlockEntities.NECTAR_LURE_BE.get(),
                (blockEntity, side) -> {
                    if(side == Direction.DOWN) {
                        return blockEntity.getSidedOutputHandler();
                    }
                    return blockEntity.getSidedInputHandler();
                }
        );

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                BeeIndustryBlockEntities.CABLE_BE.get(),
                (blockEntity, side) -> blockEntity.getEnergyStorage()
        );
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == BeeIndustryVillagers.BEEKEEPER.value()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // Level 1 (Novice)
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 2),
                    new ItemStack(BeeIndustryItems.BEE_CONTAINER.get(), 1), 12, 2, 0.05f
            ));
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(BeeIndustryItems.BEE_SMOKER.get(), 1), 1, 5, 0.05f
            ));

            // Level 2 (Apprentice)
            trades.get(2).add((entity, randomSource) -> {
                Item armorPiece;
                int price;
                switch(randomSource.nextInt(4)) {
                    case 0: armorPiece = BeeIndustryItems.BEEKEEPER_HELMET.get(); price = 16; break;
                    case 1: armorPiece = BeeIndustryItems.BEEKEEPER_CHESTPLATE.get(); price = 22; break;
                    case 2: armorPiece = BeeIndustryItems.BEEKEEPER_LEGGINGS.get(); price = 20; break;
                    default: armorPiece = BeeIndustryItems.BEEKEEPER_BOOTS.get(); price = 14; break;
                }
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, price),
                        new ItemStack(armorPiece, 1), 1, 10, 0.05f);
            });
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.HONEYCOMB_BLOCK, 4),
                    new ItemStack(Items.EMERALD, 1), 16, 5, 0.05f
            ));

            // Level 3 (Journeyman)
            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 24),
                    new ItemStack(BeeIndustryBlocks.ADVANCED_BEEHIVE.get().asItem(), 1), 1, 15, 0.05f
            ));
            trades.get(3).add((entity, randomSource) -> {
                ItemStack beeContainer = new ItemStack(BeeIndustryItems.BEE_CONTAINER.get());
                beeContainer.set(BeeIndustryDataComponents.STORED_BEE_ID, ResourceLocation.fromNamespaceAndPath("beeindustry", "lumber_bee"));
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 15),
                        beeContainer, 3, 10, 0.05f);
            });

            // Level 4 (Expert)
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 32),
                    new ItemStack(BeeIndustryBlocks.BEEPOST.get().asItem(), 1), 1, 20, 0.05f
            ));
            trades.get(4).add((entity, randomSource) -> {
                ItemStack beeContainer = new ItemStack(BeeIndustryItems.BEE_CONTAINER.get());
                beeContainer.set(BeeIndustryDataComponents.STORED_BEE_ID, ResourceLocation.fromNamespaceAndPath("beeindustry", "mining_bee"));
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 20),
                        beeContainer, 2, 15, 0.05f);
            });

            // Level 5 (Master)
            trades.get(5).add((entity, randomSource) -> {
                ItemStack beeContainer = new ItemStack(BeeIndustryItems.BEE_CONTAINER.get());
                beeContainer.set(BeeIndustryDataComponents.STORED_BEE_ID, ResourceLocation.fromNamespaceAndPath("beeindustry", "light_bee"));
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 40), Optional.of(new ItemCost(Items.DIAMOND, 1)),
                        beeContainer, 1, 30, 0.05f);
            });
            trades.get(5).add((entity, randomSource) -> {
                ItemStack beeContainer = new ItemStack(BeeIndustryItems.BEE_CONTAINER.get());
                beeContainer.set(BeeIndustryDataComponents.STORED_BEE_ID, ResourceLocation.fromNamespaceAndPath("beeindustry", "rideable_bee"));
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 64), Optional.of(new ItemCost(Items.SADDLE, 1)),
                        beeContainer, 1, 30, 0.05f);
            });
        }
    }

    @SubscribeEvent
    public static void onBeeSetTarget(LivingChangeTargetEvent event) {
        if(event.getNewAboutToBeSetTarget() instanceof Player player && event.getEntity() instanceof Bee bee) {
            if(isWearingFullBeekeeperSet(player)) {
                bee.setTarget(null);
                bee.setRemainingPersistentAngerTime(0);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onBeeDeath(LivingDeathEvent event) {
        if(event.getEntity() instanceof CustomBeeEntity bee && !bee.level().isClientSide) {
            if(bee.isWorkingForMachine && bee.getHivePos() != null) {
                BlockEntity be = bee.level().getBlockEntity(bee.getHivePos());
                if(be instanceof AdvancedBeehiveBlockEntity beehive) {
                    beehive.onWorkerBeeDied(bee.getUUID());
                } else if(be instanceof BeepostBlockEntity beepost) {
                    beepost.onWorkerBeeDied(bee.getUUID());
                }
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
                AABB searchBox = player.getBoundingBox().inflate(10.0D);
                List<Bee> nearbyBees = player.level().getEntitiesOfClass(Bee.class, searchBox);
                for (Bee bee : nearbyBees) {
                    if (player.equals(bee.getTarget())) {
                        bee.setTarget(null);
                        bee.setRemainingPersistentAngerTime(0);
                    }
                }
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
