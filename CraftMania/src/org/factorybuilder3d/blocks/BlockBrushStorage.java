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
package org.factorybuilder3d.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author martijncourteaux
 */
public class BlockBrushStorage
{
    private static Map<String, BlockBrush> brushes = new HashMap<String, BlockBrush>();

    private BlockBrushStorage()
    {
    }
    
    public static void releaseBrushes()
    {
        for (Entry<String, BlockBrush> entry : brushes.entrySet())
        {
            entry.getValue().release();
        }
    }
    
    public static void registerBrush(String id, BlockBrush bb)
    {
        brushes.put(id, bb);
    }
    
    public static BlockBrush get(String id)
    {
        return brushes.get(id);
    }
}
