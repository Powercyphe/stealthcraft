package dev.powercyphe.stealthcraft.client;

import dev.powercyphe.stealthcraft.client.init.SCPipelines;
import dev.powercyphe.stealthcraft.client.init.SCRenderTypes;
import net.fabricmc.api.ClientModInitializer;

public class StealthCraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SCPipelines.init();
        SCRenderTypes.init();
        //SCEntityVisions.init();
    }
}
