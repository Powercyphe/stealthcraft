package dev.powercyphe.stealthcraft.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.powercyphe.stealthcraft.common.SCConfig;
import dev.powercyphe.stealthcraft.common.init.SCAttachmentTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin extends TargetGoal {
    public NearestAttackableTargetGoalMixin(Mob mob, boolean mustSee) {
        super(mob, mustSee);
    }

    @ModifyReturnValue(method = "canUse", at = @At("RETURN"))
    private boolean stealthcraft$adjustTargeting(boolean original) {
        return original && SCConfig.seenMode == SCConfig.SeenMode.TARGET && this.mob.getAttachedOrCreate(SCAttachmentTypes.TARGET_TIME) > 0;
    }
}
