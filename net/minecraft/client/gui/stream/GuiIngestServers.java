package net.minecraft.client.gui.stream;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IngestServerTester;
import net.minecraft.util.EnumChatFormatting;
import tv.twitch.broadcast.IngestServer;

public class GuiIngestServers extends GuiScreen
{
    private final GuiScreen field_152309_a;
    private String field_152310_f;
    private GuiIngestServers.ServerList field_152311_g;

    public GuiIngestServers(GuiScreen p_i46312_1_)
    {
        this.field_152309_a = p_i46312_1_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
	public void initGui()
    {
        this.field_152310_f = I18n.format("options.stream.ingest.title", new Object[0]);
        this.field_152311_g = new GuiIngestServers.ServerList(this.mc);

        if (!this.mc.getTwitchStream().func_152908_z())
        {
            this.mc.getTwitchStream().func_152909_x();
        }

        this.buttonList.add(new GuiButton(1, this.width / 2 - 155, this.height - 24 - 6, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 5, this.height - 24 - 6, 150, 20, I18n.format("options.stream.ingest.reset", new Object[0])));
    }

    /**
     * Handles mouse input.
     */
    @Override
	public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.field_152311_g.handleMouseInput();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
	public void onGuiClosed()
    {
        if (this.mc.getTwitchStream().func_152908_z())
        {
            this.mc.getTwitchStream().func_152932_y().func_153039_l();
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
	protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                this.mc.displayGuiScreen(this.field_152309_a);
            }
            else
            {
                this.mc.gameSettings.streamPreferredServer = "";
                this.mc.gameSettings.saveOptions();
            }
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.field_152311_g.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.field_152310_f, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    class ServerList extends GuiSlot
    {

        public ServerList(Minecraft mcIn)
        {
            super(mcIn, GuiIngestServers.this.width, GuiIngestServers.this.height, 32, GuiIngestServers.this.height - 35, (int)(mcIn.fontRendererObj.FONT_HEIGHT * 3.5D));
            this.setShowSelectionBox(false);
        }

        @Override
		protected int getSize()
        {
            return this.mc.getTwitchStream().func_152925_v().length;
        }

        @Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
        {
            this.mc.gameSettings.streamPreferredServer = this.mc.getTwitchStream().func_152925_v()[slotIndex].serverUrl;
            this.mc.gameSettings.saveOptions();
        }

        @Override
		protected boolean isSelected(int slotIndex)
        {
            return this.mc.getTwitchStream().func_152925_v()[slotIndex].serverUrl.equals(this.mc.gameSettings.streamPreferredServer);
        }

        @Override
		protected void drawBackground() {}

        @Override
		protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
        {
            IngestServer var7 = this.mc.getTwitchStream().func_152925_v()[entryID];
            String var8 = var7.serverUrl.replaceAll("\\{stream_key\\}", "");
            String var9 = (int)var7.bitrateKbps + " kbps";
            String var10 = null;
            IngestServerTester var11 = this.mc.getTwitchStream().func_152932_y();

            if (var11 != null)
            {
                if (var7 == var11.func_153040_c())
                {
                    var8 = EnumChatFormatting.GREEN + var8;
                    var9 = (int)(var11.func_153030_h() * 100.0F) + "%";
                }
                else if (entryID < var11.func_153028_p())
                {
                    if (var7.bitrateKbps == 0.0F)
                    {
                        var9 = EnumChatFormatting.RED + "Down!";
                    }
                }
                else
                {
                    var9 = EnumChatFormatting.OBFUSCATED + "1234" + EnumChatFormatting.RESET + " kbps";
                }
            }
            else if (var7.bitrateKbps == 0.0F)
            {
                var9 = EnumChatFormatting.RED + "Down!";
            }

            p_180791_2_ -= 15;

            if (this.isSelected(entryID))
            {
                var10 = EnumChatFormatting.BLUE + "(Preferred)";
            }
            else if (var7.defaultServer)
            {
                var10 = EnumChatFormatting.GREEN + "(Default)";
            }

            GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, var7.serverName, p_180791_2_ + 2, p_180791_3_ + 5, 16777215);
            GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, var8, p_180791_2_ + 2, p_180791_3_ + GuiIngestServers.this.fontRendererObj.FONT_HEIGHT + 5 + 3, 3158064);
            GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, var9, this.getScrollBarX() - 5 - GuiIngestServers.this.fontRendererObj.getStringWidth(var9), p_180791_3_ + 5, 8421504);

            if (var10 != null)
            {
                GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, var10, this.getScrollBarX() - 5 - GuiIngestServers.this.fontRendererObj.getStringWidth(var10), p_180791_3_ + 5 + 3 + GuiIngestServers.this.fontRendererObj.FONT_HEIGHT, 8421504);
            }
        }

        @Override
		protected int getScrollBarX()
        {
            return super.getScrollBarX() + 15;
        }
    }
}
