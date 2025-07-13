package at.byfxbian.beeindustry.entity.custom;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.recipe.BreedingRecipe;
import at.byfxbian.beeindustry.recipe.BreedingRecipeManager;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import at.byfxbian.beeindustry.util.BeeRegistries;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CustomBeeEntity extends Bee {

    public CustomBeeEntity(EntityType<? extends Bee> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Bee.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FLYING_SPEED, 0.6)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnGroupData instanceof CustomBeeSpawnData customData) {
            this.setBeeType(customData.beeId());
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public void setBeeType(ResourceLocation beeId) {
        this.set(BeeIndustryDataComponents.BEE_TYPE, beeId);
        CustomBee beeData = BeeDefinitionManager.getBee(beeId);
        if (beeData != null) {
            applyBeeData(beeData);
        }
    }

    private void applyBeeData(CustomBee data) {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(data.attributes().maxHealth());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(data.attributes().attackDamage());
        this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(data.attributes().speed() / 10.0);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(data.attributes().speed() / 15.0);
        this.setHealth(this.getMaxHealth());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BeeIndustryDataComponents.BEE_TYPE.getHolder(), Optional.empty());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("bee_type")) {
            setBeeType(ResourceLocation.parse(compound.getString("bee_type")));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.get(BeeIndustryDataComponents.BEE_TYPE).ifPresent(type -> compound.putString("bee_type", type.toString()));
    }

    public record CustomBeeSpawnData(ResourceLocation beeId) implements SpawnGroupData {
    }

    @Nullable
    @Override
    public Bee getBreedOffspring(ServerLevel level, AgeableMob other) {
        return null;
    }
}
