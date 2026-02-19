package masomode.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "causeFoodExhaustion", at = @At("HEAD"), cancellable = true)
    public void causeFoodExhaustion(float amount, CallbackInfo callbackInfo) {
        Player player = ((Player) (Object)this);

        if (!player.level().isClientSide()) {
            player.getFoodData().addExhaustion(amount * 5f);
        }

        callbackInfo.cancel();

    }

   /* @Inject(method = "onEnchantmentPerformed", at = @At("TAIL"))
    public void onEnchantmentPerformed(ItemStack itemStack, int i) {
        Player player = ((Player) (Object)this);

        this.experienceLevel -= i;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experienceProgress = 0.0F;
            this.totalExperience = 0;
        }

        this.enchantmentSeed = this.random.nextInt();

        //player.experienceLevel -= (i * 10) + i;


    }*/
}
