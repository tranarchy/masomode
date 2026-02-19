package masomode.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "canRide", at = @At("RETURN"), cancellable = true)
    protected void canRide(Entity entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        Entity passenger = ((Entity) (Object)this);

        if (entity instanceof Boat) {
            if (!(passenger instanceof ServerPlayer)) {
                callbackInfoReturnable.setReturnValue(false);
            }
        }
    }
}
