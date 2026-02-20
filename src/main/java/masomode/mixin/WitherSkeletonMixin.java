package masomode.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherSkeleton.class)
public abstract class WitherSkeletonMixin extends AbstractSkeleton {
    public WitherSkeletonMixin(EntityType<? extends WitherSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
    private void populateDefaultEquipmentSlot(RandomSource randomSource, DifficultyInstance difficultyInstance, CallbackInfo info) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        info.cancel();
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
        double maxRange = 1.6;
        double minRange = 0.0;

        AABB aABB = getHitbox(livingEntity);
        return this.getAttackBoundingBox(maxRange).intersects(aABB) && (minRange <= 0.0 || !this.getAttackBoundingBox(minRange).intersects(aABB));
    }
}
