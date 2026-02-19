package masomode.mixin.client;

import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {

    @Inject(method = "ambientLight", at = @At("HEAD"), cancellable = true)
    public void ambientLight(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(-0.1F);
    }
}
