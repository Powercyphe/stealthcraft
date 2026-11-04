package dev.powercyphe.stealthcraft.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.powercyphe.stealthcraft.common.SCConfig;
import dev.powercyphe.stealthcraft.common.util.SCUtil;
import dev.powercyphe.stealthcraft.common.util.Triangle;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;

import java.util.List;

public record ConeRenderer(List<Triangle> triangles) implements SubmitNodeCollector.CustomGeometryRenderer {
    @Override
    public void render(PoseStack.Pose pose, VertexConsumer buffer) {
        if (!triangles.isEmpty()) {
            for (Triangle triangle : triangles) {
                triangle.forEach(p ->
                        buffer.addVertex(pose, (float) p.x, (float) p.y, (float) p.z).setColor(ARGB.color(0.3F, SCUtil.parseColor(SCConfig.coneColor)))
                                .setLight(LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0)
                );
            }
        }
    }
}
