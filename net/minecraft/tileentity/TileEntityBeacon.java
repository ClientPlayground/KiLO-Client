package net.minecraft.tileentity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import com.google.common.collect.Lists;

public class TileEntityBeacon extends TileEntityLockable implements IUpdatePlayerListBox, IInventory
{
    /** List of effects that Beacon can apply */
    public static final Potion[][] effectsList = new Potion[][] {{Potion.moveSpeed, Potion.digSpeed}, {Potion.resistance, Potion.jump}, {Potion.damageBoost}, {Potion.regeneration}};

    /** A list of beam segments for this beacon */
    private final List beamSegments = Lists.newArrayList();
    private long field_146016_i;
    private float field_146014_j;
    private boolean isComplete;

    /** Level of this beacon's pyramid. */
    private int levels = -1;

    /** Primary potion effect given by this beacon. */
    private int primaryEffect;

    /** Secondary potion effect given by this beacon. */
    private int secondaryEffect;

    /** Item given to this beacon as payment. */
    private ItemStack payment;
    private String customName;

    /**
     * Updates the JList with a new model.
     */
    @Override
	public void update()
    {
        if (this.worldObj.getTotalWorldTime() % 80L == 0L)
        {
            this.func_174908_m();
        }
    }

    public void func_174908_m()
    {
        this.func_146003_y();
        this.func_146000_x();
    }

    private void func_146000_x()
    {
        if (this.isComplete && this.levels > 0 && !this.worldObj.isRemote && this.primaryEffect > 0)
        {
            double var1 = this.levels * 10 + 10;
            byte var3 = 0;

            if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect)
            {
                var3 = 1;
            }

            int var4 = this.pos.getX();
            int var5 = this.pos.getY();
            int var6 = this.pos.getZ();
            AxisAlignedBB var7 = (new AxisAlignedBB(var4, var5, var6, var4 + 1, var5 + 1, var6 + 1)).expand(var1, var1, var1).addCoord(0.0D, this.worldObj.getHeight(), 0.0D);
            List var8 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, var7);
            Iterator var9 = var8.iterator();
            EntityPlayer var10;

            while (var9.hasNext())
            {
                var10 = (EntityPlayer)var9.next();
                var10.addPotionEffect(new PotionEffect(this.primaryEffect, 180, var3, true, true));
            }

