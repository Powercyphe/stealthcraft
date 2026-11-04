package dev.powercyphe.stealthcraft.common.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class EntityVisionRegistry {
    private static final Map<EntityType<?>, EntityVision> ENTITY_VISIONS = new HashMap<>();

    public static void register(EntityType<?> type, EntityVision vision) {
        ENTITY_VISIONS.put(type, vision);
    }

    public static <T extends Entity> EntityVision getVisionFor(T entity) {
        return ENTITY_VISIONS.getOrDefault(entity.getType(), EntityVision.DEFAULT);
    }
}
