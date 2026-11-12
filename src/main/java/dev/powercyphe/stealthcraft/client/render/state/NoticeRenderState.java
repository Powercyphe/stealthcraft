package dev.powercyphe.stealthcraft.client.render.state;

import dev.powercyphe.stealthcraft.common.SCConfig;
import dev.powercyphe.stealthcraft.common.entity.EntityVisionHandler;
import dev.powercyphe.stealthcraft.common.util.Quad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;

public class NoticeRenderState {
    public List<Quad> coneQuads = new ArrayList<>();
    public Vec2 coneRotation = Vec2.ZERO;
    public float coneY = 0F;

    public float noticeProgress = 0F;
    public float noticeVisibility = 0F;

    public CameraRenderState cameraState = new CameraRenderState();

    public void extract(LivingEntity entity, EntityRenderState state, float partialTicks) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (entity.getVisionHandler() != null && entity.getVisionHandler().shouldRender(player)) {
            EntityVisionHandler<LivingEntity> handler = entity.getVisionHandler();
            if (handler.coneChanged) {
                handler.coneChanged = false;

                if (SCConfig.coneColorOpacity > 0) {
                    handler.cone = handler.getVision().getQuads(entity, player);
                } else {
                    handler.cone = new ArrayList<>();
                }
            }

            this.coneQuads = handler.cone;
            this.coneRotation = handler.getVision().direction(entity, partialTicks);
            this.coneY = handler.getVision().eyeOffset(entity);

            this.noticeProgress = handler.getNoticeProgress(partialTicks);
            this.noticeVisibility = handler.getNoticeVisibility(partialTicks);
        }
    }

    public static NoticeRenderState get(EntityRenderState state) {
        return ((Getter) state).getNoticeState();
    }

    public interface Getter {
        NoticeRenderState getNoticeState();
    }
}
