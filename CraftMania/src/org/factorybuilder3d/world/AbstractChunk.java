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
package org.factorybuilder3d.world;

import org.factorybuilder3d.datastructures.AABB;
import org.factorybuilder3d.datastructures.AABBObject;
import org.factorybuilder3d.datastructures.Fast3DArray;

public class AbstractChunk<T extends AABBObject> implements AABBObject
{
	public static final int CHUNK_SIZE_X = 8;
	public static final int CHUNK_SIZE_Z = 8;

	private int _x, _z;
	private Fast3DArray<T> _content;
	private AABB _aabb;
	private int _objectCount;

	public AbstractChunk(int x, int z)
	{
		_x = x;
		_z = z;
		_content = new Fast3DArray<T>(CHUNK_SIZE_X, 1, CHUNK_SIZE_Z);
		_aabb = null;
	}

	public int getX()
	{
		return _x;
	}

	public int getZ()
	{
		return _z;
	}

	public T get(int x, int z)
	{
		return _content.get(x, 0, z);
	}

	public void set(int x, int z, T obj)
	{
		T old = _content.set(x, 0, z, obj);
		if (obj == null || old != null)
		{
			createAABB();
		} else
		{
			if (_aabb == null)
			{
				_aabb = new AABB(obj.getAABB());
			} else
			{
				_aabb.include(obj.getAABB());
			}
		}

		if (obj != null && old == null)
		{
			_objectCount++;
		}
		if (obj == null && old != null)
		{
			_objectCount--;
		}
	}

	public void forceCountObjects()
	{
		int oldVal = _objectCount;
		_objectCount = 0;

		for (int i = 0; i < _content.size(); ++i)
		{
			if (_content.getRawObject(i) != null)
			{
				_objectCount++;
			}
		}

		if (oldVal != _objectCount)
		{
			System.err.println("Object count is " + _objectCount + " and we though it was " + oldVal);
		}
	}

	private void createAABB()
	{
		_aabb = null;
		for (int i = 0; i < _content.size(); ++i)
		{
			T obj = _content.getRawObject(i);
			if (obj == null)
				continue;
			if (_aabb == null)
			{
				_aabb = new AABB(obj.getAABB());
			} else
			{
				_aabb.include(obj.getAABB());
			}
		}
	}

	@Override
	public AABB getAABB()
	{
		return _aabb;
	}

	public int objectCount()
	{
		return _objectCount;
	}
}
