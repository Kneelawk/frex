/*
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License.  You may obtain a copy
 *  of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package io.vram.frex.api.texture;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import io.vram.frex.api.mesh.QuadEditor;
import io.vram.frex.api.mesh.QuadView;
import io.vram.frex.impl.texture.SpriteFinderHolder;

/**
 * Indexes a texture atlas to allow fast lookup of Sprites from
 * baked vertex coordinates.  Main use is for {@link Mesh}-based models
 * to generate vanilla quads on demand without tracking and retaining
 * the sprites that were baked into the mesh. In other words, this class
 * supplies the sprite parameter for {@link QuadView#toBakedQuad(int, TextureAtlasSprite, boolean)}.
 */
@FunctionalInterface
public interface SpriteFinder {
	/**
	 * Retrieves or creates the finder for the given atlas.
	 * Instances should not be retained as fields or they must be
	 * refreshed whenever there is a resource reload or other event
	 * that causes atlas textures to be re-stitched.
	 */
	static SpriteFinder get(TextureAtlas atlas) {
		return SpriteFinderHolder.get(atlas);
	}

	/**
	 * Finds the atlas sprite containing the vertex centroid of the quad.
	 * Vertex centroid is essentially the mean u,v coordinate - the intent being
	 * to find a point that is unambiguously inside the sprite (vs on an edge.)
	 *
	 * <p>Should be reliable for any convex quad or triangle. May fail for non-convex quads.
	 * Note that all the above refers to u,v coordinates. Geometric vertex does not matter,
	 * except to the extent it was used to determine u,v.
	 */
	default TextureAtlasSprite find(QuadView quad) {
		final float u = quad.u(0) + quad.u(1) + quad.u(2) + quad.u(3);
		final float v = quad.v(0) + quad.v(1) + quad.v(2) + quad.v(3);
		return find(u * 0.25f, v * 0.25f);
	}

	/**
	 * Alternative to {@link #find(QuadView)} when vertex centroid is already
	 * known or unsuitable.  Expects normalized (0-1) coordinates on the atlas texture,
	 * which should already be the case for u,v values in vanilla baked quads and in
	 * {@link QuadView} after calling {@link QuadEditor#spriteBake(int, TextureAtlasSprite, int)}.
	 *
	 * <p>Coordinates must be in the sprite interior for reliable results. Generally will
	 * be easier to use {@link #find(QuadView, int)} unless you know the vertex
	 * centroid will somehow not be in the quad interior. This method will be slightly
	 * faster if you already have the centroid or another appropriate value.
	 */
	TextureAtlasSprite find(float u, float v);
}
