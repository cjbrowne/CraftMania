package org.factorybuilder3d.blocks.customblocks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.factorybuilder3d.game.Game;
import org.factorybuilder3d.inventory.DoubleContainerInventory;
import org.factorybuilder3d.inventory.InventoryIO;
import org.factorybuilder3d.math.Vec3i;
import org.factorybuilder3d.world.Chunk;
import org.factorybuilder3d.blocks.BlockManager;
import org.factorybuilder3d.blocks.DefaultBlock;

public class Container extends DefaultBlock
{

	
	static
	{
		try
		{
			Class.forName("org.craftmania.inventory.DoubleContainerInventory");
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	private DoubleContainerInventory _inventory;
	
	public Container(Chunk chunk, Vec3i pos)
	{
		super(BlockManager.getInstance().getBlockType(BlockManager.getInstance().blockID("container")), chunk, pos);
		_inventory = new DoubleContainerInventory();
		_inventory.setSharedContent(Game.getInstance().getWorld().getActivePlayer().getSharedInventoryContent());
	}

	@Override
	public void performSpecialAction()
	{
		Game.getInstance().getWorld().setActivatedInventory(_inventory);
	}
	
	@Override
	public void saveSpecialSaveData(DataOutputStream dos) throws IOException
	{
		int offset = DoubleContainerInventory.DoubleContainerInventoryRaster.CONTENT_OFFSET;
		
		InventoryIO.writeInventory(_inventory, dos, offset, 54);
	}
	
	@Override
	public void readSpecialSaveData(DataInputStream dis) throws IOException
	{
		System.out.println("Read crafting table inventory");
		int offset = DoubleContainerInventory.DoubleContainerInventoryRaster.CONTENT_OFFSET;
		
		InventoryIO.readInventory(dis, _inventory, offset);
	}
}
