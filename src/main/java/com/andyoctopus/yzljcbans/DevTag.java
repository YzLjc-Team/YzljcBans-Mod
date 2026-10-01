package com.andyoctopus.yzljcbans;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** Adds the developer badge to a rendered name without changing its source. */
public final class DevTag {
    private static final Set<String> DEV_USERNAMES = new HashSet<>(Arrays.asList("AndyOctopus", "CuteGirlLjc"));
    private static final String PREFIX = "[DEV] ";

    private DevTag() {
    }

    public static boolean isDeveloper(String username) {
        return DEV_USERNAMES.contains(username);
    }

    public static String prefixRenderedName(String name) {
        if (name == null || hasPrefix(name)) {
            return name;
        }
        return EnumChatFormatting.DARK_PURPLE + PREFIX + EnumChatFormatting.RESET + name;
    }

    public static IChatComponent prefixDisplayName(IChatComponent name) {
        if (name == null || hasPrefix(name.getUnformattedText())) {
            return name;
        }

        ChatComponentText result = new ChatComponentText("");
        ChatComponentText badge = new ChatComponentText(PREFIX);
        badge.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE));
        result.appendSibling(badge);

        IChatComponent original = name.createCopy();
        original.setChatStyle(name.getChatStyle().createDeepCopy());
        result.appendSibling(original);
        return result;
    }

    private static boolean hasPrefix(String name) {
        String plain = EnumChatFormatting.getTextWithoutFormattingCodes(name);
        return plain != null && plain.regionMatches(true, 0, PREFIX, 0, PREFIX.length());
    }
}
