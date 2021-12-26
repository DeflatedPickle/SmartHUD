package com.deflatedpickle.smarthud.mixins;

import com.deflatedpickle.smarthud.SmartHUDReheated;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("UnusedMixin")
@Mixin(InGameHud.class)
abstract public class MixinInGameHud {
    @Inject(
            method = "renderHotbar",
            at =
            @At(
                    value = "INVOKE",
                    shift = At.Shift.AFTER,
                    ordinal = 0,
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"
            )
    )
    public void drawSmartHUDBackground(float delta, MatrixStack stack, CallbackInfo info) {
        SmartHUDReheated.INSTANCE.drawSmartHUDBackground((InGameHud) (Object) this, stack);
    }

    @Inject(
            method = "renderHotbar",
            at =
            @At(
                    value = "INVOKE",
                    shift = At.Shift.BEFORE,
                    ordinal = 0,
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"
            )
            ,
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    public void drawSmartHUDItems(
            float partialTicks,
            MatrixStack matrixStack,
            CallbackInfo info,
            PlayerEntity playerEntity,
            ItemStack itemStack,
            Arm arm,
            int i,
            int j,
            int k,
            int l,
            int seed,
            int index,
            int x,
            int y
    ) {
        SmartHUDReheated.INSTANCE.drawSmartHUDItems((InGameHud) (Object) this, y, playerEntity, partialTicks, seed);
    }
}
