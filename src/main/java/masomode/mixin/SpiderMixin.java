package masomode.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import masomode.utils.Common;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Spider.class)
abstract class SpiderMixin extends Monster {
    public SpiderMixin(EntityType<? extends Spider> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder createAttributes(AttributeSupplier.Builder builder) {
       return builder.add(Attributes.SCALE, 0.6f);
    }

    @Inject(method = "registerGoals", at = @At("RETURN"))
    private void registerGoals(CallbackInfo callbackInfo) {
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal(this, Animal.class, true));
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entity) {
        if (super.doHurtTarget(serverLevel, entity)) {
            ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0), this);


            return true;
        } else {
            return false;
        }
    }

    @Inject(method = "finalizeSpawn", at = @At("HEAD"))
    public void finalizeSpawn(
            ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, EntitySpawnReason entitySpawnReason, @Nullable SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> callbackInfoReturnable
    ) {
        if (Common.isBloodMoon(this.level())) {
            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, -1));
        }
    }
}
