package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityCaveSpider extends EntitySpider
{

    public EntityCaveSpider(World worldIn)
    {
        super(worldIn);
        this.setSize(0.7F, 0.5F);
    }

    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0D);
    }

    @Override
	public boolean attackEntityAsMob(Entity entityIn)
    {
        if (super.attackEntityAsMob(entityIn))
        {
            if (entityIn instanceof EntityLivingBase)
            {
                byte var2 = 0;

                if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL)
                {
                    var2 = 7;
                }
                else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD)
                {
                    var2 = 15;
                }

                if (var2 > 0)
                {
                    ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(Potion.poison.id, var2 * 20, 0));
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        return livingdata;
    }

    @Override
	public float getEyeHeight()
    {
        return 0.45F;
    }
}
