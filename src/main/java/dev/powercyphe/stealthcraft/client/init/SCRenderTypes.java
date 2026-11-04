package dev.powercyphe.stealthcraft.client.init;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

public interface SCRenderTypes {

    RenderType CONE = RenderType.create("cone", RenderSetup.builder(SCPipelines.CONE)
            .setOitPipelines(SCPipelines.OIT_CONE).createRenderSetup());
    RenderType CONE_FILL = RenderType.create("cone_fill", RenderSetup.builder(SCPipelines.CONE_FILL)
            .setOitPipelines(SCPipelines.OIT_CONE_FILL).createRenderSetup());

    RenderType NOTICE = RenderType.create("notice", RenderSetup.builder(SCPipelines.NOTICE)
            .setOitPipelines(SCPipelines.OIT_NOTICE).createRenderSetup());

    static void init() {}

}
