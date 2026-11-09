package dev.powercyphe.stealthcraft.common.entity;

import net.minecraft.world.entity.LivingEntity;

public interface VisionHandlerHolder<E extends LivingEntity> {

    EntityVisionHandler<E> getVisionHandler();
}
