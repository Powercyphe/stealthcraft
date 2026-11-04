package dev.powercyphe.stealthcraft.client.render.state;

import dev.powercyphe.stealthcraft.common.util.Triangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class NoticeRenderState {
    public List<Triangle> entityCone = new ArrayList<>();

    public float noticeProgress = 0F;
    public float noticeVisibility = 0F;

    public CameraRenderState cameraState = new CameraRenderState();

    public void extract(LivingEntity entity, EntityRenderState state, float partialTicks) {
        if (entity.getVisionHandler() != null) {
            this.entityCone = entity.getVisionHandler().getVision().getTriangles(entity, Minecraft.getInstance().player);

            this.noticeProgress = entity.getVisionHandler().getNoticeProgress(partialTicks);
            this.noticeVisibility = entity.getVisionHandler().getNoticeVisibility(partialTicks);
        }
    }

    public static NoticeRenderState get(EntityRenderState state) {
        return ((Getter) state).getNoticeState();
    }

    public interface Getter {
        NoticeRenderState getNoticeState();
    }
}
