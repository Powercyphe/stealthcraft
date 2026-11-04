package dev.powercyphe.stealthcraft.common.entity;

import dev.powercyphe.stealthcraft.common.util.SCUtil;
import dev.powercyphe.stealthcraft.common.util.Triangle;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Function;

public record EntityVision(Function<Entity, Float> fov, Function<Entity, Float> range, Function<Entity, Vec3> eyePos, Function<Entity, Vec3> direction) {
    public static final EntityVision DEFAULT = EntityVision.builder().build();

    public static final float DEFAULT_FOV = 115F;
    public static final float DEFAULT_RANGE = 16F;

    public List<Triangle> getTriangles(LivingEntity entity, LivingEntity target) {
        float range = this.range(entity, target);
        float radius = this.coneRadius(entity, range);

       return SCUtil.getConeTriangles(range, radius);
    }

    public boolean isVisible(LivingEntity entity, LivingEntity target) {
        float range = this.range(entity, target);
        float radius = this.coneRadius(entity, range);

        Vec3 tip = this.eyePos(entity);
        Vec3 dir = this.direction(entity).normalize();
        return SCUtil.insideCone(target.getEyePosition(), tip, dir, range, radius) && entity.hasLineOfSight(target);
    }

    public float coneRadius(Entity entity, float range) {
        return (float) Math.tan(Math.clamp(this.fov(entity), 0, 179) / (360 / Math.PI)) * (range / (float) Math.PI);
    }

    public float fov(Entity entity) {
        return this.fov().apply(entity);
    }

    public float range(Entity entity) {
        return this.range().apply(entity);
    }

    public float range(Entity entity, LivingEntity target) {
        return this.range(entity) * (target.level() instanceof ServerLevel serverLevel
                ? (float) target.getVisibilityPercent(serverLevel, entity)
                : SCUtil.getPlayerVisibility(entity, target));
    }

    public Vec3 eyePos(Entity entity) {
        return this.eyePos.apply(entity);
    }

    public Vec3 direction(Entity entity) {
        return this.direction.apply(entity);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        Function<Entity, Float> fov = entity -> DEFAULT_FOV;
        Function<Entity, Float> range = entity -> {
            if (entity instanceof LivingEntity living) {
                AttributeInstance range = living.getAttribute(Attributes.FOLLOW_RANGE);

                if (range != null) {
                    return (float) range.getValue();
                }
            }
            return DEFAULT_RANGE;
        };
        Function<Entity, Vec3> eyePos = Entity::getEyePosition;
        Function<Entity, Vec3> direction = Entity::getForward;

        protected Builder() {}

        public Builder fov(float fov) {
            return this.fov(entity -> fov);
        }

        public Builder fov(Function<Entity, Float> fov) {
            this.fov = fov;
            return this;
        }

        public Builder range(float range) {
            return this.range(entity -> range);
        }

        public Builder range(Function<Entity, Float> range) {
            this.range = range;
            return this;
        }

        public Builder eyePos(Function<Entity, Vec3> eyePos) {
            this.eyePos = eyePos;
            return this;
        }

        public Builder direction(Function<Entity, Vec3> direction) {
            this.direction = direction;
            return this;
        }

        public EntityVision build() {
            return new EntityVision(this.fov, this.range, this.eyePos, this.direction);
        }
    }
}
