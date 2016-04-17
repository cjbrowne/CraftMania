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
package org.factorybuilder3d.items;


import org.factorybuilder3d.blocks.BlockType;
import org.factorybuilder3d.math.Vec2i;
import org.factorybuilder3d.blocks.BlockManager;
import org.factorybuilder3d.game.TextureStorage;

/**
 *
 * @author martijncourteaux
 */
public abstract class Tool extends TexturedItem
{

    public enum Material
    {

        WOOD, STONE, IRON, DIAMOND, GOLD, BEDROCK
    }
    private BlockType.BlockClass _blockClass;
    private float _health;
    private Material _material;

    protected Tool(String name, BlockType.BlockClass blockClass, Material material, Vec2i texturePosition, float animationSpeed)
    {
        super(name, animationSpeed, TextureStorage.getTexture("items"), texturePosition);

        this._blockClass = blockClass;
        this._material = material;
    }

    @Override
    public void update()
    {
        // Do nothing
    }

    @Override
    public float calcDamageFactorToBlock(byte block)
    {
        BlockType bt = BlockManager.getInstance().getBlockType(block);

        if (bt.getBlockClass() == getBlockClass())
        {
        	if (_material == null) return 4.0f;
            return (_material.ordinal() / 4.0f) + 4.0f;
        }
        return 1.2f;
    }

    @Override
    public float calcDamageInflictedByBlock(byte block)
    {
    	if (_material == Material.BEDROCK)
    	{
    		return 0.0f;
    	}
        BlockType bt = BlockManager.getInstance().getBlockType(block);
        float materialResistance = (0.2f / (float) Math.pow(_material.ordinal(), 1.2d));
        if (bt.getBlockClass() == getBlockClass())
        {
            return materialResistance * (0.05f * bt.getResistance());
        }
        return bt.getResistance() * materialResistance;
    }

    public BlockType.BlockClass getBlockClass()
    {
        return _blockClass;
    }

    @Override
    public boolean isStackable()
    {
        return false;
    }

    @Override
    public void inflictDamage(float toolDamage)
    {
        this._health -= toolDamage;
    }

    @Override
    public float getHealth()
    {
        return _health;
    }
}
