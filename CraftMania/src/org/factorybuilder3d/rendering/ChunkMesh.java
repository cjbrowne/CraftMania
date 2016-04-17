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
package org.factorybuilder3d.rendering;

public class ChunkMesh
{
	private int _vertexCount[];
	private int[] _vbos;

	public ChunkMesh(int[] vertexCount, int[] vbos)
	{
		super();
		this._vertexCount = vertexCount;
		this._vbos = vbos;
	}

	public ChunkMesh()
	{
		_vbos = new int[ChunkMeshBuilder.MeshType.values().length];
		_vertexCount = new int[ChunkMeshBuilder.MeshType.values().length];
	}

	public void setVBO(ChunkMeshBuilder.MeshType meshType, int vbo)
	{
		this._vbos[meshType.ordinal()] = vbo;
	}

	public void setVertexCount(ChunkMeshBuilder.MeshType meshType, int vertexCount)
	{
		this._vertexCount[meshType.ordinal()] = vertexCount;
	}

	public int getVBO(ChunkMeshBuilder.MeshType meshType)
	{
		return _vbos[meshType.ordinal()];
	}

	public int getVertexCount(ChunkMeshBuilder.MeshType meshType)
	{
		return _vertexCount[meshType.ordinal()];
	}

	public synchronized void destroy(ChunkMeshBuilder.MeshType meshType)
	{
		if (_vbos[meshType.ordinal()] != 0 && _vbos[meshType.ordinal()] != -1)
		{
			BufferManager.getInstance().deleteBuffer(_vbos[meshType.ordinal()]);
			_vbos[meshType.ordinal()] = 0;
			_vertexCount[meshType.ordinal()] = 0;
		}
	}

	public void destroyAllMeshes()
	{
		destroy(ChunkMeshBuilder.MeshType.OPAQUE);
		destroy(ChunkMeshBuilder.MeshType.TRANSLUCENT);
	}
}
