package masomode.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

import java.util.Arrays;
import java.util.List;

public class BlockInteractGoal extends Goal {
    protected Mob mob;
    protected BlockPos blockPos;
    protected boolean hasBreakableBlock;
    private boolean passed;
    private float doorOpenDirX;
    private float doorOpenDirZ;

    private final List<Block> breakableBlocks = Arrays.asList(
            Blocks.DIRT,
            Blocks.GRAVEL,
            Blocks.CLAY,
            Blocks.OAK_LOG
    );

    public BlockInteractGoal(Mob mob) {
        this.blockPos = BlockPos.ZERO;
        this.mob = mob;
        if (!GoalUtils.hasGroundPathNavigation(mob)) {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
    }

    public boolean canUse() {
        if (!GoalUtils.hasGroundPathNavigation(this.mob)) {
            return false;
        } else if (!this.mob.horizontalCollision) {
            return false;
        } else {
            Path path = this.mob.getNavigation().getPath();
            if (path != null && !path.isDone()) {
                for(int i = 0; i < Math.min(path.getNextNodeIndex() + 2, path.getNodeCount()); ++i) {
                    for (int x = -1; x < 1; x++) {
                        for (int y = -1; y < 1; y++) {
                            for (int z = -1; z < 1; z ++) {
                                Node node = path.getNode(i);
                                this.blockPos = new BlockPos(node.x + x, node.y + y, node.z + z);
                                if (!(this.mob.distanceToSqr((double) this.blockPos.getX(), this.mob.getY(), (double) this.blockPos.getZ()) > (double) 2.25F)) {
                                    System.out.println(this.mob.level().getBlockState(this.blockPos).getBlock());
                                    this.hasBreakableBlock = breakableBlocks.contains(this.mob.level().getBlockState(this.blockPos).getBlock());
                                    if (this.hasBreakableBlock) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }

                this.blockPos = this.mob.blockPosition().above();
                this.hasBreakableBlock = breakableBlocks.contains(this.mob.level().getBlockState(this.blockPos).getBlock());
                System.out.println("2 " + this.hasBreakableBlock);
                return this.hasBreakableBlock;
            } else {
                return false;
            }
        }
    }

    public boolean canContinueToUse() {
        return !this.passed;
    }

    public void start() {
        this.passed = false;
        this.doorOpenDirX = (float)((double)this.blockPos.getX() + 0.5f - this.mob.getX());
        this.doorOpenDirZ = (float)((double)this.blockPos.getZ() + 0.5f - this.mob.getZ());
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        float f = (float)((double)this.blockPos.getX() + 0.5f - this.mob.getX());
        float g = (float)((double)this.blockPos.getZ() + 0.5f - this.mob.getZ());
        float h = this.doorOpenDirX * f + this.doorOpenDirZ * g;
        if (h < 0.0F) {
            this.passed = true;
        }

    }
}
