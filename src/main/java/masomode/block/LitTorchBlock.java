package masomode.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Unique;

public class LitTorchBlock extends TorchBlock {

    @Unique
    private int tickToDestroy = 0;

    public LitTorchBlock(SimpleParticleType simpleParticleType, Properties properties) {
        super(simpleParticleType, properties);
    }

    @Override
    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        tickToDestroy++;
        if (tickToDestroy >= 10) {
            serverLevel.setBlockAndUpdate(blockPos, Blocks.TORCH.withPropertiesOf(blockState));
            tickToDestroy = 0;
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState blockState) {
        return true;
    }
}
