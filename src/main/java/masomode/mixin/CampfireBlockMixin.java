package masomode.mixin;

import masomode.utils.Common;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin extends Block {
    public CampfireBlockMixin(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Unique
    private int tickToDestroy = 0;

    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    public void getStateForPlacement(BlockPlaceContext blockPlaceContext, CallbackInfoReturnable<BlockState> callbackInfoReturnable) {
       callbackInfoReturnable.setReturnValue(callbackInfoReturnable.getReturnValue().setValue(CampfireBlock.LIT, false));
    }

    @Override
    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (blockState.getValue(CampfireBlock.LIT)) {
            Common.setNeighborBlocksOnFire(serverLevel, blockPos);

            tickToDestroy++;
            if (tickToDestroy >= 10) {
                serverLevel.levelEvent(null, 1009, blockPos, 0);
                CampfireBlock.dowse(null, serverLevel, blockPos, blockState);
                serverLevel.setBlockAndUpdate(blockPos, Blocks.CAMPFIRE.withPropertiesOf(blockState).setValue(CampfireBlock.LIT, false));
                tickToDestroy = 0;
            }
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState blockState) {
        return true;
    }
}
