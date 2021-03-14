package net.minecraft.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.ISaveHandler;

public class WorldServerMulti extends WorldServer
{
    private WorldServer delegate;

    public WorldServerMulti(MinecraftServer server, ISaveHandler saveHandlerIn, int dimensionId, WorldServer delegate, Profiler profilerIn)
    {
        super(server, saveHandlerIn, new DerivedWorldInfo(delegate.getWorldInfo()), dimensionId, profilerIn);
        this.delegate = delegate;
        delegate.getWorldBorder().addListener(new IBorderListener()
        {
            @Override
			public void onSizeChanged(WorldBorder border, double newSize)
            {
                WorldServerMulti.this.getWorldBorder().setTransition(newSize);
            }
            @Override
			public void onTransitionStarted(WorldBorder border, double oldSize, double newSize, long time)
            {
                WorldServerMulti.this.getWorldBorder().setTransition(oldSize, newSize, time);
            }
            @Override
			public void onCenterChanged(WorldBorder border, double x, double z)
            {
                WorldServerMulti.this.getWorldBorder().setCenter(x, z);
            }
            @Override
			public void onWarningTimeChanged(WorldBorder border, int newTime)
            {
                WorldServerMulti.this.getWorldBorder().setWarningTime(newTime);
            }
            @Override
			public void onWarningDistanceChanged(WorldBorder border, int newDistance)
            {
                WorldServerMulti.this.getWorldBorder().setWarningDistance(newDistance);
            }
            @Override
			public void onDamageAmountChanged(WorldBorder border, double newAmount)
            {
                WorldServerMulti.this.getWorldBorder().setDamageAmount(newAmount);
            }
            @Override
			public void onDamageBufferChanged(WorldBorder border, double newSize)
            {
                WorldServerMulti.this.getWorldBorder().setDamageBuffer(newSize);
            }
        });
    }

    /**
     * Saves the chunks to disk.
     */
    @Override
	protected void saveLevel() throws MinecraftException {}

    @Override
	public World init()
    {
        this.mapStorage = this.delegate.getMapStorage();
        this.worldScoreboard = this.delegate.getScoreboard();
        String var1 = VillageCollection.fileNameForProvider(this.provider);
        VillageCollection var2 = (VillageCollection)this.mapStorage.loadData(VillageCollection.class, var1);

        if (var2 == null)
        {
            this.villageCollectionObj = new VillageCollection(this);
            this.mapStorage.setData(var1, this.villageCollectionObj);
        }
        else
        {
            this.villageCollectionObj = var2;
            this.villageCollectionObj.setWorldsForAll(this);
        }

        return this;
    }
}
