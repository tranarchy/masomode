package masomode.mixin.client;

import masomode.MainClient;
import masomode.util.Common;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.Shapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    public void useItemOn(LocalPlayer localPlayer, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> callbackInfoReturnable) {
        if (blockHitResult.getType() != HitResult.Type.BLOCK)
            return;

        if (!localPlayer.onGround()) {
            BlockPos playerBlockPos = localPlayer.blockPosition();
            BlockPos placeBlockPos = blockHitResult.getBlockPos();

            if (placeBlockPos.getX() - playerBlockPos.getX() <= 1 && placeBlockPos.getZ() - playerBlockPos.getZ() <= 1) {
                if (placeBlockPos.getY() + 2 == playerBlockPos.getY()) {
                    callbackInfoReturnable.setReturnValue(InteractionResult.FAIL);
                }
            }
        }

        BlockState blockState = localPlayer.level().getBlockState(blockHitResult.getBlockPos());

        if (blockState.is(BlockTags.BEDS)) {
            if (Common.isBloodMoon()) {
                MainClient.mc.player.displayClientMessage(Component.literal("Sleeping is probably a bad idea now"), true);
                callbackInfoReturnable.setReturnValue(InteractionResult.FAIL);
            }
        } else if (blockState.is(Blocks.LAVA_CAULDRON)) {
            callbackInfoReturnable.setReturnValue(InteractionResult.FAIL);
        } else if (blockState.getCollisionShape(localPlayer.level(), blockHitResult.getBlockPos()) == Shapes.empty() && !blockState.canBeReplaced()) {
            if (MainClient.mc.player.getItemInHand(interactionHand).getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof Fallable) {
                    callbackInfoReturnable.setReturnValue(InteractionResult.FAIL);
                }
            }
        }
    }
}
