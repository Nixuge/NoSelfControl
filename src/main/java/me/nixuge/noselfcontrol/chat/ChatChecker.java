package me.nixuge.noselfcontrol.chat;

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
        for (String word : config.getSlursWords()) {
            if (msg.contains(word))
                return false;
        }
        // TODO: REGEX SUPPORT HERE
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
        // Global checks
        if (config.isEnabledAllServers() && !mc.isIntegratedServerRunning()) {
            return isMessageValid(msg);
        } else if (config.isEnabledSolo() && mc.isIntegratedServerRunning()) {
            return isMessageValid(msg);
        }

        // Server specific checks
        ServerData sd = mc.getCurrentServerData();
        if (sd == null)
            return true; // Should only happen in solo and not be needed, but just in case
        String ip = sd.serverIP;
        for (String server : config.getEnabledServers()) {
            if (ip.contains(server))
                return isMessageValid(msg);
        }

        // Otherwise if nothing enabled/no servers matched true
        return true;
    }
}
