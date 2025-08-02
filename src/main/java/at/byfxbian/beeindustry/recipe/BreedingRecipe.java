package at.byfxbian.beeindustry.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public record BreedingRecipe(ResourceLocation parentA, ResourceLocation parentB, List<Outcome> outcomes){

    public record Outcome(ResourceLocation child, float chance) {
        public static final Codec<Outcome> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("child").forGetter(Outcome::child),
                Codec.FLOAT.fieldOf("chance").forGetter(Outcome::chance)
        ).apply(instance, Outcome::new));
    }

    public static final Codec<BreedingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("parentA").forGetter(BreedingRecipe::parentA),
            ResourceLocation.CODEC.fieldOf("parentB").forGetter(BreedingRecipe::parentB),
            Outcome.CODEC.listOf().fieldOf("outcomes").forGetter(BreedingRecipe::outcomes)
    ).apply(instance, BreedingRecipe::new));
}
