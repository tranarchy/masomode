package masomode.mixin;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.Enchantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "isEnchantable", at = @At("HEAD"), cancellable = true)
    public void isEnchantable(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ItemStack itemStack = ((ItemStack) (Object)this);

        /*if (itemStack.is(Items.TOTEM_OF_UNDYING)) {
            System.out.println("RAN TOTEM isEnchantable");
            callbackInfoReturnable.setReturnValue(true);
        }*/
    }
}
