package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.village.Village;

public class EntityAIDefendVillage extends EntityAITarget
{
    EntityIronGolem irongolem;

    /**
     * The aggressor of the iron golem's village which is now the golem's attack target.
     */
    EntityLivingBase villageAgressorTarget;

    public EntityAIDefendVillage(EntityIronGolem ironGolemIn)
    {
        super(ironGolemIn, false, true);
        this.irongolem = ironGolemIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute()
    {
        Village var1 = this.irongolem.getVillage();

        if (var1 == null)
        {
            return false;
        }
        else
        {
            this.villageAgressorTarget = var1.findNearestVillageAggressor(this.irongolem);

            if (!this.isSuitableTarget(this.villageAgressorTarget, false))
            {
                if (this.taskOwner.getRNG().nextInt(20) == 0)
                {
                    this.villageAgressorTarget = var1.getNearestTargetPlayer(this.irongolem);
                    return this.isSuitableTarget(this.villageAgressorTarget, false);
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return true;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void startExecuting()
    {
        this.irongolem.setAttackTarget(this.villageAgressorTarget);
        super.startExecuting();
    }
}
