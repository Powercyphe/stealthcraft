package dev.powercyphe.stealthcraft.common.entity;

import dev.powercyphe.stealthcraft.common.SCConfig;
import dev.powercyphe.stealthcraft.common.init.SCAttachmentTypes;
import dev.powercyphe.stealthcraft.common.init.SCMemoryModuleTypes;
import dev.powercyphe.stealthcraft.common.util.SCUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class EntityVisionHandler {
    private final LivingEntity entity;
    private final EntityVision vision;

    private static final int NOTICE_VISIBILITY_MAX = 20;

    private float lastNotice = 0F;
    private int noticeVisibility, lastNoticeVisibility = 0;

    public EntityVisionHandler(LivingEntity entity) {
        this.entity = entity;
        this.vision = EntityVisionRegistry.getVisionFor(entity);
    }

    public void tick() {
        this.lastNotice = this.notice();

        Level level = this.entity.level();
        if (level instanceof ServerLevel serverLevel) {
            SCUtil.updatePlayerVisibility(serverLevel, this.entity);

            var players = serverLevel.getPlayers(this::isVisible);
            if (!players.isEmpty()) {
                Player closest = null;
                float dis = 1F;
                for (var p : players) {
                    float pR = p.distanceTo(this.entity) / this.vision.range(this.entity, p);
                    if (closest == null || pR < dis) {
                        closest = p;
                        dis = pR;
                    }
                }

                if (this.notice() < 1F) {
                    float incMax = 1F / SCConfig.minDetectionTicks;
                    float inc = SCConfig.minDetectionTicks <= 0 ? 1F
                            : Math.min(incMax, 1F - Math.min(1F, dis));

                    this.setNotice(Math.clamp(this.notice() + inc, 0F, 1F));
                } else {
                    this.onSeen(serverLevel, closest);
                }
            } else if (this.notice() > 0) {
                if (this.targetTime() > 0) {
                    this.setTargetTime(this.targetTime() - 1);

                    if (this.targetTime() == 0) {
                        this.entity.getBrain().eraseMemory(SCMemoryModuleTypes.CAN_TARGET);
                    }
                    return;
                }
                this.setNotice(Math.max(this.notice() - 0.02F, 0F));
            }
        } else {
            this.lastNoticeVisibility = this.noticeVisibility;
            if (this.notice() > 0 || this.targetTime() > 0) {
                if (this.noticeVisibility < NOTICE_VISIBILITY_MAX) {
                    this.noticeVisibility = Math.max(this.noticeVisibility + 2, NOTICE_VISIBILITY_MAX);
                }
            } else if (this.noticeVisibility > 0) {
                this.noticeVisibility--;
            }
        }
    }

    public void onSeen(ServerLevel level, Player player) {
        switch (SCConfig.seenMode) {
            case KILL -> player.kill(level);
            case DAMAGE -> {
                switch (SCConfig.damageMode) {
                    case ENTITY -> player.hurtServer(level, this.entity.damageSources().mobAttack(this.entity), SCConfig.damageAmount);
                    case FULL_BYPASS -> player.hurtServer(level, this.entity.damageSources().source(DamageTypes.FELL_OUT_OF_WORLD, this.entity), SCConfig.damageAmount);
                }
            }
            case TARGET -> {
                List<Mob> affected = new ArrayList<>();
                switch (SCConfig.targetMode) {
                    case SINGLE -> {
                        if (this.entity instanceof Mob mob) {
                            affected.add(mob);
                        }
                    }
                    case RADIUS -> {
                        AABB radius = AABB.ofSize(this.entity.getEyePosition(), SCConfig.targetRadius, SCConfig.targetRadius, SCConfig.targetRadius);
                        affected.addAll(level.getEntities(EntityTypeTest.forClass(Mob.class), radius, LivingEntity::isAlive));
                    }
                    case ALL -> {
                        affected.addAll(level.getEntities(EntityTypeTest.forClass(Mob.class), LivingEntity::isAlive));
                    }
                }

                int targetTicks = SCConfig.targetTicks;
                for (Mob mob : affected) {
                    mob.setTarget(player);
                    mob.setAttached(SCAttachmentTypes.TARGET_TIME, targetTicks);
                    mob.getBrain().setMemoryWithExpiry(SCMemoryModuleTypes.CAN_TARGET, true, targetTicks);
                }
            }
        }
    }

    public boolean isVisible(ServerPlayer player) {
        return !(player.isCreative() || player.isSpectator()) && this.vision.isVisible(this.entity, player);
    }

    public EntityVision getVision() {
        return this.vision;
    }

    public float getNoticeProgress(float partialTicks) {
        return Mth.lerp(partialTicks, this.lastNotice, this.notice());
    }

    public float getNoticeVisibility(float partialTicks) {
        return Mth.lerp(partialTicks, this.lastNoticeVisibility, this.noticeVisibility) / NOTICE_VISIBILITY_MAX;
    }

    public int targetTime() {
        return this.entity.getAttachedOrCreate(SCAttachmentTypes.TARGET_TIME);
    }

    public void setTargetTime(int targetTime) {
        this.entity.setAttached(SCAttachmentTypes.TARGET_TIME, targetTime);
    }

    public float notice() {
        return this.entity.getAttachedOrCreate(SCAttachmentTypes.NOTICE);
    }

    public void setNotice(float notice) {
        this.entity.setAttached(SCAttachmentTypes.NOTICE, notice);
    }

}
