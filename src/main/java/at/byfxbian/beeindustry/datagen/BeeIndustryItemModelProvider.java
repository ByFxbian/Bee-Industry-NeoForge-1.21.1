package at.byfxbian.beeindustry.datagen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BeeIndustryItemModelProvider extends ItemModelProvider {

    public BeeIndustryItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BeeIndustry.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(BeeIndustryItems.EXAMPLE_ITEM.get());
    }
}
