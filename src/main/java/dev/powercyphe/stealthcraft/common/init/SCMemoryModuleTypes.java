package dev.powercyphe.stealthcraft.common.init;

import com.mojang.serialization.Codec;
import dev.powercyphe.stealthcraft.common.StealthCraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Optional;

public interface SCMemoryModuleTypes {

    MemoryModuleType<Boolean> CAN_TARGET = register("can_target", Codec.BOOL);

    static void init() {}

    static <T> MemoryModuleType<T> register(String name, Codec<T> codec) {
        return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, StealthCraft.id(name), new MemoryModuleType<>(Optional.of(codec)));
    }
}
