package net.minecraft.network.handshake.client;

import java.io.IOException;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;

public class C00Handshake implements Packet
{
    private int protocolVersion;
    private String ip;
    private int port;
    private EnumConnectionState requestedState;

    public C00Handshake() {}

    public C00Handshake(int version, String ip, int port, EnumConnectionState requestedState)
    {
        this.protocolVersion = version;
        this.ip = ip;
        this.port = port;
        this.requestedState = requestedState;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.protocolVersion = buf.readVarIntFromBuffer();
        this.ip = buf.readStringFromBuffer(255);
        this.port = buf.readUnsignedShort();
        this.requestedState = EnumConnectionState.getById(buf.readVarIntFromBuffer());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.protocolVersion);
        buf.writeString(this.ip);
        buf.writeShort(this.port);
        buf.writeVarIntToBuffer(this.requestedState.getId());
    }

    public void handle(INetHandlerHandshakeServer handler)
    {
        handler.processHandshake(this);
    }

    public EnumConnectionState getRequestedState()
    {
        return this.requestedState;
    }

    public int getProtocolVersion()
    {
        return this.protocolVersion;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.handle((INetHandlerHandshakeServer)handler);
    }
}
