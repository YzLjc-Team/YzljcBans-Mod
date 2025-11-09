package com.andyoctopus.yzljcbans;

import java.util.*;

final class BanTemplates {
    private static final Map<String, BanTemplate> TEMPLATES = new HashMap<>();

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
                "&7Ban ID: &f#%REASON%",
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

    }

    private static void register(String key, String name, List<String> lines) {
        TEMPLATES.put(key.toLowerCase(Locale.ROOT), new BanTemplate(name, lines));
    }

    static BanTemplate get(String key) {
        return TEMPLATES.get(key.toLowerCase(Locale.ROOT));
    }

    static List<String> getKeys() {
        List<String> keys = new ArrayList<>(TEMPLATES.keySet());
        Collections.sort(keys);
        return keys;
    }

    private BanTemplates() {
    }
}

