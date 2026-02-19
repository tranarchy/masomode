package masomode.mixin;

import masomode.utils.Common;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Monster.class)
public class MonsterMixin {

    @Inject(method = "checkMonsterSpawnRules", at = @At("RETURN"), cancellable = true)
    private static void checkMonsterSpawnRules(
            EntityType<? extends Mob> entityType,
            ServerLevelAccessor serverLevelAccessor,
            EntitySpawnReason entitySpawnReason,
            BlockPos blockPos,
            RandomSource randomSource,
            CallbackInfoReturnable<Boolean> callbackInfoReturnable
    ) {
        if (entityType == EntityType.ILLUSIONER && callbackInfoReturnable.getReturnValue()) {
            callbackInfoReturnable.setReturnValue(Common.isBloodMoon(serverLevelAccessor.getLevel()));
        }
    }

}
