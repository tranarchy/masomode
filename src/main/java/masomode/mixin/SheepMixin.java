package masomode.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.resources.Identifier;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sheep.class)
public abstract class SheepMixin extends Animal implements NeutralMob {
    public SheepMixin(EntityType<? extends Sheep> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    @Unique
    private long persistentAngerEndTime = 0;

    @Unique
    private @Nullable EntityReference<LivingEntity> persistentAngerTarget;

    @Unique
    private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(Identifier.withDefaultNamespace("attacking"), 0.08F, AttributeModifier.Operation.ADD_VALUE);


    @Inject(method = "registerGoals", at = @At("RETURN"))
    private void registerGoals(CallbackInfo callbackInfo) {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0f, false));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal(this, Monster.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(0, new ResetUniversalAngerTargetGoal(this, true));
    }

    @ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder createAttributes(AttributeSupplier.Builder builder) {
        return builder.add(Attributes.ATTACK_DAMAGE, (double) 2.0f);
    }

    @Override
    public void tick() {
        super.tick();

        AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);

        if (this.getTarget() != null) {
            if (!attributeInstance.hasModifier(Identifier.withDefaultNamespace("attacking"))) {
                attributeInstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
            }
        } else {
            attributeInstance.removeModifier(Identifier.withDefaultNamespace("attacking"));
        }
    }


    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> entityReference) {
        this.persistentAngerTarget = entityReference;
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public long getPersistentAngerEndTime() {
        return this.persistentAngerEndTime;
    }

    @Override
    public void setPersistentAngerEndTime(long ticks) {
        this.persistentAngerEndTime = ticks;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setTimeToRemainAngry((long)PERSISTENT_ANGER_TIME.sample(this.random));
    }
}
