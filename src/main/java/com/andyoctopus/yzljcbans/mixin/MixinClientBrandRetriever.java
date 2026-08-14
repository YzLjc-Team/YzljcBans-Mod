package com.andyoctopus.yzljcbans.mixin;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(NetworkManager.class)
public class MixinClientBrandRetriever {

    @Inject(method = "channelRead0", at = @At("HEAD"))
    private void onChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        // 入站包，不管
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof C17PacketCustomPayload) {
            C17PacketCustomPayload payload = (C17PacketCustomPayload) packet;
            if ("MC|Brand".equals(payload.getChannelName())) {
                try {
                    // 读取原有的 buffer (为了清空引用，防止内存泄漏，虽然这里只是读取)
                    PacketBuffer buffer = payload.getBufferData();

                    // 这里比较暴力：利用反射修改包的内容，或者直接替换包
                    // 最简单的方法：取消发送原包，自己发一个新的（但这会导致递归，要小心）
                    // 更好的方法：修改 payload 对象内部的 data 字段

                    // 重新写入你的品牌
                    io.netty.buffer.ByteBuf newBuf = io.netty.buffer.Unpooled.buffer();
                    PacketBuffer newPacketBuffer = new PacketBuffer(newBuf);
                    newPacketBuffer.writeString("What_are_you_looking_at?");

                    // 使用反射将 payload 中的 data 字段替换为 newPacketBuffer
                    // 注意：这里需要处理混淆名，C17PacketCustomPayload 的 data 字段
                    java.lang.reflect.Field dataField = C17PacketCustomPayload.class.getDeclaredField("field_149561_c"); // MCP名: data
                    dataField.setAccessible(true);
                    dataField.set(payload, newPacketBuffer);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}