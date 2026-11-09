package dev.powercyphe.stealthcraft.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.powercyphe.stealthcraft.common.entity.EntityVision;
import dev.powercyphe.stealthcraft.common.entity.EntityVisionHandler;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {


    @ModifyReturnValue(method = "shouldRender", at = @At("RETURN"))
    private boolean stealthcraft$submit(boolean original, T entity, Frustum culler) {
        if (entity instanceof LivingEntity living) {
            EntityVisionHandler<LivingEntity> handler = living.getVisionHandler();
            if (handler != null) {
                EntityVision<LivingEntity> vision = handler.getVision();

                double range = vision.range(living);
                AABB coneBox = AABB.ofSize(vision.eyePos(living)
                        .add(vision.direction3(living).scale(range / 2)), range, range, range);
                if (culler.isVisible(coneBox)) {
                    return true;
                }
            }
        }
        return original;
    }
}
