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

import org.factorybuilder3d.game.TextureStorage;
import org.factorybuilder3d.world.Chunk;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;

public class ChunkMeshRenderer
{
	public static final int STRIDE = 8;
	public static final int POSITION_SIZE = 3;
	public static final int POSITION_OFFSET = 0;
	public static final int COLOR_SIZE = 3;
	public static final int COLOR_OFFSET = POSITION_OFFSET + POSITION_SIZE;
	public static final int TEX_COORD_SIZE = 2;
	public static final int TEX_COORD_OFFSET = COLOR_OFFSET + COLOR_SIZE;
	public static final int FLOAT_SIZE = 4;

	public static void renderChunkMesh(Chunk chunk, ChunkMeshBuilder.MeshType meshType)
	{
		if (chunk.getMesh().getVBO(meshType) <= 0)
		{
			return;
		}

		/* Bind the correct texture */
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		TextureStorage.getTexture("terrain").bind();

		if (meshType == ChunkMeshBuilder.MeshType.OPAQUE)
		{
			GL11.glDisable(GL11.GL_BLEND);
		} else if (meshType == ChunkMeshBuilder.MeshType.TRANSLUCENT)
		{
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.0f);
		}

		ChunkMesh mesh = chunk.getMesh();

		/* Bind the buffer */
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, mesh.getVBO(meshType));

		/* Enable the different kinds of data in the buffer */
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		// System.out.println("Chunk Vertices = " + mesh.getVertexCount());

		/* Define the starting positions */
		GL11.glVertexPointer(POSITION_SIZE, GL11.GL_FLOAT, STRIDE * FLOAT_SIZE, POSITION_OFFSET * FLOAT_SIZE);
		GL11.glTexCoordPointer(TEX_COORD_SIZE, GL11.GL_FLOAT, STRIDE * FLOAT_SIZE, TEX_COORD_OFFSET * FLOAT_SIZE);
		GL11.glColorPointer(COLOR_SIZE, GL11.GL_FLOAT, STRIDE * FLOAT_SIZE, COLOR_OFFSET * FLOAT_SIZE);

		/* Draw the buffer */
		GL11.glDrawArrays(GL11.GL_QUADS, 0, mesh.getVertexCount(meshType));

		/* Unbind the buffer */
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);

		/* Disable the different kindds of data */
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		if (meshType == ChunkMeshBuilder.MeshType.TRANSLUCENT)
		{
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
		}
	}

}
