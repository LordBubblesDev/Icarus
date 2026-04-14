package dev.cammiescorner.icarus.mixin;

import dev.cammiescorner.icarus.util.IcarusHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import dev.cammiescorner.icarus.item.WingItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityFallFlyingMixin {
    @Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
    private void icarus$stopTrinketWingFlightOnShift(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.isFallFlying()) {
            return;
        }

        if (!IcarusHelper.hasWings(entity)) {
            return;
        }

        // If wings are not in chest slot (e.g. Trinkets cape slot), handle SHIFT stop ourselves.
        if (!(entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof WingItem) && entity.isShiftKeyDown()) {
            entity.stopFallFlying();
            ci.cancel();
        }
    }
}
