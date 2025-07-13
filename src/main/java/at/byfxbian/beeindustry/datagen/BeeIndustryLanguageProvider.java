package at.byfxbian.beeindustry.datagen;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.block.BeeIndustryBlocks;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.text.WordUtils;

public class BeeIndustryLanguageProvider extends LanguageProvider {

    public BeeIndustryLanguageProvider(PackOutput output,  String locale) {
        super(output, BeeIndustry.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add(BeeIndustryBlocks.EXAMPLE_BLOCK.get(), "Example Block");
        add(BeeIndustryItems.EXAMPLE_ITEM.get(), "Example Item");



        add("creativetab.beeindustry.example_tab", "Bee Industry");
    }

    private String snakeCaseToTitleCase(String snakeCase) {
        return WordUtils.capitalizeFully(snakeCase.replace('_', ' '));
    }
}
