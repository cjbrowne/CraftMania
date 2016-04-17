package org.factorybuilder3d.items.tools;

import org.factorybuilder3d.items.Tool;
import org.factorybuilder3d.math.Vec2i;

public class WoodenSword extends Tool
{

	public WoodenSword()
	{
		super("wooden_sword", null, Material.WOOD, new Vec2i(0, 4), 7.0f);
	}

}
