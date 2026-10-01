package com.andyoctopus.yzljcbans.mixin;

import com.andyoctopus.yzljcbans.DevTag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityPlayer.class, priority = 100)
public abstract class MixinEntityPlayer {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void yzljcbans$prefixDevDisplayName(CallbackInfoReturnable<IChatComponent> callback) {
        EntityPlayer player = (EntityPlayer) (Object) this;
        if (player.worldObj != null && player.worldObj.isRemote
                && DevTag.isDeveloper(player.getGameProfile().getName())) {
            callback.setReturnValue(DevTag.prefixDisplayName(callback.getReturnValue()));
        }
    }
}
