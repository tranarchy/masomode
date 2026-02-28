package masomode.item;

import masomode.block.CustomBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FireStarterItem extends FlintAndSteelItem {

    public FireStarterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);

        if (blockState.getBlock() == Blocks.TORCH) {
            level.playSound(player, blockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            useOnContext.getItemInHand().hurtAndBreak(1, player, useOnContext.getHand().asEquipmentSlot());
            level.setBlockAndUpdate(blockPos, CustomBlocks.LIT_TORCH.withPropertiesOf(blockState));
            return InteractionResult.SUCCESS;
        } else if (blockState.getBlock() == Blocks.WALL_TORCH) {
            level.playSound(player, blockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            useOnContext.getItemInHand().hurtAndBreak(1, player, useOnContext.getHand().asEquipmentSlot());
            level.setBlockAndUpdate(blockPos, CustomBlocks.LIT_WALL_TORCH.withPropertiesOf(blockState));
            return InteractionResult.SUCCESS;
        } else {
            return super.useOn(useOnContext);
        }
    }
}
