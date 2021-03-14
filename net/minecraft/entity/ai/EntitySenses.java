package net.minecraft.entity.ai;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

import com.google.common.collect.Lists;

public class EntitySenses
{
    EntityLiving entityObj;

    /** Cache of entities which we can see */
    List seenEntities = Lists.newArrayList();

    /** Cache of entities which we cannot see */
    List unseenEntities = Lists.newArrayList();

    public EntitySenses(EntityLiving entityObjIn)
    {
        this.entityObj = entityObjIn;
    }

    /**
     * Clears canSeeCachePositive and canSeeCacheNegative.
     */
    public void clearSensingCache()
    {
        this.seenEntities.clear();
        this.unseenEntities.clear();
    }

    /**
     * Checks, whether 'our' entity can see the entity given as argument (true) or not (false), caching the result.
     */
    public boolean canSee(Entity p_75522_1_)
    {
        if (this.seenEntities.contains(p_75522_1_))
        {
            return true;
        }
        else if (this.unseenEntities.contains(p_75522_1_))
        {
            return false;
        }
        else
        {
            this.entityObj.worldObj.theProfiler.startSection("canSee");
            boolean var2 = this.entityObj.canEntityBeSeen(p_75522_1_);
            this.entityObj.worldObj.theProfiler.endSection();

            if (var2)
            {
                this.seenEntities.add(p_75522_1_);
            }
            else
            {
                this.unseenEntities.add(p_75522_1_);
            }

            return var2;
        }
    }
}
