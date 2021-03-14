package net.minecraft.util;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;

public class CombatTracker
{
    /** The CombatEntry objects that we've tracked so far. */
    private final List combatEntries = Lists.newArrayList();

    /** The entity tracked. */
    private final EntityLivingBase fighter;
    private int field_94555_c;
    private int field_152775_d;
    private int field_152776_e;
    private boolean field_94552_d;
    private boolean field_94553_e;
    private String field_94551_f;

    public CombatTracker(EntityLivingBase fighterIn)
    {
        this.fighter = fighterIn;
    }

    public void func_94545_a()
    {
        this.func_94542_g();

        if (this.fighter.isOnLadder())
        {
            Block var1 = this.fighter.worldObj.getBlockState(new BlockPos(this.fighter.posX, this.fighter.getEntityBoundingBox().minY, this.fighter.posZ)).getBlock();

            if (var1 == Blocks.ladder)
            {
                this.field_94551_f = "ladder";
            }
            else if (var1 == Blocks.vine)
            {
                this.field_94551_f = "vines";
            }
        }
        else if (this.fighter.isInWater())
        {
            this.field_94551_f = "water";
        }
    }

    public void func_94547_a(DamageSource damageSrc, float healthIn, float damageAmount)
    {
        this.func_94549_h();
        this.func_94545_a();
        CombatEntry var4 = new CombatEntry(damageSrc, this.fighter.ticksExisted, healthIn, damageAmount, this.field_94551_f, this.fighter.fallDistance);
        this.combatEntries.add(var4);
        this.field_94555_c = this.fighter.ticksExisted;
        this.field_94553_e = true;

        if (var4.isLivingDamageSrc() && !this.field_94552_d && this.fighter.isEntityAlive())
        {
            this.field_94552_d = true;
            this.field_152775_d = this.fighter.ticksExisted;
            this.field_152776_e = this.field_152775_d;
            this.fighter.sendEnterCombat();
        }
    }

    public IChatComponent getDeathMessage()
    {
        if (this.combatEntries.size() == 0)
        {
            return new ChatComponentTranslation("death.attack.generic", new Object[] {this.fighter.getDisplayName()});
        }
        else
        {
            CombatEntry var1 = this.func_94544_f();
            CombatEntry var2 = (CombatEntry)this.combatEntries.get(this.combatEntries.size() - 1);
            IChatComponent var4 = var2.getDamageSrcDisplayName();
            Entity var5 = var2.getDamageSrc().getEntity();
            Object var3;

            if (var1 != null && var2.getDamageSrc() == DamageSource.fall)
            {
                IChatComponent var6 = var1.getDamageSrcDisplayName();

                if (var1.getDamageSrc() != DamageSource.fall && var1.getDamageSrc() != DamageSource.outOfWorld)
                {
                    if (var6 != null && (var4 == null || !var6.equals(var4)))
                    {
                        Entity var9 = var1.getDamageSrc().getEntity();
                        ItemStack var8 = var9 instanceof EntityLivingBase ? ((EntityLivingBase)var9).getHeldItem() : null;

                        if (var8 != null && var8.hasDisplayName())
                        {
                            var3 = new ChatComponentTranslation("death.fell.assist.item", new Object[] {this.fighter.getDisplayName(), var6, var8.getChatComponent()});
                        }
                        else
                        {
                            var3 = new ChatComponentTranslation("death.fell.assist", new Object[] {this.fighter.getDisplayName(), var6});
                        }
                    }
                    else if (var4 != null)
                    {
                        ItemStack var7 = var5 instanceof EntityLivingBase ? ((EntityLivingBase)var5).getHeldItem() : null;

                        if (var7 != null && var7.hasDisplayName())
                        {
                            var3 = new ChatComponentTranslation("death.fell.finish.item", new Object[] {this.fighter.getDisplayName(), var4, var7.getChatComponent()});
                        }
                        else
                        {
                            var3 = new ChatComponentTranslation("death.fell.finish", new Object[] {this.fighter.getDisplayName(), var4});
                        }
                    }
                    else
                    {
                        var3 = new ChatComponentTranslation("death.fell.killer", new Object[] {this.fighter.getDisplayName()});
                    }
                }
                else
                {
                    var3 = new ChatComponentTranslation("death.fell.accident." + this.func_94548_b(var1), new Object[] {this.fighter.getDisplayName()});
                }
            }
            else
            {
                var3 = var2.getDamageSrc().getDeathMessage(this.fighter);
            }

            return (IChatComponent)var3;
        }
    }

