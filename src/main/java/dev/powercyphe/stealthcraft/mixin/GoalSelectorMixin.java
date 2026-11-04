package dev.powercyphe.stealthcraft.mixin;

import dev.powercyphe.stealthcraft.common.SCConfig;
import dev.powercyphe.stealthcraft.mixin.accessor.LookAtPlayerGoalAccessor;
import net.minecraft.world.entity.ai.goal.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GoalSelector.class)
public class GoalSelectorMixin {

    @ModifyVariable(method = "addGoal", at = @At("HEAD"), name = "goal", argsOnly = true)
    private Goal stealthcraft$replaceGoals(Goal goal) {
        if (SCConfig.entityGoalTweaks && goal instanceof LookAtPlayerGoal lapGoal) {
            goal = new RandomLookAroundGoal(((LookAtPlayerGoalAccessor) lapGoal).stealthcraft$getMob());
        }
        return goal;
    }
}
