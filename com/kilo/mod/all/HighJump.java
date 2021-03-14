package com.kilo.mod.all;

import com.kilo.mod.Category;
import com.kilo.mod.Module;
import com.kilo.mod.toolbar.dropdown.Interactable;

public class HighJump extends Module {

	public HighJump(String finder, Category category, String name, String description, String warning) {
		super(finder, category, name, description, warning);
		
		addOption("Height", "Jump height multiplier", Interactable.TYPE.SLIDER, 3, new float[] {1, 20}, true);
	}
}
