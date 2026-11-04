package dev.powercyphe.stealthcraft.mixin.accessor;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LookAtPlayerGoal.class)
public interface LookAtPlayerGoalAccessor {

    @Accessor("mob")
    Mob stealthcraft$getMob();
}