    public EntityLivingBase func_94550_c()
    {
        EntityLivingBase var1 = null;
        EntityPlayer var2 = null;
        float var3 = 0.0F;
        float var4 = 0.0F;
        Iterator var5 = this.combatEntries.iterator();

        while (var5.hasNext())
        {
            CombatEntry var6 = (CombatEntry)var5.next();

            if (var6.getDamageSrc().getEntity() instanceof EntityPlayer && (var2 == null || var6.func_94563_c() > var4))
            {
                var4 = var6.func_94563_c();
                var2 = (EntityPlayer)var6.getDamageSrc().getEntity();
            }

            if (var6.getDamageSrc().getEntity() instanceof EntityLivingBase && (var1 == null || var6.func_94563_c() > var3))
            {
                var3 = var6.func_94563_c();
                var1 = (EntityLivingBase)var6.getDamageSrc().getEntity();
            }
        }

        if (var2 != null && var4 >= var3 / 3.0F)
        {
            return var2;
        }
        else
        {
            return var1;
        }
    }

    private CombatEntry func_94544_f()
    {
        CombatEntry var1 = null;
        CombatEntry var2 = null;
        byte var3 = 0;
        float var4 = 0.0F;

        for (int var5 = 0; var5 < this.combatEntries.size(); ++var5)
        {
            CombatEntry var6 = (CombatEntry)this.combatEntries.get(var5);
            CombatEntry var7 = var5 > 0 ? (CombatEntry)this.combatEntries.get(var5 - 1) : null;

            if ((var6.getDamageSrc() == DamageSource.fall || var6.getDamageSrc() == DamageSource.outOfWorld) && var6.getDamageAmount() > 0.0F && (var1 == null || var6.getDamageAmount() > var4))
            {
                if (var5 > 0)
                {
                    var1 = var7;
                }
                else
                {
                    var1 = var6;
                }

                var4 = var6.getDamageAmount();
            }

            if (var6.func_94562_g() != null && (var2 == null || var6.func_94563_c() > var3))
            {
                var2 = var6;
            }
        }

        if (var4 > 5.0F && var1 != null)
        {
            return var1;
        }
        else if (var3 > 5 && var2 != null)
        {
            return var2;
        }
        else
        {
            return null;
        }
    }

    private String func_94548_b(CombatEntry p_94548_1_)
    {
        return p_94548_1_.func_94562_g() == null ? "generic" : p_94548_1_.func_94562_g();
    }

    public int func_180134_f()
    {
        return this.field_94552_d ? this.fighter.ticksExisted - this.field_152775_d : this.field_152776_e - this.field_152775_d;
    }

    private void func_94542_g()
    {
        this.field_94551_f = null;
    }

    public void func_94549_h()
    {
        int var1 = this.field_94552_d ? 300 : 100;

        if (this.field_94553_e && (!this.fighter.isEntityAlive() || this.fighter.ticksExisted - this.field_94555_c > var1))
        {
            boolean var2 = this.field_94552_d;
            this.field_94553_e = false;
            this.field_94552_d = false;
            this.field_152776_e = this.fighter.ticksExisted;

            if (var2)
            {
                this.fighter.sendEndCombat();
            }

            this.combatEntries.clear();
        }
    }

    /**
     * Returns EntityLivingBase assigned for this CombatTracker
     */
    public EntityLivingBase getFighter()
    {
        return this.fighter;
    }
}
