package masomode.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.sensing.Sensing;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sensing.class)
public class SensingMixin {

    @Shadow
    @Final
    private Mob mob;

    @Inject(method = "hasLineOfSight", at = @At("RETURN"), cancellable = true)
    public void hasLineOfSight(Entity entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (mob instanceof Creeper || mob instanceof Zombie || mob instanceof Spider) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }
}
