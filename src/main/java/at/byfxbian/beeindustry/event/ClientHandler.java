package at.byfxbian.beeindustry.event;

import at.byfxbian.beeindustry.item.BeeIndustryItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ClientHandler {
    private static BlockPos targetNestPos = null;
    private static int ticksLeft = 0;

    public static void handleNestFound(BlockPos pos) {
        targetNestPos = pos;
        ticksLeft = 200;
    }

    public static void onClientTick() {
        if(ticksLeft > 0 && targetNestPos != null) {
            ticksLeft--;
            Player player = Minecraft.getInstance().player;
            if(player != null && (player.getMainHandItem().is(BeeIndustryItems.APIARISTS_COMPASS.get()) || player.getOffhandItem().is(BeeIndustryItems.APIARISTS_COMPASS))) {
                Vec3 playerLookVec = player.getViewVector(1.0F);
                Vec3 toNestVec = Vec3.atCenterOf(targetNestPos).subtract(player.getEyePosition()).normalize();

                double dotProduct = playerLookVec.dot(toNestVec);

                if(dotProduct > 0.95) {
                    if(player.level().random.nextInt(4) == 0) {
                        player.level().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + playerLookVec.x, player.getEyeY(), player.getZ() + playerLookVec.z, 0, 0, 0);
                    }
                    if (ticksLeft % 10 == 0) {
                        player.level().playSound(player, player.blockPosition(), SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.PLAYERS, 0.5f, 1.5f);
                    }
                } else if (dotProduct > 0.7) {
                    if(player.level().random.nextInt(8) == 0) {
                        player.level().addParticle(ParticleTypes.NOTE, player.getX() + playerLookVec.x * 0.8, player.getEyeY(), player.getZ() + playerLookVec.z * 0.8, 0, 0, 0);
                    }
                    if(ticksLeft % 20 == 0) {
                        player.level().playSound(player, player.blockPosition(), SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.PLAYERS, 0.3f, 1.0f);
                    }
                }
            }
        } else {
            targetNestPos = null;
        }
    }
}
