package org.craftmania.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.craftmania.Side;
import org.craftmania.blocks.Block;
import org.craftmania.math.MathHelper;
import org.craftmania.math.Vec3f;

public class ChunkManager
{
	@SuppressWarnings("unused")
	private World _world;
	private Map<Integer, Chunk<BlockChunk>> _superChunks;
	private List<BlockMovement> _blocksToMove;
	private BlockChunkLoader _blockChunkLoader;
	private List<BlockChunk> _blockChunksToInsert;

	public ChunkManager(World world)
	{
		_world = world;
		_superChunks = new HashMap<Integer, Chunk<BlockChunk>>();
		_blocksToMove = new ArrayList<ChunkManager.BlockMovement>();
		_blockChunkLoader = new BlockChunkLoader(world);
	}

	public Collection<Chunk<BlockChunk>> getAllSuperChunks()
	{
		return _superChunks.values();
	}

	public Chunk<BlockChunk> getSuperChunk(int x, int z)
	{
		/* Map to positive integers */
		int posX = MathHelper.mapToPositive(x);
		int posZ = MathHelper.mapToPositive(z);

		/* Apply Cantor's method */
		Integer cantorize = Integer.valueOf(MathHelper.cantorize(posX, posZ));

		Chunk<BlockChunk> superChunk = _superChunks.get(cantorize);
		if (superChunk == null)
		{
			superChunk = new Chunk<BlockChunk>(x, z);
			_superChunks.put(cantorize, superChunk);
		}
		return superChunk;
	}
	
	public BlockChunk getBlockChunk(int x, int z, boolean createIfNecesssary, boolean generateIfNecessary)
	{
		return getBlockChunk(x, z, createIfNecesssary, generateIfNecessary, false);
	}

	public BlockChunk getBlockChunk(int x, int z, boolean createIfNecessary, boolean generateIfNecessary, boolean useEvenIfGenerating)
	{
		int superX = MathHelper.floorDivision(x, Chunk.CHUNK_SIZE_X);
		int superZ = MathHelper.floorDivision(z, Chunk.CHUNK_SIZE_Z);

		int xInChunk = x - superX * Chunk.CHUNK_SIZE_X;
		int zInChunk = z - superZ * Chunk.CHUNK_SIZE_X;

		Chunk<BlockChunk> superChunk = getSuperChunk(superX, superZ);
		BlockChunk blockChunk = superChunk.get(xInChunk, zInChunk);
		if (blockChunk == null && createIfNecessary)
		{
			blockChunk = new BlockChunk(x, z);
			assignNeighbors(blockChunk);
			superChunk.set(xInChunk, zInChunk, blockChunk);
		}
		if (blockChunk != null && generateIfNecessary && !blockChunk.isLoading() && !blockChunk.isGenerated() && !blockChunk.isDestroying())
		{
			blockChunk.generate();
			if (useEvenIfGenerating)
			{
				return blockChunk;
			} else
			{
				return null;
			}
		}
		if ((blockChunk == null || blockChunk.isLoading()) && !useEvenIfGenerating)
		{
			return null;
		} else
		{
			return blockChunk;
		}
	}

	public void assignNeighbors(BlockChunk blockChunk)
	{
		int x = blockChunk.getX();
		int z = blockChunk.getZ();
		blockChunk.setNeighborBlockChunk(Side.BACK, getBlockChunk(x, z - 1, false, false, true));
		blockChunk.setNeighborBlockChunk(Side.FRONT, getBlockChunk(x, z + 1, false, false, true));
		blockChunk.setNeighborBlockChunk(Side.LEFT, getBlockChunk(x - 1, z, false, false, true));
		blockChunk.setNeighborBlockChunk(Side.RIGHT, getBlockChunk(x + 1, z, false, false, true));
	}

	public Block getBlock(int x, int y, int z, boolean createIfNecessary, boolean generateIfNecessary)
	{
		BlockChunk blockChunk = getBlockChunkContaining(x, y, z, createIfNecessary, generateIfNecessary);
		if (blockChunk == null)
			return null;
		return blockChunk.getBlockAbsolute(x, y, z);
	}

	public void setBlock(int x, int y, int z, byte blockType, boolean createIfNecessary, boolean generateIfNecessary)
	{
		BlockChunk blockChunk = getBlockChunkContaining(x, y, z, createIfNecessary, generateIfNecessary);
		if (blockChunk == null)
		{
			System.out.println("BlockChunk not found for " + x + " " + z);
			return;
		}
		blockChunk.setBlockTypeAbsolute(x, y, z, blockType, createIfNecessary, generateIfNecessary);
	}

