package masomode.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import masomode.goal.CommonGoal;
import masomode.utils.Common;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.MoonPhase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public class CreeperMixin extends Monster {
    @Shadow
    private int maxSwell = 15;

    @Shadow
    @Final
    private static EntityDataAccessor<Boolean> DATA_IS_POWERED;

    @Unique
    private boolean ranBloodMoonTick = false;

    public CreeperMixin(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickBefore(CallbackInfo callbackInfo) {
        if (Common.isBloodMoon(this.level())) {
            if (!this.entityData.get(DATA_IS_POWERED).booleanValue() && this.random.nextInt(5) == 0 && !ranBloodMoonTick){
                this.entityData.set(DATA_IS_POWERED, true);
            }

            ranBloodMoonTick = true;
        }
    }

    @Inject(method = "registerGoals", at = @At("RETURN"))
    private void registerGoals(CallbackInfo callbackInfo) {
        this.goalSelector.addGoal(0, new LeapAtTargetGoal(this, 0.6f));
        CommonGoal.targetAnimals(this, this.targetSelector);
    }
}
