package net.minecraft.client.audio;

import java.util.List;

import com.google.common.collect.Lists;

public class SoundList
{
    private final List soundList = Lists.newArrayList();

    /**
     * if true it will override all the sounds from the resourcepacks loaded before
     */
    private boolean replaceExisting;
    private SoundCategory category;

    public List getSoundList()
    {
        return this.soundList;
    }

    public boolean canReplaceExisting()
    {
        return this.replaceExisting;
    }

    public void setReplaceExisting(boolean p_148572_1_)
    {
        this.replaceExisting = p_148572_1_;
    }

    public SoundCategory getSoundCategory()
    {
        return this.category;
    }

    public void setSoundCategory(SoundCategory soundCat)
    {
        this.category = soundCat;
    }

    public static class SoundEntry
    {
        private String name;
        private float volume = 1.0F;
        private float pitch = 1.0F;
        private int weight = 1;
        private SoundList.SoundEntry.Type type;
        private boolean streaming;

        public SoundEntry()
        {
            this.type = SoundList.SoundEntry.Type.FILE;
            this.streaming = false;
        }

        public String getSoundEntryName()
        {
            return this.name;
        }

        public void setSoundEntryName(String nameIn)
        {
            this.name = nameIn;
        }

        public float getSoundEntryVolume()
        {
            return this.volume;
        }

        public void setSoundEntryVolume(float volumeIn)
        {
            this.volume = volumeIn;
        }

        public float getSoundEntryPitch()
        {
            return this.pitch;
        }

        public void setSoundEntryPitch(float pitchIn)
        {
            this.pitch = pitchIn;
        }

        public int getSoundEntryWeight()
        {
            return this.weight;
        }

        public void setSoundEntryWeight(int weightIn)
        {
            this.weight = weightIn;
        }

        public SoundList.SoundEntry.Type getSoundEntryType()
        {
            return this.type;
        }

        public void setSoundEntryType(SoundList.SoundEntry.Type typeIn)
        {
            this.type = typeIn;
        }

        public boolean isStreaming()
        {
            return this.streaming;
        }

        public void setStreaming(boolean isStreaming)
        {
            this.streaming = isStreaming;
        }

        public static enum Type
        {
            FILE("FILE", 0, "file"),
            SOUND_EVENT("SOUND_EVENT", 1, "event");
            private final String field_148583_c; 

            private Type(String p_i45109_1_, int p_i45109_2_, String p_i45109_3_)
            {
                this.field_148583_c = p_i45109_3_;
            }

            public static SoundList.SoundEntry.Type getType(String p_148580_0_)
            {
                SoundList.SoundEntry.Type[] var1 = values();
                int var2 = var1.length;

                for (int var3 = 0; var3 < var2; ++var3)
                {
                    SoundList.SoundEntry.Type var4 = var1[var3];

                    if (var4.field_148583_c.equals(p_148580_0_))
                    {
                        return var4;
                    }
                }

                return null;
            }
        }
    }
}
