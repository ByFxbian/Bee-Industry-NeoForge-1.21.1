package at.byfxbian.beeindustry.command;

import at.byfxbian.beeindustry.block.custom.BaseNestBlock;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import java.util.Optional;

public class LocateNestCommand {
    public static final TagKey<PoiType> BEE_NESTS_TAG =
            TagKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.fromNamespaceAndPath("beeindustry", "bee_nests"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("locatenest")
                .requires(source -> source.hasPermission(2))
                .executes(LocateNestCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> context){
        ServerPlayer player = context.getSource().getPlayer();
        if(player == null) {
            context.getSource().sendFailure(Component.literal("Command must be run by a player."));
            return 0;
        }

        ServerLevel serverLevel = player.serverLevel();
        BlockPos playerPos = player.blockPosition();

        Optional<BlockPos> nearestNestPos = serverLevel.getPoiManager().findClosest(
                poiType -> poiType.is(BEE_NESTS_TAG),
                playerPos,
                512,
                PoiManager.Occupancy.ANY
        );
        nearestNestPos.ifPresentOrElse(
                pos -> {
                    player.sendSystemMessage(Component.literal("Nearest nest found at: " + pos.getX() + ", " + pos.getY() + ", " +pos.getZ()));
                    player.sendSystemMessage(Component.literal(
                            "/tp " + player.getName().getString() + " " + pos.getX() + " " + pos.getY() + " " + pos.getZ()
                    ));
                },
                () -> player.sendSystemMessage(Component.literal("No nests found within a large distance."))
        );

        return 1;
    }
}
