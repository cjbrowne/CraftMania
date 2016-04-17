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



import static org.lwjgl.opengl.GL11.*;

import org.factorybuilder3d.inventory.InventoryItem;
import org.factorybuilder3d.math.Vec2f;
import org.factorybuilder3d.math.Vec2i;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author martijncourteaux
 */
public abstract class TexturedItem extends InventoryItem
{

    protected Vec2i _texturePosition;
    protected Texture _texture;
    protected Vec2f _texPosUpLeft;
    protected Vec2f _texPosDownRight;

    public TexturedItem(String name, float animationSpeed, Texture texture, Vec2i texPos)
    {
        super(name, animationSpeed);
        this._texture = texture;
        this._texturePosition = texPos;
        
        this._texPosUpLeft = new Vec2f((float) 16.0f * _texturePosition.x() / texture.getImageWidth(), (float) 16.0f * _texturePosition.y() / texture.getImageHeight());
        this._texPosDownRight = new Vec2f(_texPosUpLeft.x() + 0.0624f, _texPosUpLeft.y() + (16.0f / texture.getImageHeight()));
        
        
    }

    @Override
    public float calcDamageFactorToBlock(byte block)
    {
        return 1.1f;
    }

    @Override
    public float calcDamageInflictedByBlock(byte block)
    {
        return 0.0f;
    }

    @Override
    public short getInventoryTypeID()
    {
        int w = _texture.getImageWidth() / 16;
        return (short) (ItemManager.ITEM_OFFSET + (_texturePosition.y() * w + _texturePosition.x()));
    }

    @Override
    public void renderInventoryItem()
    {
        glEnable(GL_TEXTURE_2D);
        /* Render the texture */

        float hw = 16f;
        float hh = 16f;

        _texture.bind();


        glBegin(GL_QUADS);
        glTexCoord2f(_texPosUpLeft.x(), _texPosUpLeft.y());
        glVertex3f(-hw, hh, 0.0f);
        glTexCoord2f(_texPosDownRight.x(), _texPosUpLeft.y());
        glVertex3f(hw, hh, 0.0f);
        glTexCoord2f(_texPosDownRight.x(), _texPosDownRight.y());
        glVertex3f(hw, -hh, 0.0f);
        glTexCoord2f(_texPosUpLeft.x(), _texPosDownRight.y());
        glVertex3f(-hw, -hh, 0.0f);
        glEnd();
    }

    @Override
    public float getHealth()
    {
        return 1.0f;
    }

    @Override
    public abstract boolean isStackable();

    @Override
    public void inflictDamage(float toolDamage)
    {
        
    }

    @Override
    public abstract void update();
}
