package org.factorybuilder3d.items.tools;

import org.factorybuilder3d.blocks.BlockType;
import org.factorybuilder3d.math.Vec2i;
import org.factorybuilder3d.items.Tool;

public class BedrockShovel extends Tool

{
	public BedrockShovel()
	{
		super("bedrock_shovel", BlockType.BlockClass.SAND, Material.BEDROCK, new Vec2i(2, 16), 7.0f);
	}

}
