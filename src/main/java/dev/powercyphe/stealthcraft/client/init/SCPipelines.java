package dev.powercyphe.stealthcraft.client.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.pipeline.*;
import dev.powercyphe.stealthcraft.common.StealthCraft;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.oit.OitPipelineSet;

import static net.minecraft.client.renderer.RenderPipelines.*;

public interface SCPipelines {

    RenderPipeline CONE = register(RenderPipeline.builder(LINES_SNIPPET)
            .withLocation(StealthCraft.id("pipeline/cone"))
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .build());

    OitPipelineSet OIT_CONE = register(OitPipelineSet.builder("cone_fill", RenderPipeline.builder(OIT_LINES_SNIPPET)).build());

    RenderPipeline.Snippet CONE_FILL_SNIPPET = RenderPipeline.builder(MATRICES_FOG_SNIPPET)
            .withVertexShader("core/position_color")
            .withFragmentShader("core/position_color")
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_NORMAL)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .withCull(false)
            .buildSnippet();

    RenderPipeline CONE_FILL = register(RenderPipeline.builder(CONE_FILL_SNIPPET)
            .withLocation(StealthCraft.id("pipeline/cone_fill"))
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .build());

    OitPipelineSet OIT_CONE_FILL = register(OitPipelineSet.builder("cone_fill", RenderPipeline.builder(CONE_FILL_SNIPPET)).build());

    RenderPipeline.Snippet NOTICE_SNIPPET = RenderPipeline.builder(GLOBALS_SNIPPET)
            .withVertexShader("core/position_color")
            .withFragmentShader("core/position_color")
            .withBindGroupLayout(BindGroupLayouts.PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_NORMAL)
            .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
            .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, true))
            .withCull(false)
            .buildSnippet();

    RenderPipeline NOTICE = register(RenderPipeline.builder(NOTICE_SNIPPET)
            .withLocation(StealthCraft.id("pipeline/notice"))
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .build());

    OitPipelineSet OIT_NOTICE = register(OitPipelineSet.builder("notice", RenderPipeline.builder(NOTICE_SNIPPET)).build());

    static void init() {}
}
