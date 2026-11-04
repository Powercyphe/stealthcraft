package dev.powercyphe.stealthcraft.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.powercyphe.stealthcraft.common.init.SCMemoryModuleTypes;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemorySlot;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Brain.class)
public abstract class BrainMixin {

    @Shadow
    public abstract boolean hasMemoryValue(MemoryModuleType<?> type);

    @Shadow
    protected abstract void registerMemory(MemoryModuleType<?> memoryType);

    @Unique
    private static final List<MemoryModuleType<?>> DISABLED = List.of(
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.NEAREST_ATTACKABLE,

            MemoryModuleType.NEAREST_PLAYERS,
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS,

            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD,
            MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM
    );

    @Inject(method = "<init>*", at = @At("TAIL"))
    private void stealthcraft$addMemory(CallbackInfo ci) {
        this.registerMemory(SCMemoryModuleTypes.CAN_TARGET);
    }

    @ModifyReturnValue(method = "getMemorySlot", at = @At(value = "RETURN"))
    private @Nullable <T> MemorySlot<T> stealthcraft$removeTargets(@Nullable MemorySlot<T> original, MemoryModuleType<T> memoryType) {
        if (original != null && DISABLED.contains(memoryType) && !this.hasMemoryValue(SCMemoryModuleTypes.CAN_TARGET)) {
            original.clear();
        }
        return original;
    }
}
