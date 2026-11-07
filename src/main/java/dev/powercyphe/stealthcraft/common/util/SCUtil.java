package dev.powercyphe.stealthcraft.common.util;

import dev.powercyphe.stealthcraft.common.init.SCAttachmentTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.*;

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

        int cones = Math.clamp(Mth.ceil(Math.max(1, range / 10) * radius), 2, 64);
        int points = 24;

        for (int cone = 0; cone < cones; cone++) {
            Vec3[] ring = new Vec3[points];

            for (int p = 0; p < points; p++) {
                float m = (float) p / (float) points;
                Vec3 offset = new Vec3(Math.cos(m * Math.PI * 2), Math.sin(m * Math.PI * 2), 0)
                        .xRot(Mth.DEG_TO_RAD * -dir.x)
                        .yRot(Mth.DEG_TO_RAD * -dir.y)
                        .scale(radius * (double) cone / cones)
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
        var players = level.getPlayers(p -> true);

        for (ServerPlayer player : players) {
            vis.put(player.getUUID(), (float) player.getVisibilityPercent(level, entity));
        }

        entity.setAttached(SCAttachmentTypes.PLAYER_VISIBILITY, vis);
    }
}
