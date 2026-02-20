package masomode.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(method = "burnUndead", at = @At("HEAD"), cancellable = true)
    private void burnUndead(CallbackInfo callbackInfo) {
        Mob mob = ((Mob) (Object)this);

        if (mob instanceof AbstractSkeleton || mob instanceof ZombieVillager) {
            callbackInfo.cancel();
        }
    }

    @ModifyReturnValue(method = "createMobAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder createMobAttributes(AttributeSupplier.Builder builder) {
        return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, (double)50.0F);
    }

}
