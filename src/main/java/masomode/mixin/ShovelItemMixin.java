package masomode.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ShovelItem.class)
public class ShovelItemMixin {

    @Shadow
    @Final
    protected static Map<Block, BlockState> FLATTENABLES;

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    public void useOn(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> callbackInfoReturnable) {
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);

        if (FLATTENABLES.get(blockState.getBlock()) != null) {
            callbackInfoReturnable.setReturnValue(InteractionResult.FAIL);
        }
    }
}
