package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.util.ResourceLocation;

public class RenderGiantZombie extends RenderLiving
{
    private static final ResourceLocation zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");

    /** Scale of the model to use */
    private float scale;

    public RenderGiantZombie(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn, float scaleIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn * scaleIn);
        this.scale = scaleIn;
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this)
        {
            @Override
			protected void func_177177_a()
            {
                this.field_177189_c = new ModelZombie(0.5F, true);
                this.field_177186_d = new ModelZombie(1.0F, true);
            }
        });
    }

    @Override
	public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityGiantZombie entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(this.scale, this.scale, this.scale);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityGiantZombie entity)
    {
        return zombieTextures;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    @Override
	protected void preRenderCallback(EntityLivingBase entitylivingbaseIn, float partialTickTime)
    {
        this.preRenderCallback((EntityGiantZombie)entitylivingbaseIn, partialTickTime);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityGiantZombie)entity);
    }
}
