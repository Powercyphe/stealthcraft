package dev.powercyphe.stealthcraft.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.powercyphe.stealthcraft.common.SCConfig;
import dev.powercyphe.stealthcraft.common.entity.EntityVisionHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Definition(id = "totalDamage", local = @Local(type = float.class, name = "totalDamage"))
    @Expression("totalDamage = @(?)")
    @ModifyExpressionValue(method = "attack", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float stealthcraft$backstabbing(float original, Entity entity) {
        if (entity instanceof LivingEntity living && living.getVisionHandler() != null) {
            EntityVisionHandler<?> handler = living.getVisionHandler();

            if (SCConfig.entityShadowStrike && handler.getNoticeProgress() < 1) {
                original = original * SCConfig.entityShadowStrikeMultiplier;

                if (this.level() instanceof ServerLevel serverLevel) {
                    this.doShadowStrike(serverLevel, living);
                }
            }
        }
        return original;
    }

    @Unique
    private void doShadowStrike(ServerLevel level, LivingEntity target) {
        float w = target.getBbWidth() / 3F;
        float h = target.getBbHeight() / 2F;

        level.sendParticles(ParticleTypes.FIREWORK, target.getX(), target.getEyeY(), target.getZ(), 14, w, h, w, 0.2);
    }

}
