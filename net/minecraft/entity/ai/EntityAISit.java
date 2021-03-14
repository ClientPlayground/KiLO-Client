package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAISit extends EntityAIBase
{
    private EntityTameable theEntity;

    /** If the EntityTameable is sitting. */
    private boolean isSitting;

    public EntityAISit(EntityTameable entityIn)
    {
        this.theEntity = entityIn;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute()
    {
        if (!this.theEntity.isTamed())
        {
            return false;
        }
        else if (this.theEntity.isInWater())
        {
            return false;
        }
        else if (!this.theEntity.onGround)
        {
            return false;
        }
        else
        {
            EntityLivingBase var1 = this.theEntity.getOwnerEntity();
            return var1 == null ? true : (this.theEntity.getDistanceSqToEntity(var1) < 144.0D && var1.getAITarget() != null ? false : this.isSitting);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void startExecuting()
    {
        this.theEntity.getNavigator().clearPathEntity();
        this.theEntity.setSitting(true);
    }

    /**
     * Resets the task
     */
    @Override
	public void resetTask()
    {
        this.theEntity.setSitting(false);
    }

    /**
     * Sets the sitting flag.
     */
    public void setSitting(boolean sitting)
    {
        this.isSitting = sitting;
    }
}
