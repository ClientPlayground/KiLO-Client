package com.kilo.mod.all;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.WorldClient;

import org.lwjgl.input.Keyboard;

import com.kilo.mod.Category;
import com.kilo.mod.Module;
import com.kilo.mod.toolbar.dropdown.Interactable;
import com.kilo.util.Util;

public class AutoJump extends Module {
	
	public AutoJump(String finder, Category category, String name, String description, String warning) {
		super(finder, category, name, description, warning);
	}
	
	public void onPlayerUpdate() {
		if (mc.thePlayer.onGround) {
			mc.thePlayer.jump();
		}
	}
}
