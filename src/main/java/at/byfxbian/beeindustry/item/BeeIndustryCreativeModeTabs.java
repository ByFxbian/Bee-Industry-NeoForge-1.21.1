package at.byfxbian.beeindustry.item;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.component.BeeColorComponent;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BeeIndustryCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BeeIndustry.MOD_ID);

    public static final Supplier<CreativeModeTab> BEE_INDUSTRY_TAB= CREATIVE_MODE_TAB.register("beeindustry",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BeeIndustryItems.EXAMPLE_ITEM.get()))
                    .title(Component.translatable("creativetab.beeindustry"))
                    .displayItems((itemDisplayParameters, output) -> {
                      //output.accept(BeeIndustryItems.EXAMPLE_ITEM);
                      //output.accept(BeeIndustryBlocks.EXAMPLE_BLOCK);
                        output.accept(BeeIndustryItems.BEE_CONTAINER);
                        output.accept(BeeIndustryItems.BEE_SMOKER);
                        output.accept(BeeIndustryItems.APIARISTS_COMPASS);
                        output.accept(BeeIndustryItems.QUANTITY_UPGRADE);
                        output.accept(BeeIndustryItems.EFFICIENCY_UPGRADE);
                        output.accept(BeeIndustryItems.RANGE_UPGRADE);
                        output.accept(BeeIndustryItems.SWEET_HONEY);

                        output.accept(BeeIndustryItems.BEEKEEPER_HELMET);
                        output.accept(BeeIndustryItems.BEEKEEPER_CHESTPLATE);
                        output.accept(BeeIndustryItems.BEEKEEPER_LEGGINGS);
                        output.accept(BeeIndustryItems.BEEKEEPER_BOOTS);

                        output.accept(BeeIndustryBlocks.ADVANCED_BEEHIVE);
                        output.accept(BeeIndustryBlocks.BEENERGY_GENERATOR);
                        output.accept(BeeIndustryBlocks.SAP_PRESS);
                        output.accept(BeeIndustryBlocks.BEEPOST);
                        output.accept(BeeIndustryBlocks.DIRT_NEST);
                        output.accept(BeeIndustryBlocks.GRAVEL_NEST);
                        output.accept(BeeIndustryBlocks.STONE_NEST);
                        output.accept(BeeIndustryBlocks.SAND_NEST);

                      for(ResourceLocation beeId : BeeDefinitionManager.getBeeDefinitionsMap().keySet()) {
                          ItemStack eggStack = new ItemStack(BeeIndustryItems.BEE_SPAWN_EGG.get());
                          CustomBee beeData = BeeDefinitionManager.getBee(beeId);

                          if (beeData != null) {
                              CompoundTag entityTag = new CompoundTag();
                              entityTag.putString("id", BeeIndustryEntities.CUSTOM_BEE_ENTITY.getId().toString());
                              entityTag.putString("bee_type", beeId.toString());

                              eggStack.set(DataComponents.ENTITY_DATA, CustomData.of(entityTag));

                              //eggStack.set(BeeIndustryDataComponents.BEE_TYPE.get(), beeId);
                              int primaryColor = Integer.parseInt(beeData.primaryColor().substring(1), 16) | 0xFF000000;
                              int secondaryColor = Integer.parseInt(beeData.secondaryColor().substring(1), 16) | 0xFF000000;
                              eggStack.set(BeeIndustryDataComponents.BEE_COLORS.get(), new BeeColorComponent(primaryColor, secondaryColor));
                              output.accept(eggStack);
                          }
                      }
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
