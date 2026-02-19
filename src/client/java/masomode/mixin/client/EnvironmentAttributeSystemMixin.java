package masomode.mixin.client;

import masomode.util.Common;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.SpatialAttributeInterpolator;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnvironmentAttributeSystem.class)
public class EnvironmentAttributeSystemMixin {
    @Inject(method = "getValue", at = @At("RETURN"), cancellable = true)
    public <Value> Value getValue(EnvironmentAttribute<Value> environmentAttribute, Vec3 vec3, @Nullable SpatialAttributeInterpolator spatialAttributeInterpolator, CallbackInfoReturnable<Value> callbackInfoReturnable) {
        if (environmentAttribute == EnvironmentAttributes.SKY_COLOR || environmentAttribute == EnvironmentAttributes.SKY_LIGHT_COLOR || environmentAttribute == EnvironmentAttributes.FOG_COLOR || environmentAttribute == EnvironmentAttributes.WATER_FOG_COLOR) {
            if (Common.isBloodMoon()) {
                callbackInfoReturnable.setReturnValue((Value) Integer.valueOf(0xe3142a));
            }
        }

        return callbackInfoReturnable.getReturnValue();
    }
}
