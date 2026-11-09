package dev.powercyphe.stealthcraft.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.powercyphe.stealthcraft.client.init.SCRenderTypes;
import dev.powercyphe.stealthcraft.client.render.ConeRenderer;
import dev.powercyphe.stealthcraft.client.render.NoticeRenderer;
import dev.powercyphe.stealthcraft.client.render.state.NoticeRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonRenderer.class)
public abstract class EnderDragonRendererMixin extends EntityRenderer<EnderDragon, EnderDragonRenderState> {
    protected EnderDragonRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;F)V", at = @At("TAIL"))
    private void stealthcraft$extractState(EnderDragon entity, EnderDragonRenderState state, float partialTicks, CallbackInfo ci) {
        NoticeRenderState.get(state).extract(entity, state, partialTicks);
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At("HEAD"))
    private void stealthcraft$enderDragonCone(EnderDragonRenderState state, PoseStack poseStack, SubmitNodeCollector submit, CameraRenderState camera, CallbackInfo ci) {
        NoticeRenderState noticeState = NoticeRenderState.get(state);
        noticeState.cameraState = camera;
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/EnderDragonRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At("TAIL"))
    private void stealthcraft$enderDragonNotice(EnderDragonRenderState state, PoseStack poseStack, SubmitNodeCollector submit, CameraRenderState camera, CallbackInfo ci,
                                                @Local(name = "yr", type = float.class) float yr, @Local(name = "rot2", type = float.class) float rot2) {
        NoticeRenderState noticeState = NoticeRenderState.get(state);

        poseStack.pushPose();
        poseStack.rotateAround(Axis.YP.rotationDegrees(-noticeState.coneRotation.y)
                        .mul(Axis.XP.rotationDegrees(-noticeState.coneRotation.x)),
                0F, noticeState.coneY, 0F);

        submit.submitCustomGeometry(poseStack, SCRenderTypes.CONE_FILL, new ConeRenderer(noticeState.coneQuads));

        poseStack.popPose();
        poseStack.pushPose();

        poseStack.scale(-1, 1, 1);
        poseStack.translate(0, state.boundingBoxHeight * 1.75, 0);

        submit.submitCustomGeometry(poseStack, SCRenderTypes.NOTICE,
                new NoticeRenderer(
                        ARGB.linearLerp(noticeState.noticeProgress, 0xFF33FF00, 0xFFFF3300),
                        noticeState.noticeVisibility, noticeState.cameraState
                )
        );
        poseStack.popPose();
    }
}
