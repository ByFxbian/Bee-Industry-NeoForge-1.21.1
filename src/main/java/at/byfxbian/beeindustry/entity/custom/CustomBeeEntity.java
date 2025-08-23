package at.byfxbian.beeindustry.entity.custom;

import at.byfxbian.beeindustry.BeeIndustry;
import at.byfxbian.beeindustry.api.CustomBee;
import at.byfxbian.beeindustry.block.entity.custom.AdvancedBeehiveBlockEntity;
import at.byfxbian.beeindustry.block.entity.custom.BeepostBlockEntity;
import at.byfxbian.beeindustry.component.BeeColorComponent;
import at.byfxbian.beeindustry.component.BeeIndustryDataComponents;
import at.byfxbian.beeindustry.entity.BeeIndustryEntities;
import at.byfxbian.beeindustry.entity.goal.*;
import at.byfxbian.beeindustry.item.BeeIndustryItems;
import at.byfxbian.beeindustry.recipe.BreedingRecipe;
import at.byfxbian.beeindustry.recipe.BreedingRecipeManager;
import at.byfxbian.beeindustry.util.BeeDefinitionManager;
import at.byfxbian.beeindustry.util.BeeRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CustomBeeEntity extends Bee {

    private static final EntityDataAccessor<String> BEE_TYPE_ID =
            SynchedEntityData.defineId(CustomBeeEntity.class, EntityDataSerializers.STRING);

    public boolean hasWorked = false;
    @Nullable
    public BlockPos productionBlockPos = null;

    @Nullable
    private BlockPos hivePos;

    @Nullable
    private BlockPos lastLightPos = null;

    protected FollowParentGoal followParentGoal;
    protected BreedGoal breedGoal;

    public int workRange = 10;
    public boolean isWorkingForMachine = false;

    public float workSpeedModifier = 1.0f;
    public int bonusQuantity = 0;

    @Nullable private BlockPos assignedHivePos;
    private boolean isWorking;
    private boolean needsPostReloadReturn;
    private boolean returningHomeAfterReload;

    private static final String NBT_HIVE_POS = "HivePos";
    private static final String NBT_IS_WORKING = "IsWorking";

    private boolean dimensionsSyncedOnce = false;

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);

    @Override
    public void tick() {
        super.tick();
        if(!level().isClientSide) {
            if(!dimensionsSyncedOnce && isRideableBee()) {
                this.refreshDimensions();
                dimensionsSyncedOnce = true;
            }
            if(needsPostReloadReturn) {
                needsPostReloadReturn = false;

                if (this.hivePos != null
                        && (this.level().getBlockEntity(this.hivePos) instanceof AdvancedBeehiveBlockEntity)
                        && this.isWorkingForMachine) {
                    goToHive();
                    returningHomeAfterReload = true;
                }
            }

            if(this.returningHomeAfterReload) {
                if(hivePos == null) {
                    this.returningHomeAfterReload = false;
                } else if (arrivedAt(hivePos)) {
                    BlockEntity be = level().getBlockEntity(hivePos);
                    if(be instanceof AdvancedBeehiveBlockEntity hive) {
                        BeeIndustry.LOGGER.debug("RETURNING HOME AFTER RELOAD ADVANCED BEEHIVE");
                        hive.onWorkedBeeReturned(this);
                    }
                    this.returningHomeAfterReload = false;
                } else if (this.getNavigation().isDone()) {
                    goToHive();
                }
            }
        }

        if(!this.level().isClientSide && this.getBeeType().getPath().equals("light_bee")) {
            BlockPos currentPos = this.blockPosition();
            if(this.lastLightPos != null && !this.lastLightPos.equals(currentPos)) {
                if(this.level().getBlockState(this.lastLightPos).is(Blocks.LIGHT)) {
                    this.level().setBlock(this.lastLightPos, Blocks.AIR.defaultBlockState(), 3);
                }
                this.lastLightPos = null;
            }

            if(this.lastLightPos == null && this.level().getBlockState(currentPos).isAir()) {
                this.level().setBlock(currentPos, Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 15), 3);
                this.lastLightPos = currentPos;
            }
        }
    }

    private boolean isRideableBee() {
        ResourceLocation id = this.getBeeType();
        return id != null && "rideable_bee".equals(id.getPath());
    }

    public void setAssignedHivePos(BlockPos pos) { this.assignedHivePos = pos.immutable(); this.hivePos = this.assignedHivePos; }
    public void setWorking(boolean working) { this.isWorking = working; }

    private boolean arrivedAt(BlockPos p) {
        return this.distanceToSqr(p.getX() + 0.5, p.getY() + 1, p.getZ() + 0.5) <= 2.25;
    }

    private void goToHive() {
        BlockPos p = hivePos;
        this.getNavigation().moveTo(p.getX() + 0.5, p.getY() + 1, p.getZ() + 0.5, 1.0);
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        if(!this.level().isClientSide && this.lastLightPos != null && this.getBeeType().getPath().equals("light_bee")) {
            if(this.level().getBlockState(this.lastLightPos).is(Blocks.LIGHT)) {
                this.level().setBlock(this.lastLightPos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
        super.remove(reason);
    }

    public NonNullList<ItemStack> getInventory() {
        return this.inventory;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!this.level().isClientSide && this.isAlive() && this.getBeeType().getPath().equals("ender_bee")) {
            if(!(source.is(DamageTypes.ARROW) || source.getDirectEntity() instanceof Player)) {
                return super.hurt(source, amount);
            }

            boolean teleported = this.teleportAway();
            boolean tookDamage = super.hurt(source, 0);

            return teleported || tookDamage;
        }

        return super.hurt(source, amount);
    }

    private boolean teleportAway() {
        if(this.level().isClientSide() || !this.isAlive()) {
            return false;
        }

        double d0 = this.getX() + (this.random.nextDouble() - 0.5) * 32.0;
        double d1 = this.getY() + (double)(this.random.nextInt(16) - 8);
        double d2 = this.getZ() + (this.random.nextDouble() - 0.5) * 32.0;

        return this.randomTeleport(d0, d1, d2, true);
    }

    public void addItemStack(ItemStack stack) {
        for (int i = 0; i < this.inventory.size(); i++) {
            if (this.inventory.get(i).isEmpty()) {
                this.inventory.set(i, stack.copy());
                return;
            }
        }
    }

    public Optional<CustomBee> getBeeData() {
        return Optional.ofNullable(BeeDefinitionManager.getBee(this.getBeeType()));
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        ItemStack eggStack = new ItemStack(BeeIndustryItems.BEE_SPAWN_EGG.get());

        CompoundTag entityTag = new CompoundTag();
        entityTag.putString("id", BeeIndustryEntities.CUSTOM_BEE_ENTITY.getId().toString());
        entityTag.putString("bee_type", this.getBeeType().toString());

        eggStack.set(DataComponents.ENTITY_DATA, CustomData.of(entityTag));
        int primaryColor = Integer.parseInt(getBeeData().get().primaryColor().substring(1), 16) | 0xFF000000;
        int secondaryColor = Integer.parseInt(getBeeData().get().secondaryColor().substring(1), 16) | 0xFF000000;
        eggStack.set(BeeIndustryDataComponents.BEE_COLORS.get(), new BeeColorComponent(primaryColor, secondaryColor));

        return eggStack;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(this.getBeeType().getPath().equals("rideable_bee") && !this.isVehicle() && !player.isSecondaryUseActive() && !this.isBaby()) {
            if(!this.level().isClientSide) {
                player.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if(this.isVehicle() && getControllingPassenger() instanceof Player passenger) {
            this.setYRot(passenger.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(passenger.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.yBodyRot;

            float sideways = passenger.xxa * 0.5F; // Links/Rechts (A/D)
            float forwards = passenger.zza;       // Vorwärts/Rückwärts (W/S)

            if (forwards <= 0.0F) {
                forwards *= 0.25F;
            }

            double verticalMovement = 0.0;
            if (passenger.jumping) { // "jumping" wird durch die Leertaste gesetzt
                verticalMovement = 0.5D; // Nach oben
            } else {
                if(passenger.getViewVector(1.0F).y < -0.5) {
                    verticalMovement = -0.5D; // Nach unten
                }
            }

            float speed = (float)this.getAttributeValue(Attributes.FLYING_SPEED);
            this.setSpeed(speed);

            super.travel(new Vec3(sideways, verticalMovement, forwards));
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        return new Vec3(0.0D, dimensions.height() * 1.0D, 0.0D);
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        Entity passenger = this.getFirstPassenger();
        if (passenger instanceof LivingEntity livingEntity) {
            return livingEntity;
        }
        return null;
    }

    public void clearInventory() {
        this.inventory.clear();
    }

    public CustomBeeEntity(EntityType<? extends Bee> entityType, Level level) {
        super(entityType, level);
        this.goalSelector.addGoal(3, new ProductiveTemptGoal(this, 1.25D));
        this.setPathfindingMalus(PathType.TRAPDOOR, -1.0F);
        this.setInvulnerable(isInvulnerable());
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
        this.entityData.set(BEE_TYPE_ID, beeId.toString());
        CustomBee beeData = BeeDefinitionManager.getBee(beeId);
        if (beeData != null) {
            applyBeeData(beeData);
        }

        this.refreshDimensions();
        this.dimensionsSyncedOnce = false;
    }

    public ResourceLocation getBeeType() {
        return ResourceLocation.parse(this.entityData.get(BEE_TYPE_ID));
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
        builder.define(BEE_TYPE_ID, "beeindustry:example_bee");
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("bee_type")) {
            setBeeType(ResourceLocation.parse(compound.getString("bee_type")));
        }
        NbtUtils.readBlockPos(compound, "hive_pos").ifPresent(pos -> this.hivePos = pos);
        NbtUtils.readBlockPos(compound, "production_block_pos").ifPresent(pos -> this.productionBlockPos = pos);
        this.hasWorked = compound.getBoolean("hasWorked");
        /*if (compound.contains("Inventory")) {
            ListTag nbtList = compound.getList("Inventory", 10);
            for(int i = 0; i < nbtList.size(); i++) {
                this.inventory.set(i, ItemStack.parse(this.registryAccess(), nbtList.getCompound(i)).orElse(ItemStack.EMPTY));
            }
        }*/
        this.inventory.clear();
        ContainerHelper.loadAllItems(compound, this.inventory, this.registryAccess());
        this.workRange = compound.getInt("workRange");
        this.workSpeedModifier = compound.getFloat("workSpeedModifier");
        this.bonusQuantity = compound.getInt("bonusQuantity");
        //if (compound.contains(NBT_HIVE_POS)) assignedHivePos = NbtUtils.readBlockPos(compound.getCompound(NBT_HIVE_POS), NBT_HIVE_POS).get();
        isWorking = compound.getBoolean(NBT_IS_WORKING);
        this.isWorkingForMachine = compound.getBoolean("isWorkingForMachine");
        this.needsPostReloadReturn = true;
        if(this.hivePos == null && this.assignedHivePos != null) { this.hivePos = this.assignedHivePos; } else if (this.assignedHivePos == null && this.hivePos != null) { this.assignedHivePos = this.hivePos; } this.registerGoals();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("bee_type", this.getBeeType().toString());
        if (this.hivePos != null) {
            compound.put("hive_pos", NbtUtils.writeBlockPos(this.hivePos));
        }
        if (this.productionBlockPos != null) {
            compound.put("production_block_pos", NbtUtils.writeBlockPos(this.productionBlockPos));
        }
        compound.putBoolean("hasWorked", this.hasWorked);
        /*ListTag nbtList = new ListTag();
        for(ItemStack itemStack : this.inventory) {
            nbtList.add(itemStack.save(this.registryAccess()));
        }
        compound.put("Inventory", nbtList);*/
        ContainerHelper.saveAllItems(compound, this.inventory, this.registryAccess());
        compound.putInt("workRange", this.workRange);
        compound.putFloat("workSpeedModifier", this.workSpeedModifier);
        compound.putInt("bonusQuantity", this.bonusQuantity);
        //if (assignedHivePos != null) compound.put(NBT_HIVE_POS, NbtUtils.writeBlockPos(assignedHivePos));
        compound.putBoolean(NBT_IS_WORKING, isWorking);
        compound.putBoolean("isWorkingForMachine", this.isWorkingForMachine);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.BEE_FOOD);
    }

    @Override
    public boolean canMate(Animal otherAnimal) {
        if(otherAnimal == this) {
            return false;
        }
        if(!(otherAnimal instanceof CustomBeeEntity)) {
            return false;
        }
        Optional<BreedingRecipe> recipe = BreedingRecipeManager.getRecipeFor(this.getBeeType(), ((CustomBeeEntity) otherAnimal).getBeeType());
        return recipe.isPresent();
    }

    @Nullable
    @Override
    public Bee getBreedOffspring(ServerLevel level, AgeableMob other) {
        if(!(other instanceof CustomBeeEntity otherBee)) {
            return null;
        }

        Optional<BreedingRecipe> recipeOptional = BreedingRecipeManager.getRecipeFor(this.getBeeType(), otherBee.getBeeType());

        if(recipeOptional.isPresent()) {
            BreedingRecipe recipe = recipeOptional.get();

            for(BreedingRecipe.Outcome outcome : recipe.outcomes()) {
                if(level.random.nextFloat() < outcome.chance()) {
                    CustomBeeEntity child = BeeIndustryEntities.CUSTOM_BEE_ENTITY.get().create(level);
                    if(child != null) {
                        child.setBeeType(outcome.child());
                        return child;
                    }
                }
            }
        }

        CustomBeeEntity child = BeeIndustryEntities.CUSTOM_BEE_ENTITY.get().create(level);
        if(child != null) {
            child.setBeeType(level.random.nextBoolean() ? this.getBeeType() : otherBee.getBeeType());
            return child;
        }

        return null;
    }

    @Override
    public boolean fireImmune() {
        return getBeeData().map(CustomBee::fireproof).orElse(false);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        ResourceLocation id = this.getBeeType();
        if (id != null && "rideable_bee".equals(id.getPath())) {
            return EntityDimensions.scalable(1.5F, 1.4F);
        }
        return super.getDimensions(pPose);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        boolean isBeeInvulnerable = getBeeData().map(CustomBee::invulnerable).orElse(false);
        if(isWorkingForMachine && (source.is(DamageTypeTags.IS_DROWNING) || source.is(DamageTypeTags.IS_FIRE))) {
            return true;
        }
        return isBeeInvulnerable || super.isInvulnerableTo(source);
    }

    @Override
    public void registerGoals() {
        /*registerBasicGoals();

        this.beePollinateGoal = new CustomBeeEntity.PollinateGoal();
        this.goalSelector.addGoal(4, this.beePollinateGoal);

        this.goToKnownFlowerGoal = new Bee.BeeGoToKnownFlowerGoal();
        this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);*/

        this.goalSelector.removeAllGoals(goal -> true);
        this.targetSelector.removeAllGoals(goal -> true);

        this.goalSelector.addGoal(0, new BeeAggressiveGoal());
        this.goalSelector.addGoal(0, new BeeAttackGoal(this, 1.4D, true));

        this.targetSelector.addGoal(1, (new Bee.BeeHurtByOtherGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new Bee.BeeBecomeAngryTargetGoal(this));

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this)); // Sich umschauen
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D)); // Zucht-Verhalten
        //this.goalSelector.addGoal(3, new ProductiveTemptGoal(this, 1.25D)); // Spieler mit Blumen folgen
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(8, new BetterBeeWanderGoal()); // Dein verbessertes Herumfliegen-Goal

        if(this.getHivePos() != null) {
            //System.out.println("BIENE HAT ZUHAUSE ------------");
            this.goalSelector.addGoal(1, new FleeOnLowHealthGoal(this));
            BlockEntity hive = this.level().getBlockEntity(this.getHivePos());
            if (hive instanceof BeepostBlockEntity) {
               // System.out.println("ZUHAUSE IST BEEPOST ------------");
                if (this.getBeeType().getPath().contains("mining")) {
                    this.goalSelector.addGoal(1, new MiningGoal(this));
                    this.targetSelector.addGoal(1, new BeeHurtByOtherGoal(this));
                    this.targetSelector.addGoal(2, new BeeBecomeAngryTargetGoal(this));
                } else if (this.getBeeType().getPath().contains("farming")) {
                    this.goalSelector.addGoal(1, new FarmingGoal(this));
                    this.targetSelector.addGoal(1, new BeeHurtByOtherGoal(this));
                    this.targetSelector.addGoal(2, new BeeBecomeAngryTargetGoal(this));
                } else if (this.getBeeType().getPath().contains("fighting")) {
                    this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
                    this.goalSelector.addGoal(2, new ReturnHomeGoal(this));
                    //this.goalSelector.addGoal(1, new FightingGoal(this));
                    this.targetSelector.addGoal(1, new FindEnemyGoal(this));
                } else if (this.getBeeType().getPath().contains("lumber")) {
                    //System.out.println("BIENE IST LUMBER ------------");
                    this.goalSelector.addGoal(1, new CollectSapGoal(this));
                    this.targetSelector.addGoal(1, new BeeHurtByOtherGoal(this));
                    this.targetSelector.addGoal(2, new BeeBecomeAngryTargetGoal(this));
                }
            } else if(hive instanceof AdvancedBeehiveBlockEntity) {
                this.targetSelector.addGoal(1, new BeeHurtByOtherGoal(this));
                this.targetSelector.addGoal(2, new BeeBecomeAngryTargetGoal(this));
                CustomBee beeData = BeeDefinitionManager.getBee(this.getBeeType());
                if (beeData != null) {
                    // DIES IST EINE ARBEITSBIENE (z.B. Eisen-Biene)
                    this.goalSelector.addGoal(1, new GoToProductionBlockGoal(this));
                    this.goalSelector.addGoal(2, new ReturnToHiveGoal(this));

                    this.beePollinateGoal = new EmptyPollinateGoal();
                    this.goToKnownFlowerGoal = new EmptyFindFlowerGoal();
                    this.goToHiveGoal = new EmptyHiveGoal();
                } else {
                    // DIES IST EINE HONIGBIENE (Standard-Verhalten)
                    this.beePollinateGoal = new PollinateGoal();
                    this.goToKnownFlowerGoal = new GoToKnownFlowerGoal();
                    this.goToHiveGoal = new GoToHiveGoal();

                    this.goalSelector.addGoal(4, this.beePollinateGoal);
                    this.goalSelector.addGoal(5, new BeeEnterHiveGoal());
                    this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
                    this.goalSelector.addGoal(7, this.goToHiveGoal);
                }
            }
        } else {
            this.targetSelector.addGoal(1, new BeeHurtByOtherGoal(this));
            this.targetSelector.addGoal(2, new BeeBecomeAngryTargetGoal(this));

            this.beePollinateGoal = new PollinateGoal();
            this.goToKnownFlowerGoal = new GoToKnownFlowerGoal();
            this.goToHiveGoal = new GoToHiveGoal();

            this.goalSelector.addGoal(4, this.beePollinateGoal);
            this.goalSelector.addGoal(5, new BeeEnterHiveGoal());
            this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
            this.goalSelector.addGoal(7, this.goToHiveGoal);
        }
    }

    /*protected void registerBasicGoals() {
        this.goalSelector.addGoal(0, new BeeAggressiveGoal());
        this.goalSelector.addGoal(0, new BeeAttackGoal(this, 1.4D, true));

        this.breedGoal = new BreedGoal(this, 1.0D, CustomBeeEntity.class);
        this.goalSelector.addGoal(2, this.breedGoal);

        this.followParentGoal = new FollowParentGoal(this, 1.25D);
        this.goalSelector.addGoal(5, this.followParentGoal);

        this.goalSelector.addGoal(8, new BetterBeeWanderGoal());

        this.goalSelector.addGoal(9, new FloatGoal(this));

        this.targetSelector.addGoal(1, (new Bee.BeeHurtByOtherGoal(this)).setAlertOthers());

        this.targetSelector.addGoal(2, new Bee.BeeBecomeAngryTargetGoal(this));

        this.beePollinateGoal = new EmptyPollinateGoal();
        this.goToKnownFlowerGoal = new EmptyFindFlowerGoal();
    }*/

    public class GoToHiveGoal extends Bee.BeeGoToHiveGoal {
        public GoToHiveGoal() {
            super();
        }

        @Override
        public boolean canUse() {
            CustomBee beeData = BeeDefinitionManager.getBee(getBeeType());
            if (beeData != null && !beeData.createNectar()) {
                return false;
            }
            return super.canUse();
        }
    }

    public class GoToKnownFlowerGoal extends Bee.BeeGoToKnownFlowerGoal {
        public GoToKnownFlowerGoal() {
            super();
        }
        @Override
        public boolean canUse() {
            CustomBee beeData = BeeDefinitionManager.getBee(getBeeType());
            if (beeData != null && !beeData.createNectar()) {
                return false;
            }
            return super.canUse();
        }
    }

    public class PollinateGoal extends Bee.BeePollinateGoal {
        public PollinateGoal() {
            super();
        }
        @Override
        public boolean canUse() {
            CustomBee beeData = BeeDefinitionManager.getBee(getBeeType());
            if (beeData != null && !beeData.createNectar()) {
                return false;
            }
            return super.canUse();
        }

        @Override
        public boolean canBeeUse() {
            return super.canBeeUse();
        }

        @Override
        public boolean canBeeContinueToUse() {
            return super.canBeeContinueToUse();
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        public void stop() {
            super.stop();
        }

        @Override
        public Optional<BlockPos> findNearbyFlower() {
            return super.findNearbyFlower();
        }


    }

    public class BeeAttackGoal extends MeleeAttackGoal
    {
        BeeAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }

        public boolean canUse() {
            return super.canUse() && CustomBeeEntity.this.isAngry() && !CustomBeeEntity.this.hasStung();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && CustomBeeEntity.this.isAngry() && !CustomBeeEntity.this.hasStung();
        }
    }

    public class BeeAggressiveGoal extends Bee.BaseBeeGoal
    {
        BeeAggressiveGoal() {}

        @Override
        public void tick() {
            List<Player> players = level().getEntitiesOfClass(Player.class, new AABB(CustomBeeEntity.this.blockPosition()).inflate(10, 5, 10));
            if (!players.isEmpty()) {
                CustomBeeEntity.this.setTarget(players.getFirst());
            }
        }

        public boolean canBeeUse() {
            return !CustomBeeEntity.this.isAngry() && !CustomBeeEntity.this.hasStung();
        }

        public boolean canBeeContinueToUse() {
            return canUse();
        }
    }

    public class ProductiveTemptGoal extends TemptGoal
    {
        public ProductiveTemptGoal(PathfinderMob entity, double speed) {
            super(entity, speed, Ingredient.of(ItemTags.BEE_FOOD), false);
        }
    }

    public class BetterBeeWanderGoal extends Bee.BeeWanderGoal {
        public BetterBeeWanderGoal() {
            super();
        }

        public boolean canUse() {
            // Trigger if the bee gets too far from its hive, it will engage it to return
            return super.canUse() || (CustomBeeEntity.this.hivePos != null && !CustomBeeEntity.this.closerThan(CustomBeeEntity.this, 22));
        }
    }

    public class EmptyPollinateGoal extends PollinateGoal
    {
        @Override
        public boolean canBeeUse() {
            return false;
        }
    }

    public class EmptyFindFlowerGoal extends BeeGoToKnownFlowerGoal
    {
        @Override
        public boolean canBeeUse() {
            return false;
        }
    }

    public class EmptyHiveGoal extends GoToHiveGoal {
        @Override
        public boolean canUse() {
            return false;
        }
    }


    public boolean getHasWorked() { return this.hasWorked; }
    public void setHasWorked(boolean hasWorked) { this.hasWorked = hasWorked; }
    @Nullable
    public BlockPos getProductionBlockPos() { return this.productionBlockPos; }
    public void setProductionBlockPos(@Nullable BlockPos pos) { this.productionBlockPos = pos; }
    @Nullable
    public BlockPos getHivePos() { return this.hivePos; }
    public void setHivePos(@Nullable BlockPos pos) { this.hivePos = pos; }

    public record CustomBeeSpawnData(ResourceLocation beeId) implements SpawnGroupData {
    }


}
