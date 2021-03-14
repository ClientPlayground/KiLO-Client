package net.minecraft.event;

import java.util.Map;

import net.minecraft.util.IChatComponent;

import com.google.common.collect.Maps;

public class HoverEvent
{
    private final HoverEvent.Action action;
    private final IChatComponent value;

    public HoverEvent(HoverEvent.Action actionIn, IChatComponent valueIn)
    {
        this.action = actionIn;
        this.value = valueIn;
    }

    /**
     * Gets the action to perform when this event is raised.
     */
    public HoverEvent.Action getAction()
    {
        return this.action;
    }

    /**
     * Gets the value to perform the action on when this event is raised.  For example, if the action is "show item",
     * this would be the item to show.
     */
    public IChatComponent getValue()
    {
        return this.value;
    }

    @Override
	public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass())
        {
            HoverEvent var2 = (HoverEvent)p_equals_1_;

            if (this.action != var2.action)
            {
                return false;
            }
            else
            {
                if (this.value != null)
                {
                    if (!this.value.equals(var2.value))
                    {
                        return false;
                    }
                }
                else if (var2.value != null)
                {
                    return false;
                }

                return true;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
	public String toString()
    {
        return "HoverEvent{action=" + this.action + ", value=\'" + this.value + '\'' + '}';
    }

    @Override
	public int hashCode()
    {
        int var1 = this.action.hashCode();
        var1 = 31 * var1 + (this.value != null ? this.value.hashCode() : 0);
        return var1;
    }

    public static enum Action
    {
        SHOW_TEXT("SHOW_TEXT", 0, "show_text", true),
        SHOW_ACHIEVEMENT("SHOW_ACHIEVEMENT", 1, "show_achievement", true),
        SHOW_ITEM("SHOW_ITEM", 2, "show_item", true),
        SHOW_ENTITY("SHOW_ENTITY", 3, "show_entity", true);
        private static final Map nameMapping = Maps.newHashMap();
        private final boolean allowedInChat;
        private final String canonicalName; 

        private Action(String p_i45157_1_, int p_i45157_2_, String canonicalNameIn, boolean allowedInChatIn)
        {
            this.canonicalName = canonicalNameIn;
            this.allowedInChat = allowedInChatIn;
        }

        public boolean shouldAllowInChat()
        {
            return this.allowedInChat;
        }

        public String getCanonicalName()
        {
            return this.canonicalName;
        }

        public static HoverEvent.Action getValueByCanonicalName(String canonicalNameIn)
        {
            return (HoverEvent.Action)nameMapping.get(canonicalNameIn);
        }

        static {
            HoverEvent.Action[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                HoverEvent.Action var3 = var0[var2];
                nameMapping.put(var3.getCanonicalName(), var3);
            }
        }
    }
}
