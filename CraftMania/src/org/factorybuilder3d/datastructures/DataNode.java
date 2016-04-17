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
package org.factorybuilder3d.datastructures;

import java.util.ArrayList;
import java.util.List;

public class DataNode<Data>
{
	private Data _data;
	private List<DataNode<Data>> _childs;
	
	public DataNode(Data d)
	{
		_data = d;
		_childs = new ArrayList<DataNode<Data>>();
	}
	
	public Data getData()
	{
		return _data;
	}
	
	public void addChild(DataNode<Data> node)
	{
		_childs.add(node);
	}
	
	public int childCount()
	{
		return _childs.size();
	}
	
	public DataNode<Data> getChild(int i)
	{
		return _childs.get(i);
	}
	
}
