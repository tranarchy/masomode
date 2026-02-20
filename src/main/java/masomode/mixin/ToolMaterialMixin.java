package masomode.mixin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToolMaterial.class)
public class ToolMaterialMixin {

    @Shadow
    int durability;

    @Shadow
    TagKey<Item> repairItems;

    @Shadow
    int enchantmentValue;

    @Inject(method = "applyCommonProperties", at = @At("RETURN"), cancellable = true)
    private void applyCommonProperties(Item.Properties properties, CallbackInfoReturnable<Item.Properties> callbackInfoReturnable) {
        int newDurability = this.durability;

        newDurability = switch (newDurability) {
            case 131 -> 59;
            case 190 -> 131;
            case 250 -> 190;
            default -> newDurability;
        };

        callbackInfoReturnable.setReturnValue(properties.durability(newDurability).repairable(this.repairItems).enchantable(this.enchantmentValue));
    }
}
