package masomode.mobeffect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BleedingMobEffect extends MobEffect {

        public BleedingMobEffect(MobEffectCategory mobEffectCategory, int i) {
            super(mobEffectCategory, i);
        }

        public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int i) {
            if (livingEntity.getHealth() > 1.0F) {
                livingEntity.hurtServer(serverLevel, livingEntity.damageSources().magic(), 1.0F);
            }

            return true;
        }

        public boolean shouldApplyEffectTickThisTick(int i, int j) {
            if (i % 200 == 0) {
                return true;
            }

            return false;
        }

}
