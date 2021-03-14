package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySmallFireball extends EntityFireball
{

    public EntitySmallFireball(World worldIn)
    {
        super(worldIn);
        this.setSize(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
        this.setSize(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
        this.setSize(0.3125F, 0.3125F);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
	protected void onImpact(MovingObjectPosition movingObject)
    {
        if (!this.worldObj.isRemote)
        {
            boolean var2;

            if (movingObject.entityHit != null)
            {
                var2 = movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0F);

                if (var2)
                {
                    this.func_174815_a(this.shootingEntity, movingObject.entityHit);

                    if (!movingObject.entityHit.isImmuneToFire())
                    {
                        movingObject.entityHit.setFire(5);
                    }
                }
            }
            else
            {
                var2 = true;

                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving)
                {
                    var2 = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
                }

                if (var2)
                {
                    BlockPos var3 = movingObject.getBlockPos().offset(movingObject.sideHit);

                    if (this.worldObj.isAirBlock(var3))
                    {
                        this.worldObj.setBlockState(var3, Blocks.fire.getDefaultState());
                    }
                }
            }

            this.setDead();
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
	public boolean canBeCollidedWith()
    {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }
}
