package masomode.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlock.class)
public class LiquidBlockMixin {
    @Shadow
    @Final
    protected FlowingFluid fluid;

    @Inject(method = "pickupBlock", at = @At("HEAD"), cancellable = true)
    public void pickupBlock(@Nullable LivingEntity livingEntity, LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, CallbackInfoReturnable<ItemStack> callbackInfoReturnable) {
        if (fluid == Fluids.LAVA) {
            callbackInfoReturnable.setReturnValue(ItemStack.EMPTY);
        }
    }
}
