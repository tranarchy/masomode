package masomode.mobeffect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FearMobEffect extends MobEffect {

    public FearMobEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int i) {
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            serverPlayer.causeFoodExhaustion(0.005f * (i + 1));
        }

        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int i, int j) {
        return true; }
}
