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

package io.vram.frex.mixin;

import java.util.List;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.loader.api.FabricLoader;

public class FrexMixinPlugin implements IMixinConfigPlugin {
	private final int packagePrefixLen = "io.vram.frex.mixin.".length();

	final ObjectOpenHashSet<String> inactiveSet = new ObjectOpenHashSet<>();

	public FrexMixinPlugin() {
		if (!FabricLoader.getInstance().isModLoaded("fabric-renderer-api-v1")) {
			inactiveSet.add("MixinFabricSpriteFinder");
			inactiveSet.add("MixinFabricBakedModel");
			inactiveSet.add("MixinSpriteFinderHolder");
			inactiveSet.add("MixinFrex");
		}

		if (!FabricLoader.getInstance().isModLoaded("fabric-rendering-fluids-v1")) {
			inactiveSet.add("MixinFluidRenderHandler");
		}

		if (!FabricLoader.getInstance().isModLoaded("fabric-rendering-data-attachment-v1")) {
			inactiveSet.add("MixinBlockEntityRenderDataImpl");
		}
	}

	@SuppressWarnings("unused")
	private final Logger log = LogManager.getLogger("FREX");

	@Override
	public void onLoad(String mixinPackage) {
		// NOOP
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return !inactiveSet.contains(mixinClassName.substring(packagePrefixLen));
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
		// NOOP
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		// NOOP
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		// NOOP
	}
}