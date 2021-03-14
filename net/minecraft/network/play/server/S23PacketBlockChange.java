package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class S23PacketBlockChange implements Packet
{
    private BlockPos blockPosition;
    private IBlockState blockState;

    public S23PacketBlockChange() {}

    public S23PacketBlockChange(World worldIn, BlockPos blockPositionIn)
    {
        this.blockPosition = blockPositionIn;
        this.blockState = worldIn.getBlockState(blockPositionIn);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.blockPosition = buf.readBlockPos();
        this.blockState = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(buf.readVarIntFromBuffer());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.blockPosition);
        buf.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(this.blockState));
    }

    public void func_180727_a(INetHandlerPlayClient handler)
    {
        handler.handleBlockChange(this);
    }

    public IBlockState getBlockState()
    {
        return this.blockState;
    }

    public BlockPos getBlockPosition()
    {
        return this.blockPosition;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_180727_a((INetHandlerPlayClient)handler);
    }
}
