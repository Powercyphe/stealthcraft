package dev.powercyphe.stealthcraft.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.LightCoordsUtil;

public record NoticeRenderer(int color, float scale, CameraRenderState cameraState) implements SubmitNodeCollector.CustomGeometryRenderer {
    @Override
    public void render(PoseStack.Pose pose, VertexConsumer consumer) {
        pose.rotate(Axis.YP.rotationDegrees(this.cameraState.yRot));
        pose.rotate(Axis.XP.rotationDegrees(this.cameraState.xRot));

        pose.translate(0, this.scale, 0);
        face(pose, consumer, this.scale);

        pose.translate(0, -this.scale * 2, 0);
        pose.rotate(Axis.XP.rotationDegrees(-180));

        face(pose, consumer, this.scale);

    }

    public void face(PoseStack.Pose pose, VertexConsumer consumer, float scale) {
        vertex(pose, consumer, -scale, -scale, 0F, 0F);
        vertex(pose, consumer, scale, -scale, 1F, 0F);
        vertex(pose, consumer, 0F, scale, 1F, 1F);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, float x, float y, float u, float v) {
        consumer.addVertex(pose, x, y, 0).setColor(this.color())
                .setLight(LightCoordsUtil.FULL_BRIGHT).setNormal(0, 1, 0);
    }
}
