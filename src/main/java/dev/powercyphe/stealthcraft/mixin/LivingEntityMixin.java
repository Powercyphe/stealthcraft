package dev.powercyphe.stealthcraft.mixin;

import dev.powercyphe.stealthcraft.common.entity.EntityVisionHandler;
import dev.powercyphe.stealthcraft.common.entity.VisionHandlerHolder;
import dev.powercyphe.stealthcraft.common.init.SCTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements VisionHandlerHolder<LivingEntity> {

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Unique
    private EntityVisionHandler<LivingEntity> visionHandler = null;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void stealthcraft$initVisionHandler(EntityType<?> type, Level level, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!level.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE).wrapAsHolder(type).is(SCTags.NO_VISION_HANDLER)) {
            this.visionHandler = new EntityVisionHandler<>(entity);
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void stealthcraft$tickVisionHandler(CallbackInfo ci) {
        if (this.visionHandler != null) {
            this.visionHandler.tick();
        }
    }

    @Override
    public EntityVisionHandler<LivingEntity> getVisionHandler() {
        return this.visionHandler;
    }
}
