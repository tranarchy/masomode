package masomode.mixin.client;

import masomode.MainClient;
import masomode.mobeffect.CustomMobEffects;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends ClientInput {

    @Unique
    private int tick = 0;

    @Shadow
    @Final
    private Options options;

    @Unique
    private static float calculateImpulse(boolean bl, boolean bl2) {
        if (bl == bl2) {
            return 0.0F;
        } else {
            return bl ? 1.0F : -1.0F;
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void beforeTick(CallbackInfo callbackInfo) {
        if (MainClient.mc.player.hasEffect(CustomMobEffects.PARALYZED)) {
            KeyMapping.releaseAll();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void afterTick(CallbackInfo callbackInfo) {
        if (!MainClient.mc.player.hasEffect(CustomMobEffects.FEAR)) {
            tick = 0;
            return;
        }

        Random random = new Random();

        if (tick % 60 == 0 && random.nextBoolean()) {
            MainClient.mc.level.playLocalSound(MainClient.mc.player.blockPosition(), SoundEvents.AMBIENT_CAVE.value(), SoundSource.AMBIENT, 1.0f, 1.0f, false);
        }

        tick++;

        for (MobEffectInstance mobEffectInstance : MainClient.mc.player.getActiveEffects()) {
            if (mobEffectInstance.is(CustomMobEffects.FEAR)) {
                if (mobEffectInstance.getAmplifier() < 1) {
                    return;
                }
            }
        }

        boolean keyDown = this.options.keyDown.isDown();
        boolean keyUp = this.options.keyUp.isDown();
        boolean keyLeft = this.options.keyLeft.isDown();
        boolean keyRight = this.options.keyRight.isDown();

        this.keyPresses = new Input(keyDown, keyUp, keyRight, keyLeft, this.options.keyJump.isDown(), this.options.keyShift.isDown(), this.options.keySprint.isDown());
        float f = calculateImpulse(this.keyPresses.forward(), this.keyPresses.backward());
        float g = calculateImpulse(this.keyPresses.left(), this.keyPresses.right());
        this.moveVector = (new Vec2(g, f)).normalized();
    }
}
