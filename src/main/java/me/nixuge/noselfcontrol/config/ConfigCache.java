package me.nixuge.noselfcontrol.config;

import lombok.Getter;
import me.nixuge.noselfcontrol.McMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Getter
public class ConfigCache {
    private final Configuration configuration;

    private static String[] defaultEnabledServers = new String[]{"hypixel.net"};
    private static String[] defaultBlacklistWords = new String[]{
        "hang yourself",
        "kill yourself",
        "hang urself",
        "kill urself",
        "nlgga",
        "nlgger",
        "nigga",
        "nigger"
    };

    private boolean enabledSolo;
    private boolean enabledAllServers;
    private boolean enabledServerList;
    private String[] serverList;
    private String[] slursWords;
    private String[] slursRegex;
    private boolean showMessageInChat;
    private boolean bypassClick;

    public ConfigCache(final Configuration configuration) {
        this.configuration = configuration;
        this.loadConfiguration();
        this.configuration.save();
    }

    public void loadConfiguration() {
        this.enabledAllServers = this.configuration.getBoolean(
                "Enable (all servers)",
                "General",
                true,
                "If the mod should be enabled on all servers, regardless of the \"Enabled servers\" option."
        );
        this.enabledSolo = this.configuration.getBoolean(
                "Enable (local)",
                "General",
                false,
                "If you want the mod to be active in solo worlds."
        );
        this.enabledServerList = this.configuration.getBoolean(
                "Enabled (server list)",
                "General",
                true,
                "If you want the mod to be active in the server list you completed below."
        );
        this.serverList = this.configuration.getStringList(
                "Server List",
                "General",
                defaultEnabledServers,
                "Servers where the mod takes effect. Only useful if \"Enabled (srver list)\" is on."
        );

        this.showMessageInChat = this.configuration.getBoolean(
                "Show a preventing message",
                "General",
                true,
                "If a message should be shown saying your message wasn't sent."
        );
        this.bypassClick = this.configuration.getBoolean(
                "Show a bypass button",
                "General",
                false,
                "If you want a clickable message that lets you send the message anyways. Requires \"Show a preventing message\" to be on."
        );

        this.slursWords = this.configuration.getStringList(
                "Word blacklist (normal)",
                "General",
                defaultBlacklistWords,
                "Simple words blacklist. Messages will be checked to see if they contain in those words."
        );
        // NOT FUNCTIONAL YET
        this.slursRegex = this.configuration.getStringList(
                "Word blacklist (regex)",
                "General",
                new String[]{},
                "Regex blacklist. Messages will be matched with those regexes, and prevented if matching."
        );
    }

    @SubscribeEvent
    public void onConfigurationChangeEvent(final ConfigChangedEvent.OnConfigChangedEvent event) {
        this.configuration.save();
        if (event.modID.equalsIgnoreCase(McMod.MOD_ID)) {
            this.loadConfiguration();
        }
    }
}