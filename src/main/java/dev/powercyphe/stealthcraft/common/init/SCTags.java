package dev.powercyphe.stealthcraft.common.init;

import dev.powercyphe.stealthcraft.common.StealthCraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public interface SCTags {

    TagKey<EntityType<?>> NO_VISION_HANDLER = key(Registries.ENTITY_TYPE, "no_vision_handler");

    static void init() {}

    static <E> TagKey<E> key(ResourceKey<Registry<E>> registry, String name) {
        return TagKey.create(registry, StealthCraft.id(name));
    }
}
