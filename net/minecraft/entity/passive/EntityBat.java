package net.minecraft.entity.passive;

import java.util.Calendar;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBat extends EntityAmbientCreature
{
    /** Coordinates of where the bat spawned. */
    private BlockPos spawnPosition;

    public EntityBat(World worldIn)
    {
        super(worldIn);
        this.setSize(0.5F, 0.9F);
        this.setIsBatHanging(true);
    }

    @Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
	protected float getSoundVolume()
    {
        return 0.1F;
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    @Override
	protected float getSoundPitch()
    {
        return super.getSoundPitch() * 0.95F;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
	protected String getLivingSound()
    {
        return this.getIsBatHanging() && this.rand.nextInt(4) != 0 ? null : "mob.bat.idle";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
	protected String getHurtSound()
    {
        return "mob.bat.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
	protected String getDeathSound()
    {
        return "mob.bat.death";
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
	public boolean canBePushed()
    {
        return false;
    }

    @Override
	protected void collideWithEntity(Entity p_82167_1_) {}

    @Override
	protected void collideWithNearbyEntities() {}

    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0D);
    }

    public boolean getIsBatHanging()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setIsBatHanging(boolean isHanging)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (isHanging)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 1)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -2)));
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
	public void onUpdate()
    {
        super.onUpdate();

        if (this.getIsBatHanging())
        {
            this.motionX = this.motionY = this.motionZ = 0.0D;
            this.posY = MathHelper.floor_double(this.posY) + 1.0D - this.height;
        }
        else
        {
            this.motionY *= 0.6000000238418579D;
        }
    }

    @Override
	protected void updateAITasks()
    {
        super.updateAITasks();
        BlockPos var1 = new BlockPos(this);
        BlockPos var2 = var1.up();

        if (this.getIsBatHanging())
        {
            if (!this.worldObj.getBlockState(var2).getBlock().isNormalCube())
            {
                this.setIsBatHanging(false);
                this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1015, var1, 0);
            }
            else
            {
                if (this.rand.nextInt(200) == 0)
                {
                    this.rotationYawHead = this.rand.nextInt(360);
                }

                if (this.worldObj.getClosestPlayerToEntity(this, 4.0D) != null)
                {
                    this.setIsBatHanging(false);
                    this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1015, var1, 0);
                }
            }
        }
        else
        {
            if (this.spawnPosition != null && (!this.worldObj.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1))
            {
                this.spawnPosition = null;
            }

            if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq(((int)this.posX), ((int)this.posY), ((int)this.posZ)) < 4.0D)
            {
                this.spawnPosition = new BlockPos((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
            }

            double var3 = this.spawnPosition.getX() + 0.5D - this.posX;
            double var5 = this.spawnPosition.getY() + 0.1D - this.posY;
            double var7 = this.spawnPosition.getZ() + 0.5D - this.posZ;
            this.motionX += (Math.signum(var3) * 0.5D - this.motionX) * 0.10000000149011612D;
            this.motionY += (Math.signum(var5) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
            this.motionZ += (Math.signum(var7) * 0.5D - this.motionZ) * 0.10000000149011612D;
            float var9 = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) - 90.0F;
            float var10 = MathHelper.wrapAngleTo180_float(var9 - this.rotationYaw);
            this.moveForward = 0.5F;
            this.rotationYaw += var10;

            if (this.rand.nextInt(100) == 0 && this.worldObj.getBlockState(var2).getBlock().isNormalCube())
            {
                this.setIsBatHanging(true);
            }
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    @Override
	protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
	public void fall(float distance, float damageMultiplier) {}

    @Override
	protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {}

    /**
     * Return whether this entity should NOT trigger a pressure plate or a tripwire.
     */
    @Override
	public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            if (!this.worldObj.isRemote && this.getIsBatHanging())
            {
                this.setIsBatHanging(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.dataWatcher.updateObject(16, Byte.valueOf(tagCompund.getByte("BatFlags")));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setByte("BatFlags", this.dataWatcher.getWatchableObjectByte(16));
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
	public boolean getCanSpawnHere()
    {
        BlockPos var1 = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

        if (var1.getY() >= 63)
        {
            return false;
        }
        else
        {
            int var2 = this.worldObj.getLightFromNeighbors(var1);
            byte var3 = 4;

            if (this.func_175569_a(this.worldObj.getCurrentDate()))
            {
                var3 = 7;
            }
            else if (this.rand.nextBoolean())
            {
                return false;
            }

            return var2 > this.rand.nextInt(var3) ? false : super.getCanSpawnHere();
        }
    }

    private boolean func_175569_a(Calendar p_175569_1_)
    {
        return p_175569_1_.get(2) + 1 == 10 && p_175569_1_.get(5) >= 20 || p_175569_1_.get(2) + 1 == 11 && p_175569_1_.get(5) <= 3;
    }

    @Override
	public float getEyeHeight()
    {
        return this.height / 2.0F;
    }
}
