package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.google.common.base.Predicate;

public class EntityRabbit extends EntityAnimal
{
    private EntityRabbit.AIAvoidEntity aiAvoidWolves;
    private int field_175540_bm = 0;
    private int field_175535_bn = 0;
    private boolean field_175536_bo = false;
    private boolean field_175537_bp = false;
    private int currentMoveTypeDuration = 0;
    private EntityRabbit.EnumMoveType moveType;
    private int carrotTicks;
    public EntityRabbit(World worldIn)
    {
        super(worldIn);
        this.moveType = EntityRabbit.EnumMoveType.HOP;
        this.carrotTicks = 0;
        this.setSize(0.6F, 0.7F);
        this.jumpHelper = new EntityRabbit.RabbitJumpHelper(this);
        this.moveHelper = new EntityRabbit.RabbitMoveHelper();
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.navigator.func_179678_a(2.5F);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityRabbit.AIPanic(1.33D));
        this.tasks.addTask(2, new EntityAITempt(this, 1.0D, Items.carrot, false));
        this.tasks.addTask(3, new EntityAIMate(this, 0.8D));
        this.tasks.addTask(5, new EntityRabbit.AIRaidFarm());
        this.tasks.addTask(5, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        this.aiAvoidWolves = new EntityRabbit.AIAvoidEntity(new Predicate()
        {
            public boolean isApplicable(Entity entityIn)
            {
                return entityIn instanceof EntityWolf;
            }
            @Override
			public boolean apply(Object p_apply_1_)
            {
                return this.isApplicable((Entity)p_apply_1_);
            }
        }, 16.0F, 1.33D, 1.33D);
        this.tasks.addTask(4, this.aiAvoidWolves);
        this.setMovementSpeed(0.0D);
    }

    @Override
	protected float func_175134_bD()
    {
        return this.moveHelper.isUpdating() && this.moveHelper.getY() > this.posY + 0.5D ? 0.5F : this.moveType.func_180074_b();
    }

    public void setMoveType(EntityRabbit.EnumMoveType type)
    {
        this.moveType = type;
    }

    public float func_175521_o(float p_175521_1_)
    {
        return this.field_175535_bn == 0 ? 0.0F : (this.field_175540_bm + p_175521_1_) / this.field_175535_bn;
    }

