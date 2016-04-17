package org.factorybuilder3d.blocks.customblocks;

import java.nio.FloatBuffer;

import org.factorybuilder3d.Side;
import org.factorybuilder3d.blocks.Block;
import org.factorybuilder3d.blocks.BlockType;
import org.factorybuilder3d.blocks.DefaultBlock;
import org.factorybuilder3d.blocks.DefaultBlockBrush;
import org.factorybuilder3d.math.Vec2f;
import org.factorybuilder3d.math.Vec3i;
import org.factorybuilder3d.world.Chunk;
import org.factorybuilder3d.world.LightBuffer;
import org.factorybuilder3d.blocks.BlockManager;

public class RedstoneLamp extends DefaultBlock implements RedstoneLogic
{

	private final static DefaultBlockBrush BRUSH_POWERED;
	public static final byte LUMINOSITY = 15;

	static
	{
		BRUSH_POWERED = new DefaultBlockBrush();
		BRUSH_POWERED.setTexture(new Vec2f(4, 13));
	}

	private boolean _powered;

	public RedstoneLamp(Chunk chunk, Vec3i pos)
	{
		super(BlockManager.getInstance().getBlockType("redstone_lamp"), chunk, pos);
	}

	@Override
	public void feed(int power)
	{
		System.out.println("RedstoneLamp power = " + power);
		if (!_powered)
		{
			_powered = true;
			_chunk.spreadLight(getX(), getY(), getZ(), (byte) 15, Chunk.LightType.BLOCK);
		}
	}

	@Override
	public void unfeed(int power)
	{
		if (_powered)
		{
			_powered = false;
			_chunk.unspreadLight(getX(), getY(), getZ(), (byte) 15, Chunk.LightType.BLOCK);
		}
	}

	@Override
	public boolean isPowered()
	{
		return _powered;
	}

	@Override
	public void connect(Side side)
	{

	}

	@Override
	public void disconnect(Side side)
	{
	}
	
	@Override
	public synchronized void neighborChanged(Side side)
	{
		super.neighborChanged(side);
		
		Vec3i n = side.getNormal();
		byte bType = _chunk.getBlockTypeAbsolute(getX() + n.x(), getY() + n.y(), getZ() + n.z(), false, false, false);
		if (bType <= 0)
		{
			disconnect(side);
			return;
		}
		BlockType type = BlockManager.getInstance().getBlockType(bType);
		if (type.hasRedstoneLogic())
		{
			Block block = _chunk.getSpecialBlockAbsolute(getX() + n.x(), getY() + n.y(), getZ() + n.z());
			RedstoneLogic rl = (RedstoneLogic) block;
			rl.connect(Side.getOppositeSide(side));
			connect(side);
		} else
		{
			disconnect(side);
		}
	}

	@Override
	public void destruct()
	{
		super.destruct();

		if (_powered)
		{
			_chunk.unspreadLight(getX(), getY(), getZ(), (byte) 15, Chunk.LightType.BLOCK);
		}
	}

	@Override
	public void storeInVBO(FloatBuffer vbo, LightBuffer lightBuffer)
	{
		if (!_powered)
		{
			super.storeInVBO(vbo, lightBuffer);
		} else
		{
			BRUSH_POWERED.setFaceMask(getFaceMask());
			BRUSH_POWERED.storeInVBO(vbo, getX() + 0.5f, getY() + 0.5f, getZ() + 0.5f, lightBuffer);
		}
	}

}
