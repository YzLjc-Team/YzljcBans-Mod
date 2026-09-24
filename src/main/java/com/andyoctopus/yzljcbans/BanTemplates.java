package com.andyoctopus.yzljcbans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

final class BanTemplates {
    private static final Map<String, BanTemplate> TEMPLATES = new LinkedHashMap<>();
    private static final Map<String, CustomTemplate> CUSTOM_TEMPLATES = new LinkedHashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static boolean customTemplatesLoaded;
    private static IOException loadError;

    static {
        register("cheating", "Cheating", Arrays.asList(
                "&cYou are temporarily banned for &f%DURATION% &cfrom this server!",
                "&7",
                "&7Reason: &r&fCheating through the use of unfair game advantages.",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("cheating_backforward", "Cheating_Backforward", Arrays.asList(
                "&cYou are permanently banned from this server!",
                "&7",
                "&7Reason: &r&fCheating through the use of unfair game advantages.",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("chatban", "Chatban", Arrays.asList(
                "&cYou are temporarily banned for &f%DURATION% &cfrom this server!",
                "&7",
                "&7Reason: &r&fExtreme Chat Infraction",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("badname", "Badname", Arrays.asList(
                "&cYou are currently blocked from joining this server!",
                "&7",
                "&7Reason: &r&fYour username, %REASON%, is not allowed on the server and is breaking our rules.",
                "&cFind out more: &bhttps://www.hypixel.net/rules",
                "&7",
                "&cPlease change your Minecraft username before trying to join again.",
                "&cIf you believe your name has been falsely blocked, contact &bhttps://www.hypixel.net/appeal"
        ));

        register("wipeban", "Wipeban", Arrays.asList(
                "&cYou are temporarily banned for &f%DURATION% &cfrom this server!",
                "&7",
                "&7Reason: &r&fBoosting detected on one or multiple SkyBlock profiles.",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("bug", "Bug", Arrays.asList(
                "&cYou are temporarily banned for &f%DURATION% &cfrom this server!",
                "&7",
                "&7Reason: &r&fExploiting a bug or issue within the server and using it to your advantage.",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("security", "Security", Arrays.asList(
                "&cYour account has been blocked.",
                "&7",
                "&7Reason: &r&fSuspicious activity has been detected on your account.",
                "&7Find out more: &b&nhttps://www.hypixel.net/security-block",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("security_old", "Security_old", Arrays.asList(
                "&cYou are permanently banned from this server!",
                "&7",
                "&7Reason: &r&fYour account has a security alert, please secure it and contact appeals.",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("security_accept", "Security_accept", Arrays.asList(
                "&cYour account is temporarily blocked for &f%DURATION% &cfrom this server!",
                "&7",
                "&7Reason: &r&fYour accounts security appeal was processed and the account has entered a recovery phase and will be able to access the server again afterwards.",
                "&7Find out more: &b&nhttps://www.hypixel.net/security-block",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("badbuilding", "Badbuilding", Arrays.asList(
                "&cYou are temporarily banned for &f%DURATION% &cfrom this server!",
                "&7",
                "&7Reason: &r&fCreating a build or drawing which is not appropriate on the server.",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("watchdogban", "Watchdogban", Arrays.asList(
                "&cYou are temporarily banned for &f%DURATION% &cfrom this server!",
                "&7",
                "&7Reason: &r&fWATCHDOG CHEAT DETECTION &7&o[AB-328572]",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("negative_behaviour", "Negative_Behaviour", Arrays.asList(
                "&cYou are temporarily banned for &f%DURATION% &cfrom this server!",
                "&7",
                "&7Reason: &r&fExtreme Negative Behaviour[F]",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("chargeback", "Chargeback", Arrays.asList(
                "&cYou are permanently banned from this server!",
                "&7",
                "&7Reason: &r&fChargeback: for more info and appeal, pleace visit support.hypixel.net.",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("cross_teaming", "Cross_teaming", Arrays.asList(
                "&cYou are temporarily banned for &f%DURATION% &cfrom this server!",
                "&7",
                "&7Reason: &r&fCross teaming with an enemy team or player.",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("ytban", "YtBan", Arrays.asList(
                "&cYou are permanently banned from this server!",
                "&7",
                "&7Reason: &r&fPlease contact creators@hypixel.net for assistance.",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("fraud", "Fraud", Arrays.asList(
                "&cYou are permanently banned from this server!",
                "&7",
                "&7Reason: &r&fFraud: Please visit support.hypixel.net for clarifications or questions.",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("badskin", "BadSkin", Arrays.asList(
                "&cYou are temporarily banned for &f%DURATION% &cfrom this server!",
                "&7",
                "&7Reason: &r&fUsing inappropriate skins or capes on the server.",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("boosting", "Boosting", Arrays.asList(
                "&cYou are temporarily banned for &f%DURATION% &cfrom this server!",
                "&7",
                "&7Reason: &r&fBoosting your account to improve your stats.",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("acc_del", "acc_del", Arrays.asList(
                "&cYou are permanently banned from this server!",
                "&7",
                "&7Reason: &r&fUpon request, data for this user has been deleted.",
                "&7Find out more: &b&nhttps://support.hypixel.net",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

        register("sabotaging", "sabotaging", Arrays.asList(
                "&cYou are temporarily banned for &f%DURATION% &cfrom this server!",
                "&7",
                "&7Reason: &r&fSabotaging the gameplay of other players on your team.",
                "&7Find out more: &b&nhttps://www.hypixel.net/appeal",
                "&7",
                "&7Ban ID: &f#%banid%",
                "&7Sharing your Ban ID may affect the processing of your appeal!"
        ));

    }

    private static void register(String key, String name, List<String> lines) {
        TEMPLATES.put(key.toLowerCase(Locale.ROOT), new BanTemplate(name, lines));
    }

    static BanTemplate get(String key) {
        loadCustomTemplates();
        String normalizedKey = key.toLowerCase(Locale.ROOT);
        CustomTemplate custom = CUSTOM_TEMPLATES.get(normalizedKey);
        return custom == null ? TEMPLATES.get(normalizedKey) : new BanTemplate(custom.name, custom.lines);
    }

    static List<String> getKeys() {
        loadCustomTemplates();
        List<String> keys = new ArrayList<>(TEMPLATES.keySet());
        keys.addAll(CUSTOM_TEMPLATES.keySet());
        Collections.sort(keys);
        return keys;
    }

    static boolean isCustom(String key) {
        loadCustomTemplates();
        return CUSTOM_TEMPLATES.containsKey(key.toLowerCase(Locale.ROOT));
    }

    static String getLoadError() {
        loadCustomTemplates();
        return loadError == null ? null : loadError.getMessage();
    }

    static void addCustom(String key, String name, List<String> lines) throws IOException {
        loadCustomTemplates();
        if (loadError != null) {
            throw new IOException("Cannot read custom templates: " + loadError.getMessage(), loadError);
        }

        String normalizedKey = key.trim().toLowerCase(Locale.ROOT);
        String normalizedName = name.trim();
        if (!normalizedKey.matches("[a-z0-9_]{1,32}")) {
            throw new IllegalArgumentException("Key must be 1-32 letters, numbers or underscores.");
        }
        if (TEMPLATES.containsKey(normalizedKey) || CUSTOM_TEMPLATES.containsKey(normalizedKey)) {
            throw new IllegalArgumentException("A template with this key already exists.");
        }
        if (normalizedName.isEmpty() || normalizedName.length() > 40) {
            throw new IllegalArgumentException("Name must be 1-40 characters.");
        }
        if (lines.isEmpty() || lines.size() > 30) {
            throw new IllegalArgumentException("Add between 1 and 30 message lines.");
        }
        List<String> copy = new ArrayList<>(lines);
        boolean hasText = false;
        for (String line : copy) {
            if (line == null || line.length() > 256) {
                throw new IllegalArgumentException("Each message line must be at most 256 characters.");
            }
            hasText |= !line.trim().isEmpty();
        }
        if (!hasText) {
            throw new IllegalArgumentException("Enter at least one non-empty message line.");
        }

        CustomTemplate template = new CustomTemplate(normalizedKey, normalizedName, copy);
        List<CustomTemplate> updated = new ArrayList<>(CUSTOM_TEMPLATES.values());
        updated.add(template);
        File file = customFile();
        Files.createDirectories(file.getParentFile().toPath());
        Path temporaryFile = file.toPath().resolveSibling(file.getName() + ".tmp");
        try (Writer writer = Files.newBufferedWriter(temporaryFile, StandardCharsets.UTF_8)) {
            GSON.toJson(updated, writer);
        }
        Files.move(temporaryFile, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        CUSTOM_TEMPLATES.put(normalizedKey, template);
    }

    private static void loadCustomTemplates() {
        if (customTemplatesLoaded) {
            return;
        }
        customTemplatesLoaded = true;
        File file = customFile();
        if (!file.isFile()) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            CustomTemplate[] templates = GSON.fromJson(reader, CustomTemplate[].class);
            if (templates == null) {
                throw new IOException("Custom template file is empty.");
            }
            for (CustomTemplate template : templates) {
                if (template == null || template.key == null || template.name == null || template.lines == null
                        || !template.key.matches("[a-z0-9_]{1,32}") || template.name.trim().isEmpty()
                        || template.name.length() > 40 || template.lines.isEmpty() || template.lines.size() > 30
                        || TEMPLATES.containsKey(template.key) || CUSTOM_TEMPLATES.containsKey(template.key)) {
                    throw new IOException("Custom template file contains an invalid entry.");
                }
                for (String line : template.lines) {
                    if (line == null || line.length() > 256) {
                        throw new IOException("Custom template file contains an invalid message line.");
                    }
                }
                CUSTOM_TEMPLATES.put(template.key, template);
            }
        } catch (Exception error) {
            CUSTOM_TEMPLATES.clear();
            loadError = error instanceof IOException ? (IOException) error : new IOException(error);
        }
    }

    private static File customFile() {
        return new File(Minecraft.getMinecraft().mcDataDir, "config/yzljcbans/custom_bans.json");
    }

    private static final class CustomTemplate {
        private final String key;
        private final String name;
        private final List<String> lines;

        private CustomTemplate(String key, String name, List<String> lines) {
            this.key = key;
            this.name = name;
            this.lines = lines;
        }
    }

    private BanTemplates() {
    }
}

