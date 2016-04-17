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
package org.factorybuilder3d.items.tools;

import org.factorybuilder3d.blocks.BlockType.BlockClass;
import org.factorybuilder3d.items.Tool;
import org.factorybuilder3d.math.Vec2i;

public class StonePickaxe extends Tool
{
    public StonePickaxe()
    {
        super("stone_pickaxe", BlockClass.STONE, Material.STONE, new Vec2i(1, 6), 7.0f);
    }
}
