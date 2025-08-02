package at.byfxbian.beeindustry.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.biome.Biome;

public record LureRecipe(Ingredient lureItem, TagKey<Biome> biomeTag, ResourceLocation bee, int time) {
    public static final Codec<LureRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC.fieldOf("lureItem").forGetter(LureRecipe::lureItem),
            TagKey.codec(Registries.BIOME).fieldOf("biomeTag").forGetter(LureRecipe::biomeTag),
            ResourceLocation.CODEC.fieldOf("bee").forGetter(LureRecipe::bee),
            Codec.INT.fieldOf("time").forGetter(LureRecipe::time)
    ).apply(instance, LureRecipe::new));
}
