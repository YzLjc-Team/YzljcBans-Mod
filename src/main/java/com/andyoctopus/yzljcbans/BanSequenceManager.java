package com.andyoctopus.yzljcbans;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

/**
 * Handles the timed execution flow once the /gb command has been issued.
 * Responsible for sending the /limbo command, disconnecting, and presenting the fake ban screen.
 */
public class BanSequenceManager {
    private static final long POST_LIMBO_DISCONNECT_DELAY_MS = 3000L;

    private ActiveBanSequence activeSequence;

    public void startSequence(List<String> messageLines, long limboDelayMillis) {
        long now = System.currentTimeMillis();
        this.activeSequence = new ActiveBanSequence(messageLines, now + limboDelayMillis);
        notifyPlayer(EnumChatFormatting.GREEN + "[YzljcBans] Scheduled Limbo in " + formatMillis(limboDelayMillis) + ".");
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (activeSequence == null) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) {
            // Player may have disconnected manually; clean up to avoid stale tasks.
            activeSequence = null;
            return;
        }

        long now = System.currentTimeMillis();

        if (!activeSequence.limboCommandSent && now >= activeSequence.limboTriggerMillis) {
            sendLimboCommand(mc.thePlayer);
            activeSequence.limboCommandSent = true;
            activeSequence.disconnectTriggerMillis = now + POST_LIMBO_DISCONNECT_DELAY_MS;
            return;
        }

        if (activeSequence.limboCommandSent
                && activeSequence.disconnectTriggerMillis > 0
                && now >= activeSequence.disconnectTriggerMillis) {
            showBanScreen(activeSequence);
            activeSequence = null;
        }
    }

    private void sendLimboCommand(EntityPlayerSP player) {
        player.sendChatMessage("/limbo");
        player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "An exception occurred in your connection, so you have been routed to limbo!"));
//        player.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "[YzljcBans] Sending /limbo..."));
    }

    private void showBanScreen(ActiveBanSequence sequence) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld != null) {
            mc.theWorld.sendQuittingDisconnectingPacket();
        }
        mc.loadWorld(null);

        IChatComponent messageComponent = new ChatComponentText(String.join("\n", sequence.messageLines));
        GuiDisconnected banScreen = new GuiDisconnected(
                new GuiMultiplayer(new GuiMainMenu()),
                "Connection Lost",
                messageComponent
        );
        mc.displayGuiScreen(banScreen);
    }

    private void notifyPlayer(String text) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            player.addChatMessage(new ChatComponentText(text));
        }
    }

    private String formatMillis(long millis) {
        long seconds = millis / 1000;
        long days = seconds / 86400;
        seconds %= 86400;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder builder = new StringBuilder();
        if (days > 0) {
            builder.append(days).append("d");
        }
        if (hours > 0) {
            if (builder.length() > 0) builder.append(" ");
            builder.append(hours).append("h");
        }
        if (minutes > 0) {
            if (builder.length() > 0) builder.append(" ");
            builder.append(minutes).append("m");
        }
        if (seconds > 0 || builder.length() == 0) {
            if (builder.length() > 0) builder.append(" ");
            builder.append(seconds).append("s");
        }
        return builder.toString();
    }

    private static final class ActiveBanSequence {
        private final List<String> messageLines;
        private final long limboTriggerMillis;

        private boolean limboCommandSent;
        private long disconnectTriggerMillis = -1L;

        private ActiveBanSequence(List<String> messageLines, long limboTriggerMillis) {
            this.messageLines = messageLines;
            this.limboTriggerMillis = limboTriggerMillis;
            this.limboCommandSent = false;
        }
    }
}

