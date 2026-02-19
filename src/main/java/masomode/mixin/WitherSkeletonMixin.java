package masomode.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
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

    /*@Inject(method = "doHurtTarget", at = @At("RETURN"))
    public void doHurtTarget(ServerLevel serverLevel, Entity entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (callbackInfoReturnable.getReturnValue()) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(CustomMobEffects.BLEED, -1), this);
            ((LivingEntity) entity).addEffect(new MobEffectInstance(CustomMobEffects.BONE_FRACTURE, -1), this);
        }
    }*/
}
