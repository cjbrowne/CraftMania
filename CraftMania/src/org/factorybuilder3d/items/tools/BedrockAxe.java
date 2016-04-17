package org.factorybuilder3d.items.tools;

import org.factorybuilder3d.blocks.BlockType;
import org.factorybuilder3d.items.Tool;
import org.factorybuilder3d.math.Vec2i;

public class BedrockAxe extends Tool

{

	public BedrockAxe()
	{
		 super("bedrock_axe", BlockType.BlockClass.WOOD, Material.BEDROCK, new Vec2i(1, 16), 7.0f);
	}
}
