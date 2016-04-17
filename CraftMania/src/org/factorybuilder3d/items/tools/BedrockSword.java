package org.factorybuilder3d.items.tools;

import org.factorybuilder3d.items.Tool;
import org.factorybuilder3d.math.Vec2i;

public class BedrockSword extends Tool
{

	public BedrockSword()
	{
		super("bedrock_sword", null, Material.BEDROCK, new Vec2i(4, 16), 5.0f);
	}

}
