package dev.powercyphe.stealthcraft.common.util;

import dev.powercyphe.stealthcraft.common.init.SCAttachmentTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public interface SCUtil {

    static int parseColor(String color) {
        return Integer.parseInt(color.substring(1), 16);
    }

    static List<Triangle> getConeTriangles(float range, float radius) {
        List<Triangle> triangles = new ArrayList<>();

        Vec3 tip = Vec3.ZERO;
        Vec3 dir = new Vec3(0, 0, -range);

        Vec3 end = tip.add(dir);

        List<Vec3> circle = new ArrayList<>();
        int points = 10;
        for (int i = 0; i < points; i++) {
            float m = (float) i / (float) points;
            Vec3 rot = new Vec3(Math.cos(m * Math.PI * 2), Math.sin(m * Math.PI * 2), 0).scale(radius);

            circle.add(end.add(rot));
        }

        Vec3 last = circle.getLast();
        for (Vec3 current : circle) {
            triangles.add(new Triangle(tip, last, current));
            triangles.add(new Triangle(end, last, current));
            last = current;
        }
        return triangles;
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
