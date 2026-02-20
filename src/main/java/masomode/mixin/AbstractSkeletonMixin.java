package masomode.mixin;

import it.unimi.dsi.fastutil.ints.IntList;
import masomode.goal.CommonGoal;
import masomode.utils.Common;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonMixin extends Monster implements RangedAttackMob {
    public AbstractSkeletonMixin(EntityType<? extends Skeleton> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals", at = @At("RETURN"))
    private void registerGoals(CallbackInfo callbackInfo) {
        CommonGoal.targetAnimals(this, this.targetSelector);
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
    private void populateDefaultEquipmentSlot(RandomSource randomSource, DifficultyInstance difficultyInstance, CallbackInfo info) {
    }

    @Inject(method = "performRangedAttack", at = @At("HEAD"), cancellable = true)
    public void performRangedAttack(LivingEntity livingEntity, float f, CallbackInfo info) {
        if (Common.isBloodMoon(this.level())) {

            if (this.random.nextFloat() > 0.95f) {
                if (this.level() instanceof ServerLevel serverLevel) {
                    ItemStack crossbowItemStack = new ItemStack(Items.CROSSBOW);
                    ItemStack fireworkItemStack = new ItemStack(Items.FIREWORK_ROCKET);
                    FireworkExplosion fireworkExplosion = new FireworkExplosion(FireworkExplosion.Shape.STAR, IntList.of(0xe3142a), IntList.of(0xe3142a), false, false);
                    fireworkItemStack.set(DataComponents.FIREWORKS, new Fireworks(100, List.of(fireworkExplosion, fireworkExplosion, fireworkExplosion)));
                    fireworkItemStack.set(DataComponents.FIREWORK_EXPLOSION, fireworkExplosion);
                    crossbowItemStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(fireworkItemStack));
                    CrossbowItem crossbowItem = (CrossbowItem) crossbowItemStack.getItem();
                    crossbowItem.performShooting(this.level(), this, InteractionHand.MAIN_HAND, crossbowItemStack, 1.6F, (float) (14 - serverLevel.getDifficulty().getId() * 4), this.getTarget());
                }
            } else {
                ItemStack itemStack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
                ItemStack itemStack2 = new ItemStack(Items.ARROW);
                AbstractArrow abstractArrow = ProjectileUtil.getMobArrow(this, itemStack, f, itemStack2);
                abstractArrow.igniteForSeconds(10.0f);
                double d = livingEntity.getX() - this.getX();
                double e = livingEntity.getY(0.3333333333333333) - abstractArrow.getY();
                double g = livingEntity.getZ() - this.getZ();
                double h = Math.sqrt(d * d + g * g);

                if (this.level() instanceof ServerLevel serverLevel) {
                    Projectile.spawnProjectileUsingShoot(abstractArrow, serverLevel, itemStack2, d, e + h * (double) 0.2F, g, 1.6F, (float) (14 - serverLevel.getDifficulty().getId() * 4));
                }
            }

            if (this.random.nextFloat() > 0.7f) {
                this.performRangedAttack(livingEntity, f);
            }

            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));

            info.cancel();
        }
    }
}
