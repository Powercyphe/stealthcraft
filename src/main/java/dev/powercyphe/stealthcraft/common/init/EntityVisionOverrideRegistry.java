package dev.powercyphe.stealthcraft.common.init;

import dev.powercyphe.stealthcraft.common.entity.EntityVision;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static net.minecraft.world.entity.EntityTypes.ENDER_DRAGON;
import static net.minecraft.world.entity.EntityTypes.WARDEN;

public class EntityVisionOverrideRegistry {
    private static final Map<EntityType<?>, EntityVision<?>> ENTITY_VISION_OVERRIDES = new HashMap<>();

    public static void init() {
        register(WARDEN, builder -> builder.range(0F));
        register(ENDER_DRAGON, builder -> builder
                .range(150).fov(145)
                .eyeOffset(e -> e.getBbHeight() / 3)
                .direction((e, p) -> new Vec2(e.getViewXRot(p), e.flightHistory.get(7, p).yRot() + 180))
                .build());
    }

    public static <E extends LivingEntity> void register(EntityType<E> type, EntityVision<E> vision) {
        ENTITY_VISION_OVERRIDES.put(type, vision);
    }

    public static <E extends LivingEntity> void register(EntityType<E> type, Consumer<EntityVision.Builder<E>> consumer) {
        var builder = EntityVision.<E>builder();
        consumer.accept(builder);
        register(type, builder.build());
    }

    public static <E extends LivingEntity> EntityVision<E> getVisionFor(E entity) {
        return (EntityVision<E>) ENTITY_VISION_OVERRIDES.getOrDefault(entity.getType(), EntityVision.DEFAULT);
    }
}
