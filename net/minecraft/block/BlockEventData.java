package net.minecraft.block;

import net.minecraft.util.BlockPos;

public class BlockEventData
{
    private BlockPos position;
    private Block blockType;

    /** Different for each blockID */
    private int eventID;
    private int eventParameter;

    public BlockEventData(BlockPos pos, Block blockType, int eventId, int p_i45756_4_)
    {
        this.position = pos;
        this.eventID = eventId;
        this.eventParameter = p_i45756_4_;
        this.blockType = blockType;
    }

    public BlockPos getPosition()
    {
        return this.position;
    }

    /**
     * Get the Event ID (different for each BlockID)
     */
    public int getEventID()
    {
        return this.eventID;
    }

    public int getEventParameter()
    {
        return this.eventParameter;
    }

    public Block getBlock()
    {
        return this.blockType;
    }

    @Override
	public boolean equals(Object p_equals_1_)
    {
        if (!(p_equals_1_ instanceof BlockEventData))
        {
            return false;
        }
        else
        {
            BlockEventData var2 = (BlockEventData)p_equals_1_;
            return this.position.equals(var2.position) && this.eventID == var2.eventID && this.eventParameter == var2.eventParameter && this.blockType == var2.blockType;
        }
    }

    @Override
	public String toString()
    {
        return "TE(" + this.position + ")," + this.eventID + "," + this.eventParameter + "," + this.blockType;
    }
}
