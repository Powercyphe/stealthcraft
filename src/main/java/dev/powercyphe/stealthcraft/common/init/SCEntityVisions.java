package dev.powercyphe.stealthcraft.common.init;

import net.minecraft.world.phys.Vec2;

import static net.minecraft.world.entity.EntityTypes.*;
import static dev.powercyphe.stealthcraft.common.entity.EntityVisionRegistry.*;

public interface SCEntityVisions {

    static void init() {
        register(ENDER_DRAGON, builder -> builder
                .range(150).fov(145)
                .eyePos(e -> e.getBbHeight() / 3)
                .direction((e, p) -> new Vec2(e.getViewXRot(p), e.flightHistory.get(7, p).yRot() + 180))
                .build());
        register(WARDEN, builder -> builder.range(0F).build());
    }
}
