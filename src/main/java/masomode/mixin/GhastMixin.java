package masomode.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ghast.class)
public class GhastMixin extends Mob {

    public GhastMixin(EntityType<? extends Ghast> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    protected void registerGoals(CallbackInfo callbackInfo) {
       this.targetSelector.removeGoal(this.targetSelector.getAvailableGoals().stream().findFirst().get().getGoal());
        this.targetSelector
                .addGoal(
                        1, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, (livingEntity, serverLevel) -> Math.abs(livingEntity.getY() - this.getY()) <= 8.0)
                );
    }
}
