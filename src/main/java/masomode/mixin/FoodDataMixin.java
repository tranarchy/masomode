package masomode.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {

    @Shadow
    private int foodLevel;

    @Shadow
    private float saturationLevel;

    @Shadow
    private float exhaustionLevel;

    @Shadow
    private int tickTimer;

    @Shadow
    public abstract void addExhaustion(float f);

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(ServerPlayer serverPlayer, CallbackInfo callbackInfo) {
        ServerLevel serverLevel = serverPlayer.level();
        if (this.exhaustionLevel > 4.0F) {
            this.exhaustionLevel -= 4.0F;
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            } else {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        boolean healthRegenEnabled = (Boolean)serverLevel.getGameRules().get(GameRules.NATURAL_HEALTH_REGENERATION);
        if (healthRegenEnabled && this.saturationLevel > 0.0F && serverPlayer.isHurt() && this.foodLevel >= 20) {
            ++this.tickTimer;
            if (this.tickTimer >= 300) {
                float f = Math.min(this.saturationLevel, 6.0F);
                serverPlayer.heal(f / 6.0F);
                this.addExhaustion(f * 2);
                this.tickTimer = 0;
            }
        } else if (healthRegenEnabled && this.foodLevel >= 18 && serverPlayer.isHurt()) {
            ++this.tickTimer;
            if (this.tickTimer >= 600) {
                serverPlayer.heal(1.0F);
                this.addExhaustion(12.f);
                this.tickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            ++this.tickTimer;
            if (this.tickTimer >= 40) {
                serverPlayer.hurtServer(serverLevel, serverPlayer.damageSources().starve(), 1.0F);

                this.tickTimer = 0;
            }
        } else {
            this.tickTimer = 0;
        }

        callbackInfo.cancel();
    }
}
