package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class GuiLanguage extends GuiScreen
{
    /** The parent Gui screen */
    protected GuiScreen parentScreen;

    /** The List GuiSlot object reference. */
    private GuiLanguage.List list;

    /** Reference to the GameSettings object. */
    private final GameSettings game_settings_3;

    /** Reference to the LanguageManager object. */
    private final LanguageManager languageManager;

    /**
     * A button which allows the user to determine if the Unicode font should be forced.
     */
    private GuiOptionButton forceUnicodeFontBtn;

    /** The button to confirm the current settings. */
    private GuiOptionButton confirmSettingsBtn;

    public GuiLanguage(GuiScreen screen, GameSettings gameSettingsObj, LanguageManager manager)
    {
        this.parentScreen = screen;
        this.game_settings_3 = gameSettingsObj;
        this.languageManager = manager;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
	public void initGui()
    {
        this.buttonList.add(this.forceUnicodeFontBtn = new GuiOptionButton(100, this.width / 2 - 155, this.height - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
        this.buttonList.add(this.confirmSettingsBtn = new GuiOptionButton(6, this.width / 2 - 155 + 160, this.height - 38, I18n.format("gui.done", new Object[0])));
        this.list = new GuiLanguage.List(this.mc);
        this.list.registerScrollButtons(7, 8);
    }

    /**
     * Handles mouse input.
     */
    @Override
	public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
	protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            switch (button.id)
            {
                case 5:
                    break;

                case 6:
                    this.mc.displayGuiScreen(this.parentScreen);
                    break;

                case 100:
                    if (button instanceof GuiOptionButton)
                    {
                        this.game_settings_3.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
                        button.displayString = this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
                        ScaledResolution var2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                        int var3 = var2.getScaledWidth();
                        int var4 = var2.getScaledHeight();
                        this.setWorldAndResolution(this.mc, var3, var4);
                    }

                    break;

                default:
                    this.list.actionPerformed(button);
            }
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format("options.language", new Object[0]), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning", new Object[0]) + ")", this.width / 2, this.height - 56, 8421504);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    class List extends GuiSlot
    {
        private final java.util.List langCodeList = Lists.newArrayList();
        private final Map languageMap = Maps.newHashMap();

        public List(Minecraft mcIn)
        {
            super(mcIn, GuiLanguage.this.width, GuiLanguage.this.height, 32, GuiLanguage.this.height - 65 + 4, 18);
            Iterator var3 = GuiLanguage.this.languageManager.getLanguages().iterator();

            while (var3.hasNext())
            {
                Language var4 = (Language)var3.next();
                this.languageMap.put(var4.getLanguageCode(), var4);
                this.langCodeList.add(var4.getLanguageCode());
            }
        }

        @Override
		protected int getSize()
        {
            return this.langCodeList.size();
        }

        @Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
        {
            Language var5 = (Language)this.languageMap.get(this.langCodeList.get(slotIndex));
            GuiLanguage.this.languageManager.setCurrentLanguage(var5);
            GuiLanguage.this.game_settings_3.language = var5.getLanguageCode();
            this.mc.refreshResources();
            GuiLanguage.this.fontRendererObj.setUnicodeFlag(GuiLanguage.this.languageManager.isCurrentLocaleUnicode() || GuiLanguage.this.game_settings_3.forceUnicodeFont);
            GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.languageManager.isCurrentLanguageBidirectional());
            GuiLanguage.this.confirmSettingsBtn.displayString = I18n.format("gui.done", new Object[0]);
            GuiLanguage.this.forceUnicodeFontBtn.displayString = GuiLanguage.this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
            GuiLanguage.this.game_settings_3.saveOptions();
        }

        @Override
		protected boolean isSelected(int slotIndex)
        {
            return ((String)this.langCodeList.get(slotIndex)).equals(GuiLanguage.this.languageManager.getCurrentLanguage().getLanguageCode());
        }

        @Override
		protected int getContentHeight()
        {
            return this.getSize() * 18;
        }

        @Override
		protected void drawBackground()
        {
            GuiLanguage.this.drawDefaultBackground();
        }

        @Override
		protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
        {
            GuiLanguage.this.fontRendererObj.setBidiFlag(true);
            GuiLanguage.this.drawCenteredString(GuiLanguage.this.fontRendererObj, ((Language)this.languageMap.get(this.langCodeList.get(entryID))).toString(), this.width / 2, p_180791_3_ + 1, 16777215);
            GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.languageManager.getCurrentLanguage().isBidirectional());
        }
    }
}
