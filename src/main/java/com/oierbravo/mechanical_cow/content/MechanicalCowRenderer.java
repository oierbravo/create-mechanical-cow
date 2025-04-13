package com.oierbravo.mechanical_cow.content;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.oierbravo.mechanical_cow.registrate.ModPartials;
import com.oierbravo.mechanicals.foundation.blockEntity.behaviour.CycleBehavior;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class MechanicalCowRenderer extends KineticBlockEntityRenderer<MechanicalCowBlockEntity> {
    public MechanicalCowRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(MechanicalCowBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (VisualizationManager.supportsVisualization(be.getLevel()))
            return;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        BlockState blockState = be.getBlockState();

        CycleBehavior cycleBehavior = be.getCycleBehaviour();

        SuperByteBuffer headRender = CachedBuffers.partialFacing(ModPartials.HEAD, blockState,
                blockState.getValue(HORIZONTAL_FACING).getOpposite());

        float yPos = 0.0f;
        if(cycleBehavior.getProgress(partialTicks) > 0)
            yPos = (float) Math.sin(cycleBehavior.getProgress(partialTicks)) /20;

        headRender.translate(0, .1+ yPos, 0)
                .light(light)
                .renderInto(ms, vb);


        SuperByteBuffer superBuffer = CachedBuffers.partial(ModPartials.COG_HORIZONTAL,blockState);
        standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, vb);
    }
}
