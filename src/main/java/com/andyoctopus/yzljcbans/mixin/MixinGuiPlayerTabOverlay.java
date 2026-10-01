package com.andyoctopus.yzljcbans.mixin;

import com.andyoctopus.yzljcbans.DevTag;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GuiPlayerTabOverlay.class, priority = 100)
public abstract class MixinGuiPlayerTabOverlay {
    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void yzljcbans$prefixDevTabName(NetworkPlayerInfo playerInfo, CallbackInfoReturnable<String> callback) {
        if (DevTag.isDeveloper(playerInfo.getGameProfile().getName())) {
            callback.setReturnValue(DevTag.prefixRenderedName(callback.getReturnValue()));
        }
    }
}
