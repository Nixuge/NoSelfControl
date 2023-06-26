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

    private boolean enabledAllServers;
    private String[] enabledServers;
    private String[] slursWords;
    private String[] slursRegex;
    private boolean showMessageInChat;
    private boolean bypassClick;
    private boolean enabledSolo;

    public ConfigCache(final Configuration configuration) {
        this.configuration = configuration;
        this.loadConfiguration();
        this.configuration.save();
    }

    public void loadConfiguration() {
        this.enabledAllServers = this.configuration.getBoolean(
                "Enable on all servers",
                "General",
                true,
                "If the mod should be enabled on all servers, regardless of the \"Enabled servers\" option."
        );
        this.enabledServers = this.configuration.getStringList(
                "Enabled servers",
                "General",
                defaultEnabledServers,
                "Servers where the mod takes effect. No usage if \"Enable on all servers\" is on."
        );
        this.slursWords = this.configuration.getStringList(
                "Words blacklist (normal)",
                "General",
                defaultBlacklistWords,
                "Simple words blacklist. Messages will be checked to see if they contain in those words."
        );
        // NOT FUNCTIONAL YET
        this.slursRegex = this.configuration.getStringList(
                "Words blacklist (regex)",
                "General",
                new String[]{},
                "Regex blacklist. Messages will be matched with those regexes, and prevented if matching."
        );
        this.showMessageInChat = this.configuration.getBoolean(
                "Show preventing message in chat",
                "General",
                true,
                "If a message should be shown saying your message wasn't sent."
        );
        // NOT WORKING FOR NOW
        this.bypassClick = this.configuration.getBoolean(
                "Bypass button on preventing message",
                "General",
                false,
                "If you want a clickable message that lets you send the message anyways."
        );
        this.enabledSolo = this.configuration.getBoolean(
                "Enabled in solo",
                "General",
                false,
                "If you want the mod to be active in solo worlds."
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