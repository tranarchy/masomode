package masomode.mixin;

import masomode.mobeffect.CustomMobEffects;
import masomode.goal.CommonGoal;
import masomode.utils.Common;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public class ZombieMixin extends Monster {
    public ZombieMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "doHurtTarget", at = @At("HEAD"))
    public void doHurtTarget(ServerLevel serverLevel, Entity entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (super.doHurtTarget(serverLevel, entity) && entity instanceof ServerPlayer serverPlayer && !serverPlayer.hasEffect(CustomMobEffects.INFECTION)) {
            serverPlayer.addEffect(new MobEffectInstance(CustomMobEffects.INFECTION, 20 * 60 * 10, 0), this);
        }
    }

    @Inject(method = "randomizeReinforcementsChance", at = @At("HEAD"), cancellable = true)
    protected void randomizeReinforcementsChance(CallbackInfo callbackInfo) {
        if (Common.isBloodMoon(this.level())) {
            this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.3f);
            callbackInfo.cancel();
        }

    }

    @Inject(method = "registerGoals", at = @At("RETURN"))
    private void registerGoals(CallbackInfo callbackInfo) {
        CommonGoal.targetAnimals(this, this.targetSelector);
        //this.targetSelector.addGoal(0, new BreakBlockGoal(this, (difficulty) -> difficulty == Difficulty.HARD));
    }

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    public void finalizeSpawnAfter(
            ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, EntitySpawnReason entitySpawnReason, @Nullable SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> callbackInfoReturnable
    ) {
        this.setDropChance(EquipmentSlot.HEAD, 1.0F);
        this.setDropChance(EquipmentSlot.CHEST, 1.0F);
        this.setDropChance(EquipmentSlot.LEGS, 1.0F);
        this.setDropChance(EquipmentSlot.FEET, 1.0F);
        this.setDropChance(EquipmentSlot.MAINHAND, 1.0F);
        this.setDropChance(EquipmentSlot.OFFHAND, 1.0F);
    }

    @Unique
    private AABB getHitbox(LivingEntity livingEntity) {
        AABB aABB = livingEntity.getBoundingBox();
        Entity entity = livingEntity.getVehicle();
        if (entity != null) {
            Vec3 vec3 = entity.getPassengerRidingPosition(livingEntity);
            return aABB.setMinY(Math.max(vec3.y, aABB.minY));
        } else {
            return aABB;
        }
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity livingEntity) {
        double maxRange = 1.4;
        double minRange = 0.0;

        AABB aABB = getHitbox(livingEntity);
        return this.getAttackBoundingBox(maxRange).intersects(aABB) && (minRange <= 0.0 || !this.getAttackBoundingBox(minRange).intersects(aABB));
    }
}
