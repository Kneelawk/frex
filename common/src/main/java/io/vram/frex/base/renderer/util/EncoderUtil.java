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

package io.vram.frex.base.renderer.util;

import com.mojang.blaze3d.vertex.VertexConsumer;

import io.vram.frex.api.math.FastMatrix3f;
import io.vram.frex.api.math.FastMatrix4f;
import io.vram.frex.api.model.BakedInputContext;
import io.vram.frex.api.model.InputContext;
import io.vram.frex.api.model.util.ColorUtil;
import io.vram.frex.api.model.util.PackedVector3f;
import io.vram.frex.base.renderer.mesh.BaseQuadEmitter;
import io.vram.frex.base.renderer.mesh.MeshEncodingHelper;

public abstract class EncoderUtil {
	public static void encodeQuad(BaseQuadEmitter quad, InputContext inputContext, VertexConsumer buff) {
		final var matrixStack = inputContext.matrixStack();
		final FastMatrix4f matrix = matrixStack.modelMatrix();
		final FastMatrix3f normalMatrix = matrixStack.normalMatrix();
		final boolean isNormalMatrixUseful = !normalMatrix.f_isIdentity();

		final var mat = quad.material();
		final boolean emissive = mat.emissive();

		final boolean isHurt = mat.hurtOverlay();
		final boolean isFlash = mat.flashOverlay();
		final int overlayV = isHurt ? 3 : 10;
		// 10 is what TNT uses
		final int overlayU = isFlash ? 10 : 0;

		final int quadNormalFlags = quad.normalFlags();
		// don't retrieve if won't be used
		final int faceNormal = quadNormalFlags == 0b1111 ? 0 : quad.packedFaceNormal();
		int packedNormal = 0;
		float nx = 0, ny = 0, nz = 0;

		for (int i = 0; i < 4; i++) {
			quad.transformAndAppendVertex(i, matrix, buff);

			final int color = quad.vertexColor(i);
			buff.color(color & 0xFF, (color >> 8) & 0xFF, (color >> 16) & 0xFF, (color >> 24) & 0xFF);

			buff.uv(quad.u(i), quad.v(i));
			buff.overlayCoords(overlayU, overlayV);
			buff.uv2(emissive ? MeshEncodingHelper.FULL_BRIGHTNESS : quad.lightmap(i));

			final int p = ((quadNormalFlags & 1 << i) == 0) ? faceNormal : quad.packedNormal(i);

			if (p != packedNormal) {
				packedNormal = p;
				final int transformedNormal = isNormalMatrixUseful ? normalMatrix.f_transformPacked3f(packedNormal) : packedNormal;
				nx = PackedVector3f.packedX(transformedNormal);
				ny = PackedVector3f.packedY(transformedNormal);
				nz = PackedVector3f.packedZ(transformedNormal);
			}

			buff.normal(nx, ny, nz);
			buff.endVertex();
		}
	}

	/**
	 * handles block color and red-blue swizzle, common to all renders.
	 */
	public static void colorizeQuad(BaseQuadEmitter quad, BakedInputContext context) {
		final int colorIndex = quad.colorIndex();

		if (colorIndex == -1 || quad.material().disableColorIndex()) {
			quad.vertexColor(0, ColorUtil.swapRedBlueIfNeeded(quad.vertexColor(0)));
			quad.vertexColor(1, ColorUtil.swapRedBlueIfNeeded(quad.vertexColor(1)));
			quad.vertexColor(2, ColorUtil.swapRedBlueIfNeeded(quad.vertexColor(2)));
			quad.vertexColor(3, ColorUtil.swapRedBlueIfNeeded(quad.vertexColor(3)));
		} else {
			final int indexedColor = context.indexedColor(colorIndex);
			quad.vertexColor(0, ColorUtil.swapRedBlueIfNeeded(ColorUtil.multiplyColor(indexedColor, quad.vertexColor(0))));
			quad.vertexColor(1, ColorUtil.swapRedBlueIfNeeded(ColorUtil.multiplyColor(indexedColor, quad.vertexColor(1))));
			quad.vertexColor(2, ColorUtil.swapRedBlueIfNeeded(ColorUtil.multiplyColor(indexedColor, quad.vertexColor(2))));
			quad.vertexColor(3, ColorUtil.swapRedBlueIfNeeded(ColorUtil.multiplyColor(indexedColor, quad.vertexColor(3))));
		}
	}

	public static void applyFlatLighting(BaseQuadEmitter quad, int lightmap) {
		quad.lightmap(0, ColorUtil.maxBrightness(quad.lightmap(0), lightmap));
		quad.lightmap(1, ColorUtil.maxBrightness(quad.lightmap(1), lightmap));
		quad.lightmap(2, ColorUtil.maxBrightness(quad.lightmap(2), lightmap));
		quad.lightmap(3, ColorUtil.maxBrightness(quad.lightmap(3), lightmap));
	}
}