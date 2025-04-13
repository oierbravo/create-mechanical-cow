package com.oierbravo.mechanical_cow.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.oierbravo.mechanical_cow.registrate.ModBlocks;
import com.oierbravo.mechanical_cow.registrate.ModPartials;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class AnimatedCow extends AnimatedKinetics {
    public int offset = 1;
    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        PoseStack matrixStack = guiGraphics.pose();

        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        int scale = 24;

        blockElement(ModBlocks.MECHANICAL_COW.getDefaultState())
                .scale(scale)
                .rotate(0,-75,0)
                .render(guiGraphics);

        blockElement(ModPartials.COG_HORIZONTAL)
                .scale(scale)
                .rotate(0,-75,0)
                .render(guiGraphics);

        blockElement(ModPartials.HEAD)
                .atLocal(0, -.1f + getAnimatedHeadOffset() /5, 0)
                .scale(scale)
                .rotate(0,-75,0)
                .render(guiGraphics);

        matrixStack.popPose();
    }
    private float getAnimatedHeadOffset() {
        float cycle = (AnimationTickHolder.getRenderTime() - offset * 4) % 30;
        float progress = cycle / 10;
        float sin = (float) Math.sin(progress);
        float clamp = Mth.clamp(sin,-1,1);
        return clamp;
    }

}
