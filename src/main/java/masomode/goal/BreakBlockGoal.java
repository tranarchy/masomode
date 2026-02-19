package masomode.goal;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Block;

import java.util.function.Predicate;

public class BreakBlockGoal extends BlockInteractGoal {
    private final Predicate<Difficulty> validDifficulties;
    protected int breakTime;
    protected int lastBreakProgress;
    protected int blockBreakTime;

    public BreakBlockGoal(Mob mob, Predicate<Difficulty> predicate) {
        super(mob);
        this.lastBreakProgress = -1;
        this.blockBreakTime = -1;
        this.validDifficulties = predicate;
    }

    public BreakBlockGoal(Mob mob, int i, Predicate<Difficulty> predicate) {
        this(mob, predicate);
        this.blockBreakTime = i;
    }

    protected int getBlockBreakTime() {
        return 60;
    }


    public void start() {
        super.start();
        this.breakTime = 0;
    }

    public boolean canContinueToUse() {
        return this.breakTime <= this.getBlockBreakTime() && this.blockPos.closerToCenterThan(this.mob.position(), (double)2.0F);
    }

    public void stop() {
        super.stop();
        this.mob.level().destroyBlockProgress(this.mob.getId(), this.blockPos, -1);
    }

    public void tick() {
        super.tick();
        if (this.mob.getRandom().nextInt(20) == 0) {
            this.mob.level().levelEvent(1019, this.blockPos, 0);
            if (!this.mob.swinging) {
                this.mob.swing(this.mob.getUsedItemHand());
            }
        }

        ++this.breakTime;
        int i = (int)((float)this.breakTime / (float)this.getBlockBreakTime() * 10.0F);
        if (i != this.lastBreakProgress) {
            this.mob.level().destroyBlockProgress(this.mob.getId(), this.blockPos, i);
            this.lastBreakProgress = i;
        }

        if (this.breakTime == this.getBlockBreakTime()) {
            this.mob.level().removeBlock(this.blockPos, false);
            this.mob.level().levelEvent(1021, this.blockPos, 0);
            this.mob.level().levelEvent(2001, this.blockPos, Block.getId(this.mob.level().getBlockState(this.blockPos)));
        }

    }
}
