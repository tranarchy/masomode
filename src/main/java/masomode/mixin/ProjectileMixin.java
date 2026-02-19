package masomode.mixin;

import masomode.utils.Common;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class ProjectileMixin {
    @Inject(method = "onHitBlock", at = @At("HEAD"))
    protected void onHitBlock(BlockHitResult blockHitResult, CallbackInfo info) {
        Projectile projectile = ((Projectile) (Object)this);

        if (!projectile.level().isClientSide()) {
            if (projectile.getRemainingFireTicks() != 0) {
                Common.setNeighborBlocksOnFire((ServerLevel) projectile.level(), blockHitResult.getBlockPos());
            }
        }
    }
}
