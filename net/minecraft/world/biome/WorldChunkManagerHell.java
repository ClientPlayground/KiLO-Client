package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.util.BlockPos;

public class WorldChunkManagerHell extends WorldChunkManager
{
    /** The biome generator object. */
    private BiomeGenBase biomeGenerator;

    /** The rainfall in the world */
    private float rainfall;

    public WorldChunkManagerHell(BiomeGenBase p_i45374_1_, float p_i45374_2_)
    {
        this.biomeGenerator = p_i45374_1_;
        this.rainfall = p_i45374_2_;
    }

    /**
     * Returns the biome generator
     */
    @Override
	public BiomeGenBase getBiomeGenerator(BlockPos p_180631_1_)
    {
        return this.biomeGenerator;
    }

    /**
     * Returns an array of biomes for the location input.
     */
    @Override
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] p_76937_1_, int p_76937_2_, int p_76937_3_, int p_76937_4_, int p_76937_5_)
    {
        if (p_76937_1_ == null || p_76937_1_.length < p_76937_4_ * p_76937_5_)
        {
            p_76937_1_ = new BiomeGenBase[p_76937_4_ * p_76937_5_];
        }

        Arrays.fill(p_76937_1_, 0, p_76937_4_ * p_76937_5_, this.biomeGenerator);
        return p_76937_1_;
    }

    /**
     * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
     */
    @Override
	public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length)
    {
        if (listToReuse == null || listToReuse.length < width * length)
        {
            listToReuse = new float[width * length];
        }

        Arrays.fill(listToReuse, 0, width * length, this.rainfall);
        return listToReuse;
    }

    /**
     * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager Args: oldBiomeList, x, z, width, depth
     */
    @Override
	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] oldBiomeList, int x, int z, int width, int depth)
    {
        if (oldBiomeList == null || oldBiomeList.length < width * depth)
        {
            oldBiomeList = new BiomeGenBase[width * depth];
        }

        Arrays.fill(oldBiomeList, 0, width * depth, this.biomeGenerator);
        return oldBiomeList;
    }

    /**
     * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
     * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
     *  
     * @param cacheFlag If false, don't check biomeCache to avoid infinite loop in BiomeCacheBlock
     */
    @Override
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
    {
        return this.loadBlockGeneratorData(listToReuse, x, z, width, length);
    }

    @Override
	public BlockPos findBiomePosition(int x, int z, int range, List biomes, Random random)
    {
        return biomes.contains(this.biomeGenerator) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    @Override
	public boolean areBiomesViable(int p_76940_1_, int p_76940_2_, int p_76940_3_, List p_76940_4_)
    {
        return p_76940_4_.contains(this.biomeGenerator);
    }
}
