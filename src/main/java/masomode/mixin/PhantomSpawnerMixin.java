package masomode.mixin;

import masomode.utils.Common;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(ServerLevel serverLevel, boolean bl, CallbackInfo callbackInfo) {
        if (!Common.isBloodMoon(serverLevel)) {
            callbackInfo.cancel();
        }
    }
}