            if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect > 0)
            {
                var9 = var8.iterator();

                while (var9.hasNext())
                {
                    var10 = (EntityPlayer)var9.next();
                    var10.addPotionEffect(new PotionEffect(this.secondaryEffect, 180, 0, true, true));
                }
            }
        }
    }

    private void func_146003_y()
    {
        int var1 = this.levels;
        int var2 = this.pos.getX();
        int var3 = this.pos.getY();
        int var4 = this.pos.getZ();
        this.levels = 0;
        this.beamSegments.clear();
        this.isComplete = true;
        TileEntityBeacon.BeamSegment var5 = new TileEntityBeacon.BeamSegment(EntitySheep.func_175513_a(EnumDyeColor.WHITE));
        this.beamSegments.add(var5);
        boolean var6 = true;
        int var7;

        for (var7 = var3 + 1; var7 < this.worldObj.getActualHeight(); ++var7)
        {
            BlockPos var8 = new BlockPos(var2, var7, var4);
            IBlockState var9 = this.worldObj.getBlockState(var8);
            float[] var10;

            if (var9.getBlock() == Blocks.stained_glass)
            {
                var10 = EntitySheep.func_175513_a((EnumDyeColor)var9.getValue(BlockStainedGlass.COLOR));
            }
            else
            {
                if (var9.getBlock() != Blocks.stained_glass_pane)
                {
                    if (var9.getBlock().getLightOpacity() >= 15)
                    {
                        this.isComplete = false;
                        this.beamSegments.clear();
                        break;
                    }

                    var5.func_177262_a();
                    continue;
                }

                var10 = EntitySheep.func_175513_a((EnumDyeColor)var9.getValue(BlockStainedGlassPane.COLOR));
            }

            if (!var6)
            {
                var10 = new float[] {(var5.func_177263_b()[0] + var10[0]) / 2.0F, (var5.func_177263_b()[1] + var10[1]) / 2.0F, (var5.func_177263_b()[2] + var10[2]) / 2.0F};
            }

            if (Arrays.equals(var10, var5.func_177263_b()))
            {
                var5.func_177262_a();
            }
            else
            {
                var5 = new TileEntityBeacon.BeamSegment(var10);
                this.beamSegments.add(var5);
            }

            var6 = false;
        }

        if (this.isComplete)
        {
            for (var7 = 1; var7 <= 4; this.levels = var7++)
            {
                int var14 = var3 - var7;

                if (var14 < 0)
                {
                    break;
                }

                boolean var16 = true;

                for (int var17 = var2 - var7; var17 <= var2 + var7 && var16; ++var17)
                {
                    for (int var11 = var4 - var7; var11 <= var4 + var7; ++var11)
                    {
                        Block var12 = this.worldObj.getBlockState(new BlockPos(var17, var14, var11)).getBlock();

                        if (var12 != Blocks.emerald_block && var12 != Blocks.gold_block && var12 != Blocks.diamond_block && var12 != Blocks.iron_block)
                        {
                            var16 = false;
                            break;
                        }
                    }
                }

                if (!var16)
                {
                    break;
                }
            }

            if (this.levels == 0)
            {
                this.isComplete = false;
            }
        }

        if (!this.worldObj.isRemote && this.levels == 4 && var1 < this.levels)
        {
            Iterator var13 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB(var2, var3, var4, var2, var3 - 4, var4)).expand(10.0D, 5.0D, 10.0D)).iterator();

            while (var13.hasNext())
            {
                EntityPlayer var15 = (EntityPlayer)var13.next();
                var15.triggerAchievement(AchievementList.fullBeacon);
            }
        }
    }

    public List func_174907_n()
    {
        return this.beamSegments;
    }

    public float shouldBeamRender()
    {
        if (!this.isComplete)
        {
            return 0.0F;
        }
        else
        {
            int var1 = (int)(this.worldObj.getTotalWorldTime() - this.field_146016_i);
            this.field_146016_i = this.worldObj.getTotalWorldTime();

            if (var1 > 1)
            {
                this.field_146014_j -= var1 / 40.0F;

                if (this.field_146014_j < 0.0F)
                {
                    this.field_146014_j = 0.0F;
                }
            }

            this.field_146014_j += 0.025F;

            if (this.field_146014_j > 1.0F)
            {
                this.field_146014_j = 1.0F;
            }

            return this.field_146014_j;
        }
    }

    /**
     * Allows for a specialized description packet to be created. This is often used to sync tile entity data from the
     * server to the client easily. For example this is used by signs to synchronise the text to be displayed.
     */
    @Override
	public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.pos, 3, var1);
    }

    @Override
	public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
    }

    @Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.primaryEffect = compound.getInteger("Primary");
        this.secondaryEffect = compound.getInteger("Secondary");
        this.levels = compound.getInteger("Levels");
    }

    @Override
	public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("Primary", this.primaryEffect);
        compound.setInteger("Secondary", this.secondaryEffect);
        compound.setInteger("Levels", this.levels);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        return 1;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
	public ItemStack getStackInSlot(int index)
    {
        return index == 0 ? this.payment : null;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
	public ItemStack decrStackSize(int index, int count)
    {
        if (index == 0 && this.payment != null)
        {
            if (count >= this.payment.stackSize)
            {
                ItemStack var3 = this.payment;
                this.payment = null;
                return var3;
            }
            else
            {
                this.payment.stackSize -= count;
                return new ItemStack(this.payment.getItem(), count, this.payment.getMetadata());
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
	public ItemStack getStackInSlotOnClosing(int index)
    {
        if (index == 0 && this.payment != null)
        {
            ItemStack var2 = this.payment;
            this.payment = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (index == 0)
        {
            this.payment = stack;
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
	public String getCommandSenderName()
    {
        return this.hasCustomName() ? this.customName : "container.beacon";
    }

    /**
     * Returns true if this thing is named
     */
    @Override
	public boolean hasCustomName()
    {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setName(String p_145999_1_)
    {
        this.customName = p_145999_1_;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    @Override
	public int getInventoryStackLimit()
    {
        return 1;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    @Override
	public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
	public void openInventory(EntityPlayer player) {}

    @Override
	public void closeInventory(EntityPlayer player) {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    @Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return stack.getItem() == Items.emerald || stack.getItem() == Items.diamond || stack.getItem() == Items.gold_ingot || stack.getItem() == Items.iron_ingot;
    }

    @Override
	public String getGuiID()
    {
        return "minecraft:beacon";
    }

    @Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerBeacon(playerInventory, this);
    }

    @Override
	public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.levels;

            case 1:
                return this.primaryEffect;

            case 2:
                return this.secondaryEffect;

            default:
                return 0;
        }
    }

    @Override
	public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.levels = value;
                break;

            case 1:
                this.primaryEffect = value;
                break;

            case 2:
                this.secondaryEffect = value;
        }
    }

    @Override
	public int getFieldCount()
    {
        return 3;
    }

    @Override
	public void clear()
    {
        this.payment = null;
    }

    @Override
	public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.func_174908_m();
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    public static class BeamSegment
    {
        private final float[] field_177266_a;
        private int field_177265_b;

        public BeamSegment(float[] p_i45669_1_)
        {
            this.field_177266_a = p_i45669_1_;
            this.field_177265_b = 1;
        }

        protected void func_177262_a()
        {
            ++this.field_177265_b;
        }

        public float[] func_177263_b()
        {
            return this.field_177266_a;
        }

        public int func_177264_c()
        {
            return this.field_177265_b;
        }
    }
}
