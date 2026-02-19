package masomode.mixin;

import masomode.mobeffect.CustomMobEffects;
import masomode.mixininterface.IServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.entity.vehicle.minecart.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements IServerPlayer {

    @Unique
    private int inDarknessTick = 0;

    @Unique
    private int onFireTick = 0;

    @Unique
    private int triggerTeleportTick = 0;

    @Unique
    private boolean canTeleport = false;

    @Unique
    public boolean teleport(ServerPlayer serverPlayer) {
        Random random = new Random();

        if (!serverPlayer.level().isClientSide() && serverPlayer.isAlive()) {
            double d = serverPlayer.getX() + (random.nextDouble() - (double)0.5F) * (double)64.0F;
            double e = serverPlayer.getY() + (double)(random.nextInt(64) - 32);
            double f = serverPlayer.getZ() + (random.nextDouble() - (double)0.5F) * (double)64.0F;
            return teleport(serverPlayer, d, e, f);
        } else {
            return false;
        }
    }

    @Unique
    private boolean teleport(ServerPlayer serverPlayer, double d, double e, double f) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(d, e, f);

        while(mutableBlockPos.getY() > serverPlayer.level().getMinY() && !serverPlayer.level().getBlockState(mutableBlockPos).blocksMotion()) {
            mutableBlockPos.move(Direction.DOWN);
        }

        BlockState blockState = serverPlayer.level().getBlockState(mutableBlockPos);
        boolean bl = blockState.blocksMotion();
        boolean bl2 = blockState.getFluidState().is(FluidTags.WATER);
        if (bl && !bl2) {
            Vec3 vec3 = serverPlayer.position();
            boolean bl3 = serverPlayer.randomTeleport(d, e, f, true);
            if (bl3) {
                serverPlayer.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(serverPlayer));
                serverPlayer.level().playSound((Entity)null, serverPlayer.xo, serverPlayer.yo, serverPlayer.zo, SoundEvents.ENDERMAN_TELEPORT, serverPlayer.getSoundSource(), 1.0F, 1.0F);
                serverPlayer.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }

            return bl3;
        } else {
            return false;
        }
    }

    @Inject(method = "doTick", at = @At("TAIL"))
    private void doTick(CallbackInfo callbackInfo) {
        ServerPlayer serverPlayer = ((ServerPlayer) (Object)this);
        ServerLevel level = serverPlayer.level();

        boolean nearEnderMan = false;
        boolean enderManAngry = false;

        for (Entity entity : level.getAllEntities()) {
            if (entity instanceof EnderMan enderMan) {

                if (enderMan.isAngryAt(serverPlayer, level)) {
                    nearEnderMan = true;
                    enderManAngry = true;
                }

                if (enderMan.distanceTo(serverPlayer) <= 4.0f) {
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 30, 0));
                    nearEnderMan = true;
                }
            }
        }

        BlockPos aboveBlock = serverPlayer.blockPosition().above();

        if (enderManAngry && ((!level.getBlockState(aboveBlock).canBeReplaced() || !level.getBlockState(aboveBlock.above()).canBeReplaced()) || (serverPlayer.isInWater() || serverPlayer.isUnderWater())) ) {
            triggerTeleportTick++;
        }

        if (triggerTeleportTick >= 80) {
            canTeleport = teleport(serverPlayer);
            if (canTeleport) {
                triggerTeleportTick = 0;
            }
        }


        BlockPos blockPos = serverPlayer.getVehicle() instanceof Boat || serverPlayer.getVehicle() instanceof Minecart ? serverPlayer.blockPosition().above() : serverPlayer.blockPosition();
        int combinedBrightness = level.getBrightness(LightLayer.BLOCK, blockPos) + level.getBrightness(LightLayer.SKY, blockPos);

        if (combinedBrightness <= 7 || nearEnderMan) {


            inDarknessTick++;

            if (inDarknessTick > 5) {
                if (!serverPlayer.hasEffect(CustomMobEffects.FEAR)) {
                    serverPlayer.addEffect(new MobEffectInstance(CustomMobEffects.FEAR, -1, 0));
                }

                if (inDarknessTick >= 20 * 20) {
                    serverPlayer.addEffect(new MobEffectInstance(CustomMobEffects.FEAR, -1, 1));

                    if (inDarknessTick >= 20 * 30) {
                        serverPlayer.addEffect(new MobEffectInstance(CustomMobEffects.FEAR, -1, 2));

                        if (inDarknessTick >= 20 * 45) {
                            serverPlayer.addEffect(new MobEffectInstance(CustomMobEffects.FEAR, -1, 3));
                            serverPlayer.hurtServer(level, serverPlayer.damageSources().generic(), 2);
                        }
                    }
                }
            }
        } else {
            serverPlayer.removeEffect(CustomMobEffects.FEAR);
            inDarknessTick = 0;
        }

        if (serverPlayer.hasEffect(CustomMobEffects.INFECTION)) {
            for (MobEffectInstance mobEffectInstance : serverPlayer.getActiveEffects()) {
                if (mobEffectInstance.is(CustomMobEffects.INFECTION)) {
                    if (mobEffectInstance.getDuration() <= 1) {
                        serverPlayer.kill(level);
                    }
                }
            }
        }

        float health = serverPlayer.getHealth();

        if (health <= 12) {
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 1));
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 60, 0));
            if (health <= 8) {
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 2));
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 60, 1));
                if (health <= 4) {
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 3));
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1));
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 60, 2));
                    if (health <= 2) {
                        serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 4));
                        serverPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
                        serverPlayer.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 60, 4));
                        serverPlayer.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 4));
                    }
                }
            }
        }

        if (serverPlayer.isOnFire()) {
            onFireTick++;

            if (onFireTick >= 100) {
                onFireTick = 0;

                if (!serverPlayer.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    serverPlayer.hurtServer(level, serverPlayer.damageSources().onFire(), 9999.0F);
                }
            }
        } else {
            onFireTick = 0;
        }

        serverPlayer.getFoodData().addExhaustion(0.005f);
    }

    @Inject(method = "hurtServer", at = @At("TAIL"))
    public void hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ServerPlayer serverPlayer = ((ServerPlayer) (Object)this);

        ItemStack itemBlockingWith = serverPlayer.getItemBlockingWith();

        if (itemBlockingWith != null) {
            if (serverPlayer.getCooldowns().isOnCooldown(itemBlockingWith)) {
                serverPlayer.stopUsingItem();
            }
        }

        if (damageSource.is(DamageTypes.FALL)) {
            if (f >= 8) {
                serverPlayer.addEffect(new MobEffectInstance(CustomMobEffects.BONE_FRACTURE, -1));
            }
        }
    }

    @Override
    public void addFireTicks(int fireTicks) {
        onFireTick += fireTicks;
    }
}
