package masomode.mixin;

import masomode.mobeffect.CustomMobEffects;
import masomode.mixininterface.IServerPlayer;
import masomode.utils.Common;
import masomode.utils.CustomDamage;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.illager.Illusioner;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void checkTotemDeathProtection(DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(false);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo callbackInfo) {
        LivingEntity livingEntity = ((LivingEntity) (Object)this);

        Random random = new Random();

        if (livingEntity.isOnFire() && !(livingEntity.level().isClientSide()) && random.nextInt(50) == 0) {
            Common.setNeighborBlocksOnFire((ServerLevel) livingEntity.level(), livingEntity.blockPosition());
        }
    }

    @ModifyVariable(method = "hurtServer", at = @At(value = "HEAD", ordinal = 0))
    public float hurtServer(float f, ServerLevel serverLevel, DamageSource damageSource) {
        Entity attacker = damageSource.getEntity();

        if (!(attacker instanceof ServerPlayer))
            return f;

        ItemStack weaponItem = damageSource.getWeaponItem();

        if (weaponItem == null)
            return f;

        CustomDamage.DAMAGE_TYPE damageType = CustomDamage.getDamageType(damageSource);

        if (damageType == CustomDamage.DAMAGE_TYPE.OTHER) {
            damageType = CustomDamage.getDamageType(weaponItem);
        }

        LivingEntity livingEntity = ((LivingEntity) (Object)this);

        if (CustomDamage.resistances.containsKey(livingEntity.getType()))
            return f * CustomDamage.resistances.get(livingEntity.getType()).get(damageType.ordinal());
        else
            return f;
    }

    @Inject(method = "applyItemBlocking", at = @At("RETURN"))
    public void applyItemBlocking(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        LivingEntity livingEntity = ((LivingEntity) (Object)this);

        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if (callbackInfoReturnable.getReturnValue() != 0) {

                ItemStack itemBlockingWith = serverPlayer.getItemBlockingWith();

                if (itemBlockingWith != null) {
                    boolean explosion = damageSource.getEntity() instanceof Creeper || damageSource.is(DamageTypes.EXPLOSION) || damageSource.is(DamageTypes.PLAYER_EXPLOSION);

                    serverPlayer.getCooldowns().addCooldown(itemBlockingWith, explosion ? 20 * 10 : 20 * 4);
                    serverPlayer.stopUsingItem();
                } else {
                    Entity attacker = damageSource.getEntity();

                    if (attacker != null) {
                        if (attacker.getWeaponItem() != null) {
                            if (attacker.getWeaponItem().is(ItemTags.AXES)) {
                                if (serverPlayer.getOffhandItem().get(DataComponents.BLOCKS_ATTACKS) != null) {
                                    serverPlayer.getOffhandItem().hurtAndBreak(9999, livingEntity, serverPlayer.getEquipmentSlotForItem(serverPlayer.getOffhandItem()));
                                } else if (serverPlayer.getMainHandItem().get(DataComponents.BLOCKS_ATTACKS) != null) {
                                    serverPlayer.getMainHandItem().hurtAndBreak(9999, livingEntity, serverPlayer.getEquipmentSlotForItem(serverPlayer.getMainHandItem()));
                                }
                            }
                        }
                    }
                }
            } else {
                Random random = new Random();

                if (damageSource.getEntity() instanceof Drowned || damageSource.getEntity() instanceof CaveSpider) {
                    serverPlayer.addEffect(new MobEffectInstance(CustomMobEffects.PARALYZED, 20 * 15));
                } else if (damageSource.getEntity() instanceof Blaze) {
                    ((IServerPlayer)serverPlayer).addFireTicks(20);
                } else if (damageSource.getEntity() instanceof WitherSkeleton || damageSource.getEntity() instanceof PiglinBrute || damageSource.getEntity() instanceof Vindicator) {
                    serverPlayer.addEffect(new MobEffectInstance(CustomMobEffects.BLEED, -1));
                    serverPlayer.addEffect(new MobEffectInstance(CustomMobEffects.BONE_FRACTURE, -1));
                } else if (damageSource.is(DamageTypes.ARROW)) {
                    if (random.nextBoolean()) {
                        serverPlayer.addEffect(new MobEffectInstance(CustomMobEffects.BLEED, -1));
                    }

                    if (!(damageSource.getEntity() instanceof Illusioner)) {
                        serverPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 25, 1));
                    }
                }
            }
        }
    }
}
