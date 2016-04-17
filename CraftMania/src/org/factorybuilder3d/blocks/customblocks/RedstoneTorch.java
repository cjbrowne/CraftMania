package org.factorybuilder3d.blocks.customblocks;

import org.factorybuilder3d.blocks.CrossedBlock;
import org.factorybuilder3d.Side;
import org.factorybuilder3d.blocks.Block;
import org.factorybuilder3d.blocks.BlockManager;
import org.factorybuilder3d.blocks.BlockType;
import org.factorybuilder3d.math.Vec3i;
import org.factorybuilder3d.world.Chunk;

public class RedstoneTorch extends CrossedBlock implements RedstoneLogic
{

	private int _connectionCount;
	private boolean[] _connections;

	public RedstoneTorch(Chunk chunk, Vec3i pos)
	{
		super(BlockManager.getInstance().getBlockType("redstone_torch"), chunk, pos);
		addToVisibilityList();

		_connections = new boolean[6];

	}

	@Override
	public void feed(int power)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void unfeed(int power)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPowered()
	{
		return true;
	}

	@Override
	public void connect(Side side)
	{
		if (!_connections[side.ordinal()])
		{
			_connectionCount++;
		}
		_connections[side.ordinal()] = true;
		if (isPowered())
		{
			feedNeighbor(side, MAXIMUM_REDSTONE_TRAVELING_DISTANCE);
		}
	}

	@Override
	public void disconnect(Side side)
	{
		if (_connections[side.ordinal()])
		{
			_connectionCount--;
			_connections[side.ordinal()] = false;
			if (isPowered())
			{
				unfeedNeighbor(side, MAXIMUM_REDSTONE_TRAVELING_DISTANCE);
			}
		}
	}

	private void feedNeighbor(Side side, int power)
	{
		Vec3i n = side.getNormal();
		Block bl = _chunk.getSpecialBlockAbsolute(getX() + n.x(), getY() + n.y(), getZ() + n.z());
		if (bl == null)
		{
			return;
		}
		if (bl instanceof RedstoneLogic)
		{
			RedstoneLogic rl = (RedstoneLogic) bl;
			rl.feed(power);
		}
	}

	private void unfeedNeighbor(Side side, int power)
	{
		Vec3i n = side.getNormal();
		Block bl = _chunk.getSpecialBlockAbsolute(getX() + n.x(), getY() + n.y(), getZ() + n.z());
		if (bl == null)
		{
			return;
		}
		if (bl instanceof RedstoneLogic)
		{
			RedstoneLogic rl = (RedstoneLogic) bl;
			rl.unfeed(power);
		}
	}

	@Override
	public void neighborChanged(Side side)
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
		disconnect(Side.BACK);
		disconnect(Side.FRONT);
		disconnect(Side.LEFT);
		disconnect(Side.RIGHT);
		disconnect(Side.TOP);
		disconnect(Side.BOTTOM);
	}

}
