package dev.powercyphe.stealthcraft.common.data;

import dev.powercyphe.stealthcraft.common.init.SCTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;

import java.util.concurrent.CompletableFuture;

public class SCEntityTypeTagProvider extends FabricTagsProvider<EntityType<?>> {
    public SCEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.ENTITY_TYPE, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(SCTags.NO_VISION_HANDLER)
                .add(EntityTypeIds.WARDEN)
                .add(EntityTypeIds.PLAYER);
    }
}
