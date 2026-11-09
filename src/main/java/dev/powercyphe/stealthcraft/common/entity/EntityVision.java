package dev.powercyphe.stealthcraft.common.entity;

import dev.powercyphe.stealthcraft.common.util.Quad;
import dev.powercyphe.stealthcraft.common.util.SCUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public record EntityVision<E extends LivingEntity>(Function<E, Float> fov, Function<E, Float> range, Function<E, Float> eyeOffset, BiFunction<E, Float, Vec2> direction) {
    public static final EntityVision<?> DEFAULT = EntityVision.builder().build();

    public static final float DEFAULT_FOV = 115F;
    public static final float DEFAULT_RANGE = 16F;

    public List<Quad> getQuads(E entity, LivingEntity target) {
        float range = this.range(entity, target);
        float radius = this.coneRadius(entity, range);

       return SCUtil.getConeQuads(entity, this.eyePos(entity), this.direction(entity), range, radius);
    }

    public boolean isVisible(E entity, LivingEntity target) {
        float range = this.range(entity, target);
        float radius = this.coneRadius(entity, range);

        Vec3 tip = this.eyePos(entity);
        Vec3 dir = this.direction3(entity);
        return SCUtil.insideCone(target.getEyePosition(), tip, dir, range, radius)
                && this.hasLineOfSight(entity, target);
    }

    public boolean hasLineOfSight(E entity, LivingEntity target) {
        if (entity instanceof LivingEntity living) {
            return living.hasLineOfSight(target);
        }
        return false;
    }

    public float coneRadius(E entity, float range) {
        return (float) Math.tan(Math.clamp(this.fov(entity), 0, 179) / (360 / Math.PI)) * (range / (float) Math.PI);
    }

    public float fov(E entity) {
        return this.fov().apply(entity);
    }

    public float range(E entity) {
        return this.range().apply(entity);
    }

    public float range(E entity, LivingEntity target) {
        return this.range(entity) * (target.level() instanceof ServerLevel serverLevel
                ? (float) target.getVisibilityPercent(serverLevel, entity)
                : SCUtil.getPlayerVisibility(entity, target));
    }

    public Vec3 eyePos(E entity) {
        return entity.position().add(0, this.eyeOffset(entity), 0);
    }

    public float eyeOffset(E entity) {
        return this.eyeOffset.apply(entity);
    }

    public Vec3 direction3(E entity) {
        return Vec3.directionFromRotation(this.direction(entity));
    }

    public Vec2 direction(E entity) {
        return this.direction(entity, 0F);
    }

    public Vec2 direction(E entity, float tickProgress) {
        return this.direction.apply(entity, tickProgress);
    }

    public static <E extends LivingEntity> Builder<E> builder() {
        return new Builder<>();
    }

    public static class Builder<E extends LivingEntity> {
        Function<E, Float> fov = entity -> DEFAULT_FOV;
        Function<E, Float> range = entity -> {
            AttributeInstance range = entity.getAttribute(Attributes.FOLLOW_RANGE);

            if (range != null) {
                return (float) range.getValue();
            }
            return DEFAULT_RANGE;
        };
        Function<E, Float> eyeOffset = Entity::getEyeHeight;
        BiFunction<E, Float, Vec2> direction = (entity, partialTicks) ->
                new Vec2(entity.getViewXRot(partialTicks), entity.getViewYRot(partialTicks));

        protected Builder() {}

        public Builder<E> fov(float fov) {
            return this.fov(entity -> fov);
        }

        public Builder<E> fov(Function<E, Float> fov) {
            this.fov = fov;
            return this;
        }

        public Builder<E> range(float range) {
            return this.range(entity -> range);
        }

        public Builder<E> range(Function<E, Float> range) {
            this.range = range;
            return this;
        }

        public Builder<E> eyePos(Function<E, Float> eyeOffset) {
            this.eyeOffset = eyeOffset;
            return this;
        }

        public Builder<E> direction(BiFunction<E, Float, Vec2> direction) {
            this.direction = direction;
            return this;
        }

        public EntityVision<E> build() {
            return new EntityVision<E>(this.fov, this.range, this.eyeOffset, this.direction);
        }
    }
}
