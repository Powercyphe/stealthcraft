package dev.powercyphe.stealthcraft.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import dev.powercyphe.stealthcraft.common.SCConfig;
import dev.powercyphe.stealthcraft.common.init.SCAttachmentTypes;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

import static dev.powercyphe.stealthcraft.common.StealthCraft.LOGGER;

public interface SCUtil {

    static int parseColor(String color) {
        return Integer.parseInt(color.substring(1), 16);
    }

    static List<Quad> getConeQuads(Entity entity, Vec3 eyePos, Vec2 dir, float range, float radius) {
        Level level = entity.level();

        List<Quad> quads = new ArrayList<>();
        List<Vec3[]> rings = new ArrayList<>();

        Vec3 base = eyePos.subtract(entity.position());
        Vec3 end = eyePos.add(Vec3.directionFromRotation(dir).scale(range));

        int cones = Mth.lerpInt(SCConfig.coneComplexity, 4, 8) * (int) Math.clamp(Math.max(1, range / 8) * (radius / (Math.PI * 3)), 2, 4);
        int points = Mth.lerpInt(SCConfig.coneComplexity, 16, 32);

        for (int cone = 0; cone <= cones; cone++) {
            Vec3[] ring = new Vec3[points];

            for (int p = 0; p < points; p++) {
                float m = (float) p / (float) points;
                Vec3 offset = new Vec3(Math.cos(m * Math.PI * 2), Math.sin(m * Math.PI * 2), 0)
                        .xRot(Mth.DEG_TO_RAD * -dir.x)
                        .yRot(Mth.DEG_TO_RAD * -dir.y)
                        .scale(radius * ((double) cone / (double) cones))
                        ;

                Vec3 cPos = ray(level, eyePos, eyePos.vectorTo(end.add(offset)), range);
                ring[p] = base.add(eyePos.vectorTo(cPos)
                        .yRot(Mth.DEG_TO_RAD * dir.y)
                        .xRot(Mth.DEG_TO_RAD * -dir.x))
                        ;
            }
            rings.add(ring);
        }

        for (int c = 1; c < cones; c++) {
            Vec3[] lr = rings.get(c - 1);
            Vec3[] r = rings.get(c);

            int lp = points - 1;
            for (int p = 0; p < points; p++) {
                quads.add(new Quad(lr[lp], lr[p], r[p], r[lp]));
                if (c == cones - 1) {
                    quads.add(new Quad(base, base, r[lp], r[p]));
                }

                lp = p;
            }

        }
        return quads;
    }

    static Vec3 ray(Level level, Vec3 from, Vec3 dir, float range) {
        Vec3 to = from.add(dir.normalize().scale(range));
        Vec3 clip = level.clip(new ClipContext(
                from, to,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
                CollisionContext.empty()
        )).getLocation();
        Vec3 rayD = from.vectorTo(clip);
        return clip.subtract(rayD.length() > 1 ? rayD.normalize().scale(-0.01) : rayD.scale(-0.01));
    }

    static boolean insideCone(Vec3 toCheck, Vec3 tip, Vec3 dir, float range, float radius) {
        toCheck =  toCheck.subtract(tip);

        double dot = toCheck.dot(dir);
        if (dot < 0 || dot > range) {
            return false;
        }

        Vec3 perp = toCheck.subtract(dir.scale(dot));
        double coneL = radius * (dot / range);

        return perp.length() <= coneL;
    }

    static float getPlayerVisibility(Entity entity, LivingEntity target) {
        return entity.getAttachedOrCreate(SCAttachmentTypes.PLAYER_VISIBILITY)
                .getOrDefault(target.getUUID(), 1F);
    }

    static void updatePlayerVisibility(ServerLevel level, LivingEntity entity) {
        Map<UUID, Float> vis = new HashMap<>();
        var players = level.getPlayers(_ -> true);

        for (ServerPlayer player : players) {
            vis.put(player.getUUID(), (float) player.getVisibilityPercent(level, entity));
        }

        entity.setAttached(SCAttachmentTypes.PLAYER_VISIBILITY, vis);
    }

    public static <T> void scanDirectory(
            final ResourceManager manager, final FileToIdConverter lister, final DynamicOps<JsonElement> ops, final Codec<T> codec, final Map<Identifier, T> result
    ) {
        for (Map.Entry<Identifier, Resource> entry : lister.listMatchingResources(manager).entrySet()) {
            Identifier location = entry.getKey();
            Identifier id = lister.fileToId(location);

            try {
                Reader reader = entry.getValue().openAsReader();

                try {
                    codec.parse(ops, StrictJsonParser.parse(reader)).ifSuccess(parsed -> {
                        if (result.putIfAbsent(id, parsed) != null) {
                            throw new IllegalStateException("Duplicate data file ignored with ID " + id);
                        }
                    }).ifError(error -> LOGGER.error("Couldn't parse data file '{}' from '{}': {}", new Object[]{id, location, error}));
                } catch (Throwable var13) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var12) {
                            var13.addSuppressed(var12);
                        }
                    }

                    throw var13;
                }

                if (reader != null) {
                    reader.close();
                }
            } catch (JsonParseException | IllegalArgumentException | IOException e) {
                LOGGER.error("Couldn't parse data file '{}' from '{}'", new Object[]{id, location, e});
            }
        }
    }
}
