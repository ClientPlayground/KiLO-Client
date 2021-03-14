package com.kilo.ui.inter.slotlist.slot;

import com.kilo.Kilo;
import com.kilo.manager.ActivityManager;
import com.kilo.render.Colors;
import com.kilo.render.Draw;
import com.kilo.render.Fonts;
import com.kilo.ui.UIHandler;
import com.kilo.ui.UIInGameMenu;
import com.kilo.ui.UIPopupMessageReply;
import com.kilo.ui.inter.IconButton;
import com.kilo.ui.inter.Inter;
import com.kilo.ui.inter.slotlist.SlotList;
import com.kilo.ui.inter.slotlist.part.Activity;
import com.kilo.util.Align;
import com.kilo.util.Resources;
import com.kilo.util.ServerUtil;
import com.kilo.util.Util;

public class ActivitySlotNewMessage extends ActivitySlot {

	public ActivitySlotNewMessage(SlotList p, int i, float x, float y, float w, float h, float ox, float oy) {
		super(p, i, x, y, w, h, ox, oy);
		
		inters.add(new IconButton(this, x+w-12, y+20, 8, 8, Colors.GREEN.c, Resources.iconGoto[0]));
	}
	
	public void update(int mx, int my) {
		super.update(mx, my);

		inters.get(0).x = x+width-12;
		inters.get(0).y = y+20;
	}
	
	public void mouseClick(int mx, int my, int b) {
		super.mouseClick(mx, my, b);
	}
	
	public void doubleClick(int mx, int my) {
		((UIInGameMenu)UIHandler.currentUI).changePopup(new UIPopupMessageReply(UIHandler.currentUI, ActivityManager.getActivity(index).minecraftName, ActivityManager.getActivity(index).message));
		new Thread() {
			@Override
			public void run() {
				ServerUtil.removeActivity(Kilo.kilo().client.clientID, ActivityManager.getActivity(index).id);
				ActivityManager.removeActivity(ActivityManager.getActivity(index));
				//UpdateManager.updateLatestActivityList();
			}
		}.start();
	}
	
	@Override
	public void interact(Inter i) {
		switch (i.type) {
		default:
			break;
		case button:
			switch(inters.indexOf(i)) {
			case 0:
				doubleClick(0, 0);
				break;
			}
			break;
		case slider:
			break;
		case checkbox:
			break;
		case textbox:
			break;
		case link:
			break;
		}
	}
	
	public void render(float opacity) {
		super.render(opacity);
		if (ActivityManager.getActivity(index) != null) {
			if (ActivityManager.getActivity(index).kiloName != null) {
				String[] lines = new String[] {
						((Activity)ActivityManager.getActivity(index)).minecraftName,
						"\u00a77Sent you a message"
				};
				int k = 0;
				for(String l : lines) {
					for(int i = 0; i < l.length(); i++) {
						if (Fonts.ttfRoundedBold10.getWidth(l.substring(0, i)) > parent.w-60-Fonts.ttfRoundedBold10.getWidth("...")) {
							l = l.substring(0, i)+"...";
							break;
						}
					}
					Draw.string(Fonts.ttfRoundedBold10, x+48, y+24-((Fonts.ttfRoundedBold10.getHeight()*(lines.length-1))/2)+(Fonts.ttfRoundedBold10.getHeight()*k), l, Util.reAlpha(Colors.WHITE.c, 1f*opacity), Align.L, Align.C);
					k++;
				}
			}
		}
	}
	
}
