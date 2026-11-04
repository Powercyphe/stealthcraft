package dev.powercyphe.stealthcraft.common.init;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import dev.powercyphe.stealthcraft.common.StealthCraft;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public interface SCAttachmentTypes {

    StreamCodec<ByteBuf, Map<UUID, Float>> PLAYER_VISIBILITY_CODEC = ByteBufCodecs.map(Object2ObjectOpenHashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.FLOAT);
    AttachmentType<Map<UUID, Float>> PLAYER_VISIBILITY = register("player_visibility", builder ->
            builder.initializer(HashMap::new).syncWith(PLAYER_VISIBILITY_CODEC, AttachmentSyncPredicate.all()));

    AttachmentType<Float> NOTICE = register("notice", builder -> builder
            .initializer(() -> 0F).persistent(Codec.FLOAT).syncWith(ByteBufCodecs.FLOAT, AttachmentSyncPredicate.all()));
    AttachmentType<Integer> TARGET_TIME = register("target_time", builder -> builder
            .initializer(() -> 0).persistent(Codec.INT));

    static void init() {}

    static <T> AttachmentType<T> register(String name, Consumer<AttachmentRegistry.Builder<T>> builder) {
        return AttachmentRegistry.create(StealthCraft.id(name), builder);
    }
}
