package masomode.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SwellGoal.class)
public class SwellGoalMixin {

    @Shadow
    @Final
    private Creeper creeper;

    @Shadow
    private @Nullable LivingEntity target;

    @Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
    public void canUse(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (this.creeper.isPowered()) {
            LivingEntity livingEntity = this.creeper.getTarget();
            callbackInfoReturnable.setReturnValue(this.creeper.getSwellDir() > 0 || livingEntity != null && this.creeper.distanceToSqr(livingEntity) < (double)30.0F);
        }
    }
}
