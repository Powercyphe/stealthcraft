package dev.powercyphe.stealthcraft.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.powercyphe.stealthcraft.client.init.SCRenderTypes;
import dev.powercyphe.stealthcraft.client.render.ConeRenderer;
import dev.powercyphe.stealthcraft.client.render.NoticeRenderer;
import dev.powercyphe.stealthcraft.client.render.state.NoticeRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.ARGB;

public class NoticeRenderLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
    public NoticeRenderLayer(RenderLayerParent<S, M> parent) {
        super(parent);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submit, int lightCoords, S state, float yRot, float xRot) {
        NoticeRenderState noticeState = NoticeRenderState.get(state);

        poseStack.pushPose();
        poseStack.translate(0, 1.501F, 0);

        poseStack.pushPose();
        poseStack.rotateDegrees(Axis.YP, -state.bodyRot);
        poseStack.scale(1, -1, -1);

        poseStack.rotateAround(Axis.YP.rotationDegrees(-noticeState.coneRotation.y)
                        .mul(Axis.XP.rotationDegrees(-noticeState.coneRotation.x)),
                0F, noticeState.coneY, 0F);

        // USED FOR DEBUG
        // submit.submitCustomGeometry(poseStack, SCRenderTypes.CONE, new ConeOutlineRenderer(noticeState.entityCone));
        submit.submitCustomGeometry(poseStack, SCRenderTypes.CONE_FILL, new ConeRenderer(noticeState.coneQuads));
        poseStack.popPose();

        poseStack.rotateDegrees(Axis.YN, state.bodyRot);
        poseStack.translate(0, -state.boundingBoxHeight * 1.75, 0);

        submit.submitCustomGeometry(poseStack, SCRenderTypes.NOTICE,
                new NoticeRenderer(
                        ARGB.linearLerp(noticeState.noticeProgress, 0xFF33FF00, 0xFFFF3300),
                        0.21F * noticeState.noticeVisibility, noticeState.cameraState
                )
        );
        poseStack.popPose();
    }
}
