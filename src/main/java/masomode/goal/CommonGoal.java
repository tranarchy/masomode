package masomode.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.wolf.Wolf;

public class CommonGoal {
    public static void targetAnimals (Mob mob, GoalSelector targetSelector) {
        int goalCount = targetSelector.getAvailableGoals().size();
        targetSelector.addGoal(goalCount + 1, new NearestAttackableTargetGoal(mob, Pig.class, true));
        targetSelector.addGoal(goalCount + 2, new NearestAttackableTargetGoal(mob, Cow.class, true));
        targetSelector.addGoal(goalCount + 3, new NearestAttackableTargetGoal(mob, Sheep.class, true));
        targetSelector.addGoal(goalCount + 4, new NearestAttackableTargetGoal(mob, Chicken.class, true));
        targetSelector.addGoal(goalCount + 5, new NearestAttackableTargetGoal(mob, Wolf.class, true));
    }
}
