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

public record EntityVision(Function<Entity, Float> fov, Function<Entity, Float> range, Function<Entity, Float> eyeOffset, BiFunction<Entity, Float, Vec2> direction) {
    public static final EntityVision DEFAULT = EntityVision.builder().build();

    public static final float DEFAULT_FOV = 115F;
    public static final float DEFAULT_RANGE = 16F;

    public List<Quad> getQuads(LivingEntity entity, LivingEntity target) {
        float range = this.range(entity, target);
        float radius = this.coneRadius(entity, range);

       return SCUtil.getConeQuads(entity, this.eyePos(entity), this.direction(entity), range, radius);
    }

    public boolean isVisible(LivingEntity entity, LivingEntity target) {
        float range = this.range(entity, target);
        float radius = this.coneRadius(entity, range);

        Vec3 tip = this.eyePos(entity);
        Vec3 dir = this.direction3(entity);
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
        return entity.position().add(0, this.eyeOffset(entity), 0);
    }

    public float eyeOffset(Entity entity) {
        return this.eyeOffset.apply(entity);
    }

    public Vec3 direction3(Entity entity) {
        return Vec3.directionFromRotation(this.direction(entity));
    }

    public Vec2 direction(Entity entity) {
        return this.direction(entity, 0F);
    }

    public Vec2 direction(Entity entity, float tickProgress) {
        return this.direction.apply(entity, tickProgress);
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
        Function<Entity, Float> eyeOffset = Entity::getEyeHeight;
        BiFunction<Entity, Float, Vec2> direction = (entity, partialTicks) ->
                new Vec2(entity.getViewXRot(partialTicks), entity.getViewYRot(partialTicks));

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

        public Builder eyePos(Function<Entity, Float> eyeOffset) {
            this.eyeOffset = eyeOffset;
            return this;
        }

        public Builder direction(BiFunction<Entity, Float, Vec2> direction) {
            this.direction = direction;
            return this;
        }

        public EntityVision build() {
            return new EntityVision(this.fov, this.range, this.eyeOffset, this.direction);
        }
    }
}
