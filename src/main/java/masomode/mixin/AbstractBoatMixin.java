package masomode.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoat.class)
public class AbstractBoatMixin {
    @Inject(method = "checkFallDamage", at = @At("HEAD"))
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos, CallbackInfo info) {
        AbstractBoat abstractBoat = ((AbstractBoat) (Object)this);

        for (Entity entity : abstractBoat.getPassengers()) {
            if (!entity.isInWater() && d < (double)0.0F) {
                abstractBoat.fallDistance -= (double)((float)d);
            }

            if (bl) {
                if (abstractBoat.fallDistance > (double)0.0F) {
                    blockState.getBlock().fallOn(entity.level(), blockState, blockPos, entity, abstractBoat.fallDistance);
                    entity.level().gameEvent(GameEvent.HIT_GROUND, entity.position(), GameEvent.Context.of(entity, (BlockState)entity.mainSupportingBlockPos.map((blockPosx) -> entity.level().getBlockState(blockPosx)).orElse(blockState)));
                }

                entity.resetFallDistance();
            }
        }
    }
}
