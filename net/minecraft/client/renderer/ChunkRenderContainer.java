package net.minecraft.client.renderer;

import java.util.List;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;

import com.google.common.collect.Lists;

public abstract class ChunkRenderContainer
{
    private double viewEntityX;
    private double viewEntityY;
    private double viewEntityZ;
    protected List renderChunks = Lists.newArrayListWithCapacity(17424);
    protected boolean initialized;

    public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn)
    {
        this.initialized = true;
        this.renderChunks.clear();
        this.viewEntityX = viewEntityXIn;
        this.viewEntityY = viewEntityYIn;
        this.viewEntityZ = viewEntityZIn;
    }

    public void preRenderChunk(RenderChunk renderChunkIn)
    {
        BlockPos var2 = renderChunkIn.getPosition();
        GlStateManager.translate((float)(var2.getX() - this.viewEntityX), (float)(var2.getY() - this.viewEntityY), (float)(var2.getZ() - this.viewEntityZ));
    }

    public void addRenderChunk(RenderChunk renderChunkIn, EnumWorldBlockLayer layer)
    {
        this.renderChunks.add(renderChunkIn);
    }

    public abstract void renderChunkLayer(EnumWorldBlockLayer var1);
}
