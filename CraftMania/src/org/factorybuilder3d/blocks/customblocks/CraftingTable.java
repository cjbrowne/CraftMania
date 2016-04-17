/*******************************************************************************
 * Copyright 2012 Martijn Courteaux <martijn.courteaux@skynet.be>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.factorybuilder3d.blocks.customblocks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.factorybuilder3d.inventory.InventoryIO;
import org.factorybuilder3d.blocks.BlockManager;
import org.factorybuilder3d.blocks.DefaultBlock;
import org.factorybuilder3d.game.Game;
import org.factorybuilder3d.inventory.CraftingTableInventory;
import org.factorybuilder3d.math.Vec3i;
import org.factorybuilder3d.world.Chunk;

public class CraftingTable extends DefaultBlock
{
	
	static
	{
		try
		{
			Class.forName("org.craftmania.inventory.CraftingTableInventory");
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	private CraftingTableInventory _inventory;
	
	public CraftingTable(Chunk chunk, Vec3i pos)
	{
		super(BlockManager.getInstance().getBlockType(BlockManager.getInstance().blockID("crafting_table")), chunk, pos);
		_inventory = new CraftingTableInventory();
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
		int offset = CraftingTableInventory.CraftingTableInventoryRaster.CRAFTING_OFFSET;
		
		InventoryIO.writeInventory(_inventory, dos, offset, 9);
	}
	
	@Override
	public void readSpecialSaveData(DataInputStream dis) throws IOException
	{
		System.out.println("Read crafting table inventory");
		int offset = CraftingTableInventory.CraftingTableInventoryRaster.CRAFTING_OFFSET;
		
		InventoryIO.readInventory(dis, _inventory, offset);
		_inventory.checkForRecipe();
	}
	
}
