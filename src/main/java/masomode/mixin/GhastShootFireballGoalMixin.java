package masomode.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.entity.monster.Ghast$GhastShootFireballGoal")
public abstract class GhastShootFireballGoalMixin {

    @Shadow
    public int chargeTime;

    @Shadow
    @Final
    private Ghast ghast;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo callbackInfo) {
        LivingEntity livingEntity = ghast.getTarget();
        if (livingEntity != null) {
            double d = 64.0;
            if (livingEntity.distanceToSqr(ghast) < 4096.0) {
                Level level = ghast.level();
                this.chargeTime++;
                if (this.chargeTime == 10 && !ghast.isSilent()) {
                    level.levelEvent(null, 1015, ghast.blockPosition(), 0);
                }

                if (this.chargeTime == 20) {
                    double e = 4.0;
                    Vec3 vec3 = ghast.getViewVector(1.0F);
                    double f = livingEntity.getX() - (ghast.getX() + vec3.x * 4.0);
                    double g = livingEntity.getY(0.5) - (0.5 + this.ghast.getY(0.5));
                    double h = livingEntity.getZ() - (ghast.getZ() + vec3.z * 4.0);
                    Vec3 vec32 = new Vec3(f, g, h);
                    if (!ghast.isSilent()) {
                        level.levelEvent(null, 1016, ghast.blockPosition(), 0);
                    }

                    LargeFireball largeFireball = new LargeFireball(level, ghast, vec32.normalize(), ghast.getExplosionPower());
                    largeFireball.setPos(ghast.getX() + vec3.x * 4.0, ghast.getY(0.5) + 0.5, largeFireball.getZ() + vec3.z * 4.0);
                    level.addFreshEntity(largeFireball);
                    this.chargeTime = 0;
                }
            } else if (this.chargeTime > 0) {
                this.chargeTime--;
            }

            ghast.setCharging(this.chargeTime > 10);
        }

        callbackInfo.cancel();
    }
}