    public void setMovementSpeed(double newSpeed)
    {
        this.getNavigator().setSpeed(newSpeed);
        this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), newSpeed);
    }

    public void setJumping(boolean jump, EntityRabbit.EnumMoveType moveTypeIn)
    {
        super.setJumping(jump);

        if (!jump)
        {
            if (this.moveType == EntityRabbit.EnumMoveType.ATTACK)
            {
                this.moveType = EntityRabbit.EnumMoveType.HOP;
            }
        }
        else
        {
            this.setMovementSpeed(1.5D * moveTypeIn.getSpeed());
            this.playSound(this.getJumpingSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }

        this.field_175536_bo = jump;
    }

    public void doMovementAction(EntityRabbit.EnumMoveType movetype)
    {
        this.setJumping(true, movetype);
        this.field_175535_bn = movetype.func_180073_d();
        this.field_175540_bm = 0;
    }

    public boolean func_175523_cj()
    {
        return this.field_175536_bo;
    }

    @Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
    }

    @Override
	public void updateAITasks()
    {
        if (this.moveHelper.getSpeed() > 0.8D)
        {
            this.setMoveType(EntityRabbit.EnumMoveType.SPRINT);
        }
        else if (this.moveType != EntityRabbit.EnumMoveType.ATTACK)
        {
            this.setMoveType(EntityRabbit.EnumMoveType.HOP);
        }

        if (this.currentMoveTypeDuration > 0)
        {
            --this.currentMoveTypeDuration;
        }

        if (this.carrotTicks > 0)
        {
            this.carrotTicks -= this.rand.nextInt(3);

            if (this.carrotTicks < 0)
            {
                this.carrotTicks = 0;
            }
        }

        if (this.onGround)
        {
            if (!this.field_175537_bp)
            {
                this.setJumping(false, EntityRabbit.EnumMoveType.NONE);
                this.func_175517_cu();
            }

            if (this.getRabbitType() == 99 && this.currentMoveTypeDuration == 0)
            {
                EntityLivingBase var1 = this.getAttackTarget();

                if (var1 != null && this.getDistanceSqToEntity(var1) < 16.0D)
                {
                    this.calculateRotationYaw(var1.posX, var1.posZ);
                    this.moveHelper.setMoveTo(var1.posX, var1.posY, var1.posZ, this.moveHelper.getSpeed());
                    this.doMovementAction(EntityRabbit.EnumMoveType.ATTACK);
                    this.field_175537_bp = true;
                }
            }

            EntityRabbit.RabbitJumpHelper var4 = (EntityRabbit.RabbitJumpHelper)this.jumpHelper;

            if (!var4.getIsJumping())
            {
                if (this.moveHelper.isUpdating() && this.currentMoveTypeDuration == 0)
                {
                    PathEntity var2 = this.navigator.getPath();
                    Vec3 var3 = new Vec3(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());

                    if (var2 != null && var2.getCurrentPathIndex() < var2.getCurrentPathLength())
                    {
                        var3 = var2.getPosition(this);
                    }

                    this.calculateRotationYaw(var3.xCoord, var3.zCoord);
                    this.doMovementAction(this.moveType);
                }
            }
            else if (!var4.func_180065_d())
            {
                this.func_175518_cr();
            }
        }

        this.field_175537_bp = this.onGround;
    }

    /**
     * Attempts to create sprinting particles if the entity is sprinting and not in water.
     */
    @Override
	public void spawnRunningParticles() {}

    private void calculateRotationYaw(double p_175533_1_, double p_175533_3_)
    {
        this.rotationYaw = (float)(Math.atan2(p_175533_3_ - this.posZ, p_175533_1_ - this.posX) * 180.0D / Math.PI) - 90.0F;
    }

    private void func_175518_cr()
    {
        ((EntityRabbit.RabbitJumpHelper)this.jumpHelper).func_180066_a(true);
    }

    private void func_175520_cs()
    {
        ((EntityRabbit.RabbitJumpHelper)this.jumpHelper).func_180066_a(false);
    }

    private void updateMoveTypeDuration()
    {
        this.currentMoveTypeDuration = this.getMoveTypeDuration();
    }

    private void func_175517_cu()
    {
        this.updateMoveTypeDuration();
        this.func_175520_cs();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
	public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.field_175540_bm != this.field_175535_bn)
        {
            if (this.field_175540_bm == 0 && !this.worldObj.isRemote)
            {
                this.worldObj.setEntityState(this, (byte)1);
            }

            ++this.field_175540_bm;
        }
        else if (this.field_175535_bn != 0)
        {
            this.field_175540_bm = 0;
            this.field_175535_bn = 0;
        }
    }

    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("RabbitType", this.getRabbitType());
        tagCompound.setInteger("MoreCarrotTicks", this.carrotTicks);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.setRabbitType(tagCompund.getInteger("RabbitType"));
        this.carrotTicks = tagCompund.getInteger("MoreCarrotTicks");
    }

    protected String getJumpingSound()
    {
        return "mob.rabbit.hop";
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
	protected String getLivingSound()
    {
        return "mob.rabbit.idle";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
	protected String getHurtSound()
    {
        return "mob.rabbit.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
	protected String getDeathSound()
    {
        return "mob.rabbit.death";
    }

    @Override
	public boolean attackEntityAsMob(Entity entityIn)
    {
        if (this.getRabbitType() == 99)
        {
            this.playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0F);
        }
        else
        {
            return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
        }
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    @Override
	public int getTotalArmorValue()
    {
        return this.getRabbitType() == 99 ? 8 : super.getTotalArmorValue();
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return this.isEntityInvulnerable(source) ? false : super.attackEntityFrom(source, amount);
    }

    /**
     * Causes this Entity to drop a random item.
     */
    @Override
	protected void addRandomDrop()
    {
        this.entityDropItem(new ItemStack(Items.rabbit_foot, 1), 0.0F);
    }

    /**
     * Drop 0-2 items of this living's type
     */
    @Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        int var3 = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_);
        int var4;

        for (var4 = 0; var4 < var3; ++var4)
        {
            this.dropItem(Items.rabbit_hide, 1);
        }

        var3 = this.rand.nextInt(2);

        for (var4 = 0; var4 < var3; ++var4)
        {
            if (this.isBurning())
            {
                this.dropItem(Items.cooked_rabbit, 1);
            }
            else
            {
                this.dropItem(Items.rabbit, 1);
            }
        }
    }

    private boolean isRabbitBreedingItem(Item itemIn)
    {
        return itemIn == Items.carrot || itemIn == Items.golden_carrot || itemIn == Item.getItemFromBlock(Blocks.yellow_flower);
    }

    public EntityRabbit createChildRabbit(EntityAgeable entityageableIn)
    {
        EntityRabbit var2 = new EntityRabbit(this.worldObj);

        if (entityageableIn instanceof EntityRabbit)
        {
            var2.setRabbitType(this.rand.nextBoolean() ? this.getRabbitType() : ((EntityRabbit)entityageableIn).getRabbitType());
        }

        return var2;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    @Override
	public boolean isBreedingItem(ItemStack stack)
    {
        return stack != null && this.isRabbitBreedingItem(stack.getItem());
    }

    public int getRabbitType()
    {
        return this.dataWatcher.getWatchableObjectByte(18);
    }

    public void setRabbitType(int rabbitTypeId)
    {
        if (rabbitTypeId == 99)
        {
            this.tasks.removeTask(this.aiAvoidWolves);
            this.tasks.addTask(4, new EntityRabbit.AIEvilAttack());
            this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityWolf.class, true));

            if (!this.hasCustomName())
            {
                this.setCustomNameTag(StatCollector.translateToLocal("entity.KillerBunny.name"));
            }
        }

        this.dataWatcher.updateObject(18, Byte.valueOf((byte)rabbitTypeId));
    }

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        Object livingdata1 = super.onInitialSpawn(difficulty, livingdata);
        int var3 = this.rand.nextInt(6);
        boolean var4 = false;

        if (livingdata1 instanceof EntityRabbit.RabbitTypeData)
        {
            var3 = ((EntityRabbit.RabbitTypeData)livingdata1).typeData;
            var4 = true;
        }
        else
        {
            livingdata1 = new EntityRabbit.RabbitTypeData(var3);
        }

        this.setRabbitType(var3);

        if (var4)
        {
            this.setGrowingAge(-24000);
        }

        return (IEntityLivingData)livingdata1;
    }

    /**
     * Returns true if {@link net.minecraft.entity.passive.EntityRabbit#carrotTicks carrotTicks} has reached zero
     */
    private boolean isCarrotEaten()
    {
        return this.carrotTicks == 0;
    }

    /**
     * Returns duration of the current {@link net.minecraft.entity.passive.EntityRabbit.EnumMoveType move type}
     */
    protected int getMoveTypeDuration()
    {
        return this.moveType.getDuration();
    }

    protected void createEatingParticles()
    {
        this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, 0.0D, 0.0D, 0.0D, new int[] {Block.getStateId(Blocks.carrots.getStateFromMeta(7))});
        this.carrotTicks = 100;
    }

    @Override
	public void handleHealthUpdate(byte id)
    {
        if (id == 1)
        {
            this.createRunningParticles();
            this.field_175535_bn = 10;
            this.field_175540_bm = 0;
        }
        else
        {
            super.handleHealthUpdate(id);
        }
    }

    @Override
	public EntityAgeable createChild(EntityAgeable ageable)
    {
        return this.createChildRabbit(ageable);
    }

    class AIAvoidEntity extends EntityAIAvoidEntity
    {
        public AIAvoidEntity(Predicate avoidPredicate, float searchDistance, double farSpeed, double nearSpeed)
        {
            super(EntityRabbit.this, avoidPredicate, searchDistance, farSpeed, nearSpeed);
        }

        @Override
		public void updateTask()
        {
            super.updateTask();
        }
    }

    class AIEvilAttack extends EntityAIAttackOnCollide
    {

        public AIEvilAttack()
        {
            super(EntityRabbit.this, EntityLivingBase.class, 1.4D, true);
        }

        @Override
		protected double func_179512_a(EntityLivingBase attackTarget)
        {
            return 4.0F + attackTarget.width;
        }
    }

    class AIPanic extends EntityAIPanic
    {
        private EntityRabbit theEntity = EntityRabbit.this;

        public AIPanic(double speedIn)
        {
            super(EntityRabbit.this, speedIn);
        }

        @Override
		public void updateTask()
        {
            super.updateTask();
            this.theEntity.setMovementSpeed(this.speed);
        }
    }

    class AIRaidFarm extends EntityAIMoveToBlock
    {
        private boolean field_179498_d;
        private boolean field_179499_e = false;

        public AIRaidFarm()
        {
            super(EntityRabbit.this, 0.699999988079071D, 16);
        }

        @Override
		public boolean shouldExecute()
        {
            if (this.runDelay <= 0)
            {
                if (!EntityRabbit.this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
                {
                    return false;
                }

                this.field_179499_e = false;
                this.field_179498_d = EntityRabbit.this.isCarrotEaten();
            }

            return super.shouldExecute();
        }

        @Override
		public boolean continueExecuting()
        {
            return this.field_179499_e && super.continueExecuting();
        }

        @Override
		public void startExecuting()
        {
            super.startExecuting();
        }

        @Override
		public void resetTask()
        {
            super.resetTask();
        }

        @Override
		public void updateTask()
        {
            super.updateTask();
            EntityRabbit.this.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, 10.0F, EntityRabbit.this.getVerticalFaceSpeed());

            if (this.getIsAboveDestination())
            {
                World var1 = EntityRabbit.this.worldObj;
                BlockPos var2 = this.destinationBlock.up();
                IBlockState var3 = var1.getBlockState(var2);
                Block var4 = var3.getBlock();

                if (this.field_179499_e && var4 instanceof BlockCarrot && ((Integer)var3.getValue(BlockCrops.AGE)).intValue() == 7)
                {
                    var1.setBlockState(var2, Blocks.air.getDefaultState(), 2);
                    var1.destroyBlock(var2, true);
                    EntityRabbit.this.createEatingParticles();
                }

                this.field_179499_e = false;
                this.runDelay = 10;
            }
        }

        @Override
		protected boolean shouldMoveTo(World worldIn, BlockPos pos)
        {
            Block var3 = worldIn.getBlockState(pos).getBlock();

            if (var3 == Blocks.farmland)
            {
                pos = pos.up();
                IBlockState var4 = worldIn.getBlockState(pos);
                var3 = var4.getBlock();

                if (var3 instanceof BlockCarrot && ((Integer)var4.getValue(BlockCrops.AGE)).intValue() == 7 && this.field_179498_d && !this.field_179499_e)
                {
                    this.field_179499_e = true;
                    return true;
                }
            }

            return false;
        }
    }

    static enum EnumMoveType
    {
        NONE("NONE", 0, 0.0F, 0.0F, 30, 1),
        HOP("HOP", 1, 0.8F, 0.2F, 20, 10),
        STEP("STEP", 2, 1.0F, 0.45F, 14, 14),
        SPRINT("SPRINT", 3, 1.75F, 0.4F, 1, 8),
        ATTACK("ATTACK", 4, 2.0F, 0.7F, 7, 8);
        private final float speed;
        private final float field_180077_g;
        private final int duration;
        private final int field_180085_i; 

        private EnumMoveType(String name, int id, float typeSpeed, float p_i45866_4_, int typeDuration, int p_i45866_6_)
        {
            this.speed = typeSpeed;
            this.field_180077_g = p_i45866_4_;
            this.duration = typeDuration;
            this.field_180085_i = p_i45866_6_;
        }

        public float getSpeed()
        {
            return this.speed;
        }

        public float func_180074_b()
        {
            return this.field_180077_g;
        }

        public int getDuration()
        {
            return this.duration;
        }

        public int func_180073_d()
        {
            return this.field_180085_i;
        }
    }

    public class RabbitJumpHelper extends EntityJumpHelper
    {
        private EntityRabbit theEntity;
        private boolean field_180068_d = false;

        public RabbitJumpHelper(EntityRabbit rabbit)
        {
            super(rabbit);
            this.theEntity = rabbit;
        }

        public boolean getIsJumping()
        {
            return this.isJumping;
        }

        public boolean func_180065_d()
        {
            return this.field_180068_d;
        }

        public void func_180066_a(boolean p_180066_1_)
        {
            this.field_180068_d = p_180066_1_;
        }

        @Override
		public void doJump()
        {
            if (this.isJumping)
            {
                this.theEntity.doMovementAction(EntityRabbit.EnumMoveType.STEP);
                this.isJumping = false;
            }
        }
    }

    class RabbitMoveHelper extends EntityMoveHelper
    {
        private EntityRabbit theEntity = EntityRabbit.this;

        public RabbitMoveHelper()
        {
            super(EntityRabbit.this);
        }

        @Override
		public void onUpdateMoveHelper()
        {
            if (this.theEntity.onGround && !this.theEntity.func_175523_cj())
            {
                this.theEntity.setMovementSpeed(0.0D);
            }

            super.onUpdateMoveHelper();
        }
    }

    public static class RabbitTypeData implements IEntityLivingData
    {
        public int typeData;

        public RabbitTypeData(int type)
        {
            this.typeData = type;
        }
    }
}
