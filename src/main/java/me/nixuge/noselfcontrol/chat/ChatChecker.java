package me.nixuge.noselfcontrol.chat;

import java.util.regex.Pattern;

import me.nixuge.noselfcontrol.McMod;
import me.nixuge.noselfcontrol.config.ConfigCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class ChatChecker {
    private static ConfigCache config = McMod.getInstance().getConfigCache();
    private static Minecraft mc = Minecraft.getMinecraft();

    /**
     * Processes the wordlist & regex matches
     * 
     * @param msg message to check
     * @return message valid or not
     */
    public static boolean isMessageValid(String msg) {
        msg = msg.toLowerCase();
        // Normal words
        for (String word : config.getSlurWords()) {
            if (msg.contains(word.toLowerCase()))
                return false;
        }
        // Regexes
        for (Pattern pattern : config.getSlurPatterns()) {
            if (pattern.matcher(msg).find()) {
                return false;
            }
        }
        // No match = safe
        return true;
    }

    /**
     * Figures out if the check must be done, and if yes call checkRaw()
     * Otherwise return true
     * 
     * @param msg message to check
     * @return message valid or not
     */
    public static boolean validateMessage(String msg) {
        // Command check
        if (msg.startsWith("/"))
            return true;

        // Global checks
        if (config.isEnabledAllServers() && !mc.isIntegratedServerRunning()) {
            return isMessageValid(msg);
        } else if (config.isEnabledSolo() && mc.isIntegratedServerRunning()) {
            return isMessageValid(msg);
        }

        // Server specific checks
        if (config.isEnabledServerList()) {
            ServerData sd = mc.getCurrentServerData();
            if (sd == null)
                return true; // Should only happen in solo and not be needed, but just in case
            String ip = sd.serverIP;
            for (String server : config.getServerList()) {
                if (ip.contains(server))
                    return isMessageValid(msg);
            }
        }

        // Otherwise if nothing enabled/no servers matched true
        return true;
    }
}
