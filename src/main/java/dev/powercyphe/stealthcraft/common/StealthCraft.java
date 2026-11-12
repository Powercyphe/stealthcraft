package dev.powercyphe.stealthcraft.common;

import dev.powercyphe.stealthcraft.common.init.EntityVisionOverrideRegistry;
import dev.powercyphe.stealthcraft.common.init.SCTags;
import eu.midnightdust.lib.config.MidnightConfig;
import dev.powercyphe.stealthcraft.common.init.SCAttachmentTypes;
import dev.powercyphe.stealthcraft.common.init.SCMemoryModuleTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StealthCraft implements ModInitializer {
    public static final String MOD_ID = "stealthcraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MidnightConfig.register(MOD_ID, SCConfig.class);

        EntityVisionOverrideRegistry.init();
        SCAttachmentTypes.init();
        SCMemoryModuleTypes.init();
        SCTags.init();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
