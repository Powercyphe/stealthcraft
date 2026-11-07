package dev.powercyphe.stealthcraft.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.powercyphe.stealthcraft.common.SCConfig;
import dev.powercyphe.stealthcraft.common.util.Quad;
import dev.powercyphe.stealthcraft.common.util.SCUtil;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.phys.Vec3;
import oshi.util.tuples.Pair;

import java.util.List;

public record ConeOutlineRenderer(List<Quad> quads) implements SubmitNodeCollector.CustomGeometryRenderer {
    @Override
    public void render(PoseStack.Pose pose, VertexConsumer buffer) {
        if (!quads.isEmpty()) {
            for (Quad quad: quads) {
                quad.forEach(p ->
                        buffer.addVertex(pose, (float) p.x, (float) p.y, (float) p.z).setColor(ARGB.color(SCConfig.coneColorOpacity, SCUtil.parseColor(SCConfig.coneColor)))
                                .setLight(LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0).setLineWidth(6F)
                );
            }
        }
    }
}
