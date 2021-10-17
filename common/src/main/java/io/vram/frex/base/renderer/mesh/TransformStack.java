/*
 * Copyright © Contributing Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional copyright and licensing notices may apply for content that was
 * included from other projects. For more information, see ATTRIBUTION.md.
 */

package io.vram.frex.base.renderer.mesh;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import io.vram.frex.api.buffer.QuadTransform;
import io.vram.frex.api.model.InputContext;

public class TransformStack {
	protected final ObjectArrayList<TransformingQuadEmitter> POOL = new ObjectArrayList<>();

	public TransformingQuadEmitter createTransform(InputContext context, QuadTransform transform, BaseQuadEmitter output) {
		return (POOL.isEmpty() ? new TransformingQuadEmitter(this) : POOL.pop()).prepare(context, transform, output);
	}

	public void reclaim(TransformingQuadEmitter transformingQuadEmitter) {
		POOL.add(transformingQuadEmitter);
	}
}