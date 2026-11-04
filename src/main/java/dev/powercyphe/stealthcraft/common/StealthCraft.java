package dev.powercyphe.stealthcraft.common;

import eu.midnightdust.lib.config.MidnightConfig;
import dev.powercyphe.stealthcraft.common.init.SCAttachmentTypes;
import dev.powercyphe.stealthcraft.common.init.SCEntityVisions;
import dev.powercyphe.stealthcraft.common.init.SCMemoryModuleTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class StealthCraft implements ModInitializer {
    public static final String MOD_ID = "stealthcraft";

    @Override
    public void onInitialize() {
        MidnightConfig.register(MOD_ID, SCConfig.class);

        SCAttachmentTypes.init();
        SCMemoryModuleTypes.init();
        SCEntityVisions.init();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
