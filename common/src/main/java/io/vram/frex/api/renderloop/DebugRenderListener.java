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

package io.vram.frex.api.renderloop;

import io.vram.frex.impl.renderloop.DebugRenderListenerImpl;

/**
 * Called before vanilla debug renderers are output to the framebuffer.
 * This happens very soon after entities, block breaking and most other
 * non-translucent renders but before translucency is drawn.
 *
 * <p>Unlike most other events, renders in this event are expected to be drawn
 * directly and immediately to the framebuffer. The OpenGL render state view
 * matrix will be transformed to match the camera view before the event is called.
 *
 * <p>Use to drawn lines, overlays and other content similar to vanilla
 * debug renders.
 */
@FunctionalInterface
public interface DebugRenderListener {
	void beforeDebugRender(WorldRenderContext context);

	static void register(DebugRenderListener listener) {
		DebugRenderListenerImpl.register(listener);
	}

	/**
	 * Must be called by renderer implementations if they
	 * disable the hooks implemented by FREX.
	 */
	static void invoke(WorldRenderContext context) {
		DebugRenderListenerImpl.invoke(context);
	}
}