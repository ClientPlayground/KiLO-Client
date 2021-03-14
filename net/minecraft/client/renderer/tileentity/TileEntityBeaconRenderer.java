package net.minecraft.client.renderer.tileentity;

import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class TileEntityBeaconRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");

    /**
     * Renders the TileEntityBeacon, including the beam segments.
     */
    public void renderBeacon(TileEntityBeacon entityBeacon, double p_180536_2_, double p_180536_4_, double p_180536_6_, float p_180536_8_, int p_180536_9_)
    {
        float var10 = entityBeacon.shouldBeamRender();
        GlStateManager.alphaFunc(516, 0.1F);

        if (var10 > 0.0F)
        {
            Tessellator var11 = Tessellator.getInstance();
            WorldRenderer var12 = var11.getWorldRenderer();
            List var13 = entityBeacon.func_174907_n();
            int var14 = 0;

            for (int var15 = 0; var15 < var13.size(); ++var15)
            {
                TileEntityBeacon.BeamSegment var16 = (TileEntityBeacon.BeamSegment)var13.get(var15);
                int var17 = var14 + var16.func_177264_c();
                this.bindTexture(beaconBeam);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(true);
                GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
                float var18 = entityBeacon.getWorld().getTotalWorldTime() + p_180536_8_;
                float var19 = -var18 * 0.2F - MathHelper.floor_float(-var18 * 0.1F);
                double var20 = var18 * 0.025D * -1.5D;
                var12.startDrawingQuads();
                double var22 = 0.2D;
                double var24 = 0.5D + Math.cos(var20 + 2.356194490192345D) * var22;
                double var26 = 0.5D + Math.sin(var20 + 2.356194490192345D) * var22;
                double var28 = 0.5D + Math.cos(var20 + (Math.PI / 4D)) * var22;
                double var30 = 0.5D + Math.sin(var20 + (Math.PI / 4D)) * var22;
                double var32 = 0.5D + Math.cos(var20 + 3.9269908169872414D) * var22;
                double var34 = 0.5D + Math.sin(var20 + 3.9269908169872414D) * var22;
                double var36 = 0.5D + Math.cos(var20 + 5.497787143782138D) * var22;
                double var38 = 0.5D + Math.sin(var20 + 5.497787143782138D) * var22;
                double var40 = 0.0D;
                double var42 = 1.0D;
                double var44 = -1.0F + var19;
                double var46 = var16.func_177264_c() * var10 * (0.5D / var22) + var44;
                var12.setColorRGBA_F(var16.func_177263_b()[0], var16.func_177263_b()[1], var16.func_177263_b()[2], 0.125F);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var17, p_180536_6_ + var26, var42, var46);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var14, p_180536_6_ + var26, var42, var44);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + var14, p_180536_6_ + var30, var40, var44);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + var17, p_180536_6_ + var30, var40, var46);
                var12.addVertexWithUV(p_180536_2_ + var36, p_180536_4_ + var17, p_180536_6_ + var38, var42, var46);
                var12.addVertexWithUV(p_180536_2_ + var36, p_180536_4_ + var14, p_180536_6_ + var38, var42, var44);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + var14, p_180536_6_ + var34, var40, var44);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + var17, p_180536_6_ + var34, var40, var46);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + var17, p_180536_6_ + var30, var42, var46);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + var14, p_180536_6_ + var30, var42, var44);
                var12.addVertexWithUV(p_180536_2_ + var36, p_180536_4_ + var14, p_180536_6_ + var38, var40, var44);
                var12.addVertexWithUV(p_180536_2_ + var36, p_180536_4_ + var17, p_180536_6_ + var38, var40, var46);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + var17, p_180536_6_ + var34, var42, var46);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + var14, p_180536_6_ + var34, var42, var44);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var14, p_180536_6_ + var26, var40, var44);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var17, p_180536_6_ + var26, var40, var46);
                var11.draw();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.depthMask(false);
                var12.startDrawingQuads();
                var12.setColorRGBA_F(var16.func_177263_b()[0], var16.func_177263_b()[1], var16.func_177263_b()[2], 0.125F);
                var20 = 0.2D;
                var22 = 0.2D;
                var24 = 0.8D;
                var26 = 0.2D;
                var28 = 0.2D;
                var30 = 0.8D;
                var32 = 0.8D;
                var34 = 0.8D;
                var36 = 0.0D;
                var38 = 1.0D;
                var40 = -1.0F + var19;
                var42 = var16.func_177264_c() * var10 + var40;
                var12.addVertexWithUV(p_180536_2_ + var20, p_180536_4_ + var17, p_180536_6_ + var22, var38, var42);
                var12.addVertexWithUV(p_180536_2_ + var20, p_180536_4_ + var14, p_180536_6_ + var22, var38, var40);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var14, p_180536_6_ + var26, var36, var40);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var17, p_180536_6_ + var26, var36, var42);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + var17, p_180536_6_ + var34, var38, var42);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + var14, p_180536_6_ + var34, var38, var40);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + var14, p_180536_6_ + var30, var36, var40);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + var17, p_180536_6_ + var30, var36, var42);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var17, p_180536_6_ + var26, var38, var42);
                var12.addVertexWithUV(p_180536_2_ + var24, p_180536_4_ + var14, p_180536_6_ + var26, var38, var40);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + var14, p_180536_6_ + var34, var36, var40);
                var12.addVertexWithUV(p_180536_2_ + var32, p_180536_4_ + var17, p_180536_6_ + var34, var36, var42);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + var17, p_180536_6_ + var30, var38, var42);
                var12.addVertexWithUV(p_180536_2_ + var28, p_180536_4_ + var14, p_180536_6_ + var30, var38, var40);
                var12.addVertexWithUV(p_180536_2_ + var20, p_180536_4_ + var14, p_180536_6_ + var22, var36, var40);
                var12.addVertexWithUV(p_180536_2_ + var20, p_180536_4_ + var17, p_180536_6_ + var22, var36, var42);
                var11.draw();
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
                GlStateManager.depthMask(true);
                var14 = var17;
            }
        }
    }

    @Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        this.renderBeacon((TileEntityBeacon)te, x, y, z, partialTicks, destroyStage);
    }
}
