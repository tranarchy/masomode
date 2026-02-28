package masomode.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class CeruleanPearlItem extends Item {
    public CeruleanPearlItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        consumer.accept(Component.literal("Use to enchant and disenchant items").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

        if (!itemStack.isEnchanted()) {
            player.displayClientMessage(Component.literal(ChatFormatting.RED + "You need to hold an enchanted item"), true);
            return InteractionResult.FAIL;
        } else {
            if (player.experienceLevel < 30) {
                player.displayClientMessage(Component.literal(ChatFormatting.RED + "You need to have at least 30 XP levels"), true);
                return InteractionResult.FAIL;
            }

            ItemStack book = Items.ENCHANTED_BOOK.getDefaultInstance();
            for (Holder<Enchantment> enchantment : itemStack.getEnchantments().keySet()) {
                book.enchant(enchantment, itemStack.getEnchantments().getLevel(enchantment));
            }

            itemStack.consume(1, player);
            player.getItemInHand(interactionHand).consume(1, player);

            player.addItem(book);
            player.giveExperienceLevels(-3);

            level.playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
        }

        return InteractionResult.SUCCESS;
    }
}
