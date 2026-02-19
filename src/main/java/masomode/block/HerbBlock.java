package masomode.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HerbBlock extends BushBlock {
    public HerbBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        if (randomSource.nextInt(5) == 0) {
            BonemealableBlock.findSpreadableNeighbourPos(serverLevel, blockPos, blockState)
                    .ifPresent(blockPosx -> serverLevel.setBlockAndUpdate(blockPosx, this.defaultBlockState()));
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.is(BlockTags.DIRT) || blockState.is(Blocks.FARMLAND) || blockState.is(Blocks.PODZOL) || blockState.is(Blocks.SAND);
    }
}
