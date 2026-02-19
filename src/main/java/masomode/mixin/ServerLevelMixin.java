package masomode.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.MoonPhase;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Shadow
    @Final
    private ServerLevelData serverLevelData;

    @Shadow
    @Final
    private SleepStatus sleepStatus;

    @Shadow
    @Final
    List<ServerPlayer> players;

    @ModifyVariable(method = "setDayTime", at = @At(value = "HEAD", ordinal = 0))
    public long setDayTime(long l) {

        int playersSleepingPercentage = serverLevelData.getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE);
        if (this.sleepStatus.areEnoughSleeping(playersSleepingPercentage) && this.sleepStatus.areEnoughDeepSleeping(playersSleepingPercentage, this.players)) {
            if (l - serverLevelData.getDayTime() > 1) {
                return serverLevelData.getDayTime() + 6L;
            }
        }

        return l;
    }

    @Inject(method = "wakeUpAllPlayers", at = @At("HEAD"), cancellable = true)
    private void wakeUpAllPlayers(CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}
