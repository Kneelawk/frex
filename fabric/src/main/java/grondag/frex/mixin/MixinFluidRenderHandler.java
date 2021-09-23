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

package grondag.frex.mixin;

import io.vram.frex.api.model.FluidAppearance;
import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;

// Makes FluidRenderHandler and FREX equivalents cross-compatible
@Mixin(FluidRenderHandler.class)
public interface MixinFluidRenderHandler extends FluidAppearance { }