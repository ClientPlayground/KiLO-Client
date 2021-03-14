package net.minecraft.world.chunk;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import com.google.common.base.Predicate;

public class EmptyChunk extends Chunk
{

    public EmptyChunk(World worldIn, int x, int z)
    {
        super(worldIn, x, z);
    }

    /**
     * Checks whether the chunk is at the X/Z location specified
     */
    @Override
	public boolean isAtLocation(int x, int z)
    {
        return x == this.xPosition && z == this.zPosition;
    }

    /**
     * Returns the value in the height map at this x, z coordinate in the chunk
     */
    @Override
	public int getHeightValue(int x, int z)
    {
        return 0;
    }

    /**
     * Generates the height map for a chunk from scratch
     */
    @Override
	public void generateHeightMap() {}

    /**
     * Generates the initial skylight map for the chunk upon generation or load.
     */
    @Override
	public void generateSkylightMap() {}

    @Override
	public Block getBlock(BlockPos pos)
    {
        return Blocks.air;
    }

    @Override
	public int getBlockLightOpacity(BlockPos pos)
    {
        return 255;
    }

    @Override
	public int getBlockMetadata(BlockPos pos)
    {
        return 0;
    }

    @Override
	public int getLightFor(EnumSkyBlock p_177413_1_, BlockPos pos)
    {
        return p_177413_1_.defaultLightValue;
    }

    @Override
	public void setLightFor(EnumSkyBlock p_177431_1_, BlockPos pos, int value) {}

    @Override
	public int getLightSubtracted(BlockPos pos, int amount)
    {
        return 0;
    }

    /**
     * Adds an entity to the chunk. Args: entity
     */
    @Override
	public void addEntity(Entity entityIn) {}

    /**
     * removes entity using its y chunk coordinate as its index
     */
    @Override
	public void removeEntity(Entity entityIn) {}

    /**
     * Removes entity at the specified index from the entity array.
     */
    @Override
	public void removeEntityAtIndex(Entity entityIn, int p_76608_2_) {}

    @Override
	public boolean canSeeSky(BlockPos pos)
    {
        return false;
    }

    @Override
	public TileEntity getTileEntity(BlockPos pos, Chunk.EnumCreateEntityType p_177424_2_)
    {
        return null;
    }

    @Override
	public void addTileEntity(TileEntity tileEntityIn) {}

    @Override
	public void addTileEntity(BlockPos pos, TileEntity tileEntityIn) {}

    @Override
	public void removeTileEntity(BlockPos pos) {}

    /**
     * Called when this Chunk is loaded by the ChunkProvider
     */
    @Override
	public void onChunkLoad() {}

    /**
     * Called when this Chunk is unloaded by the ChunkProvider
     */
    @Override
	public void onChunkUnload() {}

    /**
     * Sets the isModified flag for this Chunk
     */
    @Override
	public void setChunkModified() {}

    /**
     * Fills the given list of all entities that intersect within the given bounding box that aren't the passed entity.
     */
    @Override
	public void getEntitiesWithinAABBForEntity(Entity entityIn, AxisAlignedBB aabb, List listToFill, Predicate p_177414_4_) {}

    /**
     * Gets all entities that can be assigned to the specified class.
     */
    @Override
	public void getEntitiesOfTypeWithinAAAB(Class entityClass, AxisAlignedBB aabb, List listToFill, Predicate p_177430_4_) {}

    /**
     * Returns true if this Chunk needs to be saved
     */
    @Override
	public boolean needsSaving(boolean p_76601_1_)
    {
        return false;
    }

    @Override
	public Random getRandomWithSeed(long seed)
    {
        return new Random(this.getWorld().getSeed() + this.xPosition * this.xPosition * 4987142 + this.xPosition * 5947611 + this.zPosition * this.zPosition * 4392871L + this.zPosition * 389711 ^ seed);
    }

    @Override
	public boolean isEmpty()
    {
        return true;
    }

    /**
     * Returns whether the ExtendedBlockStorages containing levels (in blocks) from arg 1 to arg 2 are fully empty
     * (true) or not (false).
     */
    @Override
	public boolean getAreLevelsEmpty(int startY, int endY)
    {
        return true;
    }
}
