package dev.powercyphe.stealthcraft.common.init;

import dev.powercyphe.stealthcraft.common.entity.EntityVision;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.world.entity.EntityTypes.*;
import static dev.powercyphe.stealthcraft.common.entity.EntityVisionRegistry.*;

public interface SCEntityVisions {

    static void init() {
        register(ENDER_DRAGON, EntityVision.builder().range(150).fov(145)
                .eyePos(e -> e.position().add(0, e.getBbHeight() / 3, 0))
                .direction(e -> Vec3.directionFromRotation(e.getXRot(), e.getYRot() + 180)).build());
        register(WARDEN, EntityVision.builder().range(0F).build());
    }
}
