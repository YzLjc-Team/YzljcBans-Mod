package com.andyoctopus.yzljcbans;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Implements the /gb (alias /getban) client-side command that simulates multiple ban templates.
 */
public class GetBanCommand extends CommandBase {
    private static final String COMMAND_NAME = "getban";
    private static final List<String> ALIASES = Collections.singletonList("gb");
    private static final Random RANDOM = new SecureRandom();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final BanSequenceManager sequenceManager;

    public GetBanCommand(BanSequenceManager sequenceManager) {
        this.sequenceManager = sequenceManager;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public List<String> getCommandAliases() {
        return ALIASES;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gb config | /gb <template> <duration|permanent> <limboDelay> [Reason/PlayerName]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            throw new CommandException("You must be in-game to use this command.");
        }

        if (args.length == 1 && "config".equalsIgnoreCase(args[0])) {
            sequenceManager.openSettings();
            return;
        }

        if (args.length < 3) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        String templateKey = args[0].toLowerCase(Locale.ROOT);
        BanTemplate template = BanTemplates.get(templateKey);
        if (template == null) {
            throw new CommandException("Unsupported ban template: " + templateKey);
        }

        String durationArg = args[1];
        Long parsedDuration = null;
        if (template.usesDurationPlaceholder()) {
            parsedDuration = requireDuration(durationArg);
        } else {
            parsedDuration = parseOptionalDuration(durationArg);
        }

        long limboDelayMillis = requireDuration(args[2]);
        if (limboDelayMillis < 0L) {
            throw new CommandException("Limbo delay must be positive.");
        }

        String reasonText = collectReason(args);
        String banId = generateBanId();
        String numericId = generateNumericId();
        String dateText = DATE_FORMAT.format(System.currentTimeMillis());

        if (template.usesReasonPlaceholder() && reasonText.isEmpty()) {
            reasonText = banId;
        }

        String displayDuration = template.usesDurationPlaceholder()
                ? formatBanDuration(parsedDuration)
                : durationArg;

        List<String> messageLines = template.render(displayDuration, reasonText, banId, numericId, dateText);
        sequenceManager.startSequence(messageLines, limboDelayMillis);
        player.addChatMessage(buildFeedback(template.getName(), displayDuration, template.usesDurationPlaceholder(), limboDelayMillis));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            List<String> keys = new java.util.ArrayList<>(BanTemplates.getKeys());
            keys.add("config");
            return getListOfStringsMatchingLastWord(args, keys);
        }
        return Collections.emptyList();
    }

    private ChatComponentText buildFeedback(String templateName, String durationText, boolean usesDuration, long limboDelayMillis) {
        StringBuilder builder = new StringBuilder();
        builder.append(EnumChatFormatting.AQUA).append("[YzljcBans] ")
                .append(EnumChatFormatting.GRAY).append("Template: ")
                .append(EnumChatFormatting.WHITE).append(templateName)
                .append(EnumChatFormatting.GRAY).append(". ");
        if (usesDuration) {
            builder.append(EnumChatFormatting.GRAY).append("Simulating ban for ")
                    .append(EnumChatFormatting.WHITE).append(durationText)
                    .append(EnumChatFormatting.GRAY).append(". ");
        }
        builder.append(EnumChatFormatting.GRAY).append("Limbo in ")
                .append(EnumChatFormatting.WHITE).append(formatMillis(limboDelayMillis))
                .append(EnumChatFormatting.GRAY).append(", showing fake ban screen 3s later.");
        return new ChatComponentText(builder.toString());
    }

    private long requireDuration(String input) throws CommandException {
        Long value = parseOptionalDuration(input);
        if (value == null) {
            throw new CommandException("Invalid duration format: " + input);
        }
        return value;
    }

    private Long parseOptionalDuration(String input) throws CommandException {
        if ("permanent".equalsIgnoreCase(input) || "perm".equalsIgnoreCase(input) || "forever".equalsIgnoreCase(input)) {
            return Long.MAX_VALUE;
        }

        long totalMillis = 0L;
        long currentNumber = 0L;
        boolean foundUnit = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                currentNumber = currentNumber * 10 + (c - '0');
            } else {
                long multiplier = getUnitMultiplier(c);
                totalMillis += currentNumber * multiplier;
                currentNumber = 0L;
                foundUnit = true;
            }
        }

        if (!foundUnit) {
            return null;
        }

        if (currentNumber != 0L) {
            throw new CommandException("Duration must end with a unit: " + input);
        }

        return totalMillis;
    }

    private long getUnitMultiplier(char unitChar) throws CommandException {
        switch (Character.toLowerCase(unitChar)) {
            case 'd':
                return 86_400_000L;
            case 'h':
                return 3_600_000L;
            case 'm':
                return 60_000L;
            case 's':
                return 1_000L;
            default:
                throw new CommandException("Unsupported duration unit: " + unitChar);
        }
    }

    private String formatMillis(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds %= 60;

        long hours = minutes / 60;
        minutes %= 60;

        long days = hours / 24;
        hours %= 24;

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

    private String generateBanId() {
        final char[] charset = "ABCDEF123456789".toCharArray();
        StringBuilder builder = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            builder.append(charset[RANDOM.nextInt(charset.length)]);
        }
        return builder.toString();
    }

    private String generateNumericId() {
        int value = 100000 + RANDOM.nextInt(900000);
        return Integer.toString(value);
    }

    private String formatBanDuration(long durationMillis) {
        if (durationMillis == Long.MAX_VALUE) {
            return "permanent";
        }
        long adjusted = Math.max(0L, durationMillis - 2000L);
        return formatMillis(adjusted);
    }

    private String collectReason(String[] args) {
        if (args.length <= 3) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(args[i]);
        }
        return builder.toString();
    }
}