	public List<BlockChunk> getApproximateChunks(Vec3f position, float viewingDistance, List<BlockChunk> chunks)
	{
		viewingDistance /= BlockChunk.BLOCKCHUNK_SIZE_HORIZONTAL;
		viewingDistance += 1.1f;

		int distance = MathHelper.ceil(viewingDistance);
		int distanceSq = distance * distance;

		int centerX = MathHelper.floor(position.x() / BlockChunk.BLOCKCHUNK_SIZE_HORIZONTAL);
		int centerZ = MathHelper.floor(position.z() / BlockChunk.BLOCKCHUNK_SIZE_HORIZONTAL);

		chunks.clear();

		for (int x = -distance; x <= distance; ++x)
		{
			for (int z = -distance; z <= distance; ++z)
			{
				int distSq = x * x + z * z;
				if (distSq <= distanceSq)
				{
					BlockChunk chunk = getBlockChunk(centerX + x, centerZ + z, false, false, false);
					if (chunk != null && !chunk.isDestroying())
					{
						chunks.add(chunk);
					}
				}
			}
		}

		return chunks;
	}

	public BlockChunk getBlockChunkContaining(int x, int y, int z, boolean createIfNecessary, boolean generateIfNecessary)
	{
		return getBlockChunk(MathHelper.floorDivision(x, BlockChunk.BLOCKCHUNK_SIZE_HORIZONTAL), MathHelper.floorDivision(z, BlockChunk.BLOCKCHUNK_SIZE_HORIZONTAL),
				createIfNecessary, generateIfNecessary);
	}

	/**
	 * Moves a {@code Block} to a given point, this point might be in another
	 * chunk.
	 * 
	 * @param block
	 * @param newChunk
	 */
	public void moveBlockTo(Block block, int srcX, int srcY, int srcZ, int dstX, int dstY, int dstZ)
	{
		BlockChunk oldChunk = block.getBlockChunk();
		BlockChunk newChunk = oldChunk.getBlockChunkContaining(dstX, dstY, dstZ, true, false);

		oldChunk.setBlockAbsolute(srcX, srcY, srcZ, null, false, false);
		newChunk.setBlockAbsolute(dstX, dstY, dstZ, block, true, false);
	}

	public void rememberBlockMovement(Block block, int srcX, int srcY, int srcZ, int dstX, int dstY, int dstZ)
	{
		_blocksToMove.add(new BlockMovement(block, srcX, srcY, srcZ, dstX, dstY, dstZ));
	}

	public void performRememberedBlockChanges()
	{
		for (BlockMovement bm : _blocksToMove)
		{
			// System.out.println(bm);
			moveBlockTo(bm.block, bm.srcX, bm.srcY, bm.srcZ, bm.dstX, bm.dstY, bm.dstZ);
		}
		_blocksToMove.clear();
	}

	private class BlockMovement
	{

		public Block block;
		public int srcX, srcY, srcZ;
		public int dstX, dstY, dstZ;

		public BlockMovement(Block block, int srcX, int srcY, int srcZ, int dstX, int dstY, int dstZ)
		{
			this.block = block;
			this.srcX = srcX;
			this.srcY = srcY;
			this.srcZ = srcZ;
			this.dstX = dstX;
			this.dstY = dstY;
			this.dstZ = dstZ;
		}

		@Override
		public String toString()
		{
			return "BlockMovement{" + "block=" + block + ", srcX=" + srcX + ", srcY=" + srcY + ", srcZ=" + srcZ + ", dstX=" + dstX + ", dstY=" + dstY + ", dstZ=" + dstZ + '}';
		}
	}

	public void destroyBlock(Block block)
	{
		block.getBlockChunk().setBlockTypeAbsolute(block.getX(), block.getY(), block.getZ(), (byte) 0, false, false);

		forgetBlockMovementsForBlock(block);

		/* Clear it from the lists */
		block.removeFromVisibilityList();
		block.getBlockChunk().getUpdatingBlocks().rememberToRemoveBlock(block);
	}

	public void forgetBlockMovementsForBlock(Block block)
	{
		/* Forget all remember block actions (i.e.: movements) */
		for (Iterator<BlockMovement> it = _blocksToMove.iterator(); it.hasNext();)
		{
			BlockMovement bm = it.next();
			if (bm.block == block)
			{
				it.remove();
			}
		}
	}

	public int getTotalBlockChunkCount()
	{
		int count = 0;
		for (Chunk<BlockChunk> ch : getAllSuperChunks())
		{
			count += ch.objectCount();
		}
		return count;
	}

	public void deleteChunk(BlockChunk chunk)
	{
		_blockChunkLoader.deleteChunk(chunk);
	}

	public void insertChunk(BlockChunk chunk)
	{
		_blockChunksToInsert.add(chunk);
	}

	public void generateOrLoadChunk(BlockChunk blockChunk)
	{
		_blockChunkLoader.generateOrLoadChunk(blockChunk.getX(), blockChunk.getZ());
	}

	public boolean isBlockChunkLoaderBusy()
	{
		return _blockChunkLoader.isBusy();
	}
}