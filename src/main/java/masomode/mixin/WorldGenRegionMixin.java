package masomode.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(WorldGenRegion.class)
public abstract class WorldGenRegionMixin {

    @Shadow
    public abstract boolean removeBlock(BlockPos blockPos, boolean bl);

    @Unique
    private final List<Block> bannedBlocks = Arrays.asList(
            Blocks.CRAFTING_TABLE,
            Blocks.FURNACE,
            Blocks.SMOKER,
            Blocks.BLAST_FURNACE,
            Blocks.SMITHING_TABLE,
            Blocks.GRINDSTONE,
            Blocks.LOOM,
            Blocks.FLETCHING_TABLE,
            Blocks.HAY_BLOCK,
            Blocks.CAULDRON,
            Blocks.WATER_CAULDRON,
            Blocks.STONECUTTER,
            Blocks.BREWING_STAND
    );

    @Inject(method = "setBlock", at = @At("RETURN"))
    public void setBlock(BlockPos blockPos, BlockState blockState, @Block.UpdateFlags int i, int j, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
       if (callbackInfoReturnable.getReturnValue() && (bannedBlocks.contains(blockState.getBlock()) || blockState.is(BlockTags.BEDS))) {
           removeBlock(blockPos, true);
       }
    }
}
