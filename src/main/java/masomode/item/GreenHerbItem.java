package masomode.item;

import masomode.consumeeffect.CureConsumeEffect;
import masomode.mobeffect.CustomMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class GreenHerbItem extends Item {
    public static Item.Properties properties = new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().build(), Consumables.defaultFood().onConsume(new CureConsumeEffect(CustomMobEffects.INFECTION)).build());

    public GreenHerbItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        consumer.accept(Component.literal("A green herb with a spicy scent").withStyle(ChatFormatting.GOLD));
    }
}
