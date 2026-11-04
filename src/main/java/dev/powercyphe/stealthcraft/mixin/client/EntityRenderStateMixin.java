package dev.powercyphe.stealthcraft.mixin.client;

import dev.powercyphe.stealthcraft.client.render.state.NoticeRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements NoticeRenderState.Getter {

    @Unique
    private final NoticeRenderState state = new NoticeRenderState();

    @Override
    public NoticeRenderState getNoticeState() {
        return this.state;
    }
}
