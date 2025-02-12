/*
 * Copyright © Wynntils 2022.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.wynn.model.map.poi;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wynntils.features.user.map.MapFeature;
import com.wynntils.gui.render.FontRenderer;
import com.wynntils.gui.render.HorizontalAlignment;
import com.wynntils.gui.render.RenderUtils;
import com.wynntils.gui.render.Texture;
import com.wynntils.gui.render.VerticalAlignment;
import com.wynntils.mc.objects.CommonColors;
import com.wynntils.utils.MathUtils;

public abstract class IconPoi implements Poi {
    @Override
    public int getWidth(float mapZoom, float scale) {
        return (int) (getIcon().width() * scale);
    }

    @Override
    public int getHeight(float mapZoom, float scale) {
        return (int) (getIcon().height() * scale);
    }

    public abstract Texture getIcon();

    // Returns the minimum zoom where the poi should be rendered with full alpha
    // Return -1 to always render without fading
    public abstract float getMinZoomForRender();

    public float getIconAlpha(float zoom) {
        if (getMinZoomForRender() <= -1) return 1f;

        return MathUtils.map(
                zoom, getMinZoomForRender() - MapFeature.INSTANCE.poiFadeDistance, getMinZoomForRender(), 0f, 1f);
    }

    @Override
    public void renderAt(
            PoseStack poseStack, float renderX, float renderZ, boolean hovered, float scale, float mapZoom) {
        float modifier = scale;

        if (hovered) {
            modifier *= 1.05;
        }

        Texture icon = getIcon();

        float width = icon.width() * modifier;
        float height = icon.height() * modifier;

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        float[] colors = RenderSystem.getShaderColor();
        RenderSystem.setShaderColor(colors[0], colors[1], colors[2], getIconAlpha(mapZoom));

        RenderUtils.drawScalingTexturedRect(
                poseStack,
                icon.resource(),
                renderX - width / 2,
                renderZ - height / 2,
                0,
                width,
                height,
                icon.width(),
                icon.height());

        if (hovered) {
            // Render name if hovered

            poseStack.pushPose();

            FontRenderer.getInstance()
                    .renderText(
                            poseStack,
                            getName(),
                            renderX,
                            20 + renderZ,
                            CommonColors.GREEN,
                            HorizontalAlignment.Center,
                            VerticalAlignment.Middle,
                            FontRenderer.TextShadow.OUTLINE);

            poseStack.popPose();
        }

        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
