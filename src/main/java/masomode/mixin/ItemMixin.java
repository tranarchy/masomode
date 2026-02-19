package masomode.mixin;

import masomode.utils.CustomDamage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag, CallbackInfo info) {
        Item item = ((Item) (Object)this);

        CustomDamage.DAMAGE_TYPE damageType = CustomDamage.getDamageType(item.getDefaultInstance());

        switch (damageType) {
            case BLUNT -> consumer.accept(Component.literal("Blunt weapon").withStyle(ChatFormatting.DARK_PURPLE));
            case SLASHING -> consumer.accept(Component.literal("Slashing weapon").withStyle(ChatFormatting.DARK_PURPLE));
            case PIERCING -> consumer.accept(Component.literal("Piercing weapon").withStyle(ChatFormatting.DARK_PURPLE));
            case MAGIC -> consumer.accept(Component.literal("Magic weapon").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}
