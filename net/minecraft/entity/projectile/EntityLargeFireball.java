package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityLargeFireball extends EntityFireball
{
    public int explosionPower = 1;

    public EntityLargeFireball(World worldIn)
    {
        super(worldIn);
    }

    public EntityLargeFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
    }

    public EntityLargeFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
	protected void onImpact(MovingObjectPosition movingObject)
    {
        if (!this.worldObj.isRemote)
        {
            if (movingObject.entityHit != null)
            {
                movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F);
                this.func_174815_a(this.shootingEntity, movingObject.entityHit);
            }

            boolean var2 = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
            this.worldObj.newExplosion((Entity)null, this.posX, this.posY, this.posZ, this.explosionPower, var2, var2);
            this.setDead();
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("ExplosionPower", this.explosionPower);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.hasKey("ExplosionPower", 99))
        {
            this.explosionPower = tagCompund.getInteger("ExplosionPower");
        }
    }
}
