package net.minecraft.entity;

import java.util.UUID;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class EntityCreature extends EntityLiving
{
    public static final UUID field_110179_h = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
    public static final AttributeModifier FLEEING_SPEED_MODIFIER = (new AttributeModifier(field_110179_h, "Fleeing speed bonus", 2.0D, 2)).setSaved(false);
    private BlockPos homePosition;

    /** If -1 there is no maximum distance */
    private float maximumHomeDistance;
    private EntityAIBase aiBase;
    private boolean isMovementAITaskSet;

    public EntityCreature(World worldIn)
    {
        super(worldIn);
        this.homePosition = BlockPos.ORIGIN;
        this.maximumHomeDistance = -1.0F;
        this.aiBase = new EntityAIMoveTowardsRestriction(this, 1.0D);
    }

    public float getBlockPathWeight(BlockPos pos)
    {
        return 0.0F;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
	public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() && this.getBlockPathWeight(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) >= 0.0F;
    }

    /**
     * if the entity got a PathEntity it returns true, else false
     */
    public boolean hasPath()
    {
        return !this.navigator.noPath();
    }

    public boolean isWithinHomeDistanceCurrentPosition()
    {
        return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
    }

    public boolean isWithinHomeDistanceFromPosition(BlockPos pos)
    {
        return this.maximumHomeDistance == -1.0F ? true : this.homePosition.distanceSq(pos) < this.maximumHomeDistance * this.maximumHomeDistance;
    }

    /**
     * Sets home position and max distance for it
     */
    public void setHomePosAndDistance(BlockPos pos, int distance)
    {
        this.homePosition = pos;
        this.maximumHomeDistance = distance;
    }

    public BlockPos getHomePosition()
    {
        return this.homePosition;
    }

    public float getMaximumHomeDistance()
    {
        return this.maximumHomeDistance;
    }

    public void detachHome()
    {
        this.maximumHomeDistance = -1.0F;
    }

    /**
     * Returns whether a home area is defined for this entity.
     */
    public boolean hasHome()
    {
        return this.maximumHomeDistance != -1.0F;
    }

    /**
     * Applies logic related to leashes, for example dragging the entity or breaking the leash.
     */
    @Override
	protected void updateLeashedState()
    {
        super.updateLeashedState();

        if (this.getLeashed() && this.getLeashedToEntity() != null && this.getLeashedToEntity().worldObj == this.worldObj)
        {
            Entity var1 = this.getLeashedToEntity();
            this.setHomePosAndDistance(new BlockPos((int)var1.posX, (int)var1.posY, (int)var1.posZ), 5);
            float var2 = this.getDistanceToEntity(var1);

            if (this instanceof EntityTameable && ((EntityTameable)this).isSitting())
            {
                if (var2 > 10.0F)
                {
                    this.clearLeashed(true, true);
                }

                return;
            }

            if (!this.isMovementAITaskSet)
            {
                this.tasks.addTask(2, this.aiBase);

                if (this.getNavigator() instanceof PathNavigateGround)
                {
                    ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
                }

                this.isMovementAITaskSet = true;
            }

            this.func_142017_o(var2);

            if (var2 > 4.0F)
            {
                this.getNavigator().tryMoveToEntityLiving(var1, 1.0D);
            }

            if (var2 > 6.0F)
            {
                double var3 = (var1.posX - this.posX) / var2;
                double var5 = (var1.posY - this.posY) / var2;
                double var7 = (var1.posZ - this.posZ) / var2;
                this.motionX += var3 * Math.abs(var3) * 0.4D;
                this.motionY += var5 * Math.abs(var5) * 0.4D;
                this.motionZ += var7 * Math.abs(var7) * 0.4D;
            }

            if (var2 > 10.0F)
            {
                this.clearLeashed(true, true);
            }
        }
        else if (!this.getLeashed() && this.isMovementAITaskSet)
        {
            this.isMovementAITaskSet = false;
            this.tasks.removeTask(this.aiBase);

            if (this.getNavigator() instanceof PathNavigateGround)
            {
                ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
            }

            this.detachHome();
        }
    }

    protected void func_142017_o(float p_142017_1_) {}
}
