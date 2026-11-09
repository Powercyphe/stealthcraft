package dev.powercyphe.stealthcraft.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EntityVisionRegistry {
    private static final Map<EntityType<?>, EntityVision<?>> ENTITY_VISIONS = new HashMap<>();

    public static <E extends LivingEntity> void register(EntityType<E> type, EntityVision<E> vision) {
        ENTITY_VISIONS.put(type, vision);
    }

    public static <E extends LivingEntity> void register(EntityType<E> type, Consumer<EntityVision.Builder<E>> consumer) {
        var builder = EntityVision.<E>builder();
        consumer.accept(builder);
        register(type, builder.build());
    }

    public static <E extends LivingEntity> EntityVision<E> getVisionFor(E entity) {
        return (EntityVision<E>) ENTITY_VISIONS.getOrDefault(entity.getType(), EntityVision.DEFAULT);
    }
}
