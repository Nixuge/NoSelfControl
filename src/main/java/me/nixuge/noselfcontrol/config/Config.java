package me.nixuge.noselfcontrol.config;

import lombok.Getter;
import me.nixuge.noselfcontrol.McMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Getter
public class Config {
    private final Configuration configuration;

    private static String[] defaultEnabledServers = new String[]{"hypixel.net"};
    private static String[] defaultBlacklist = new String[]{
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
    private String[] slurs;
    private boolean showMessageInChat;
    private boolean addBypassClick;

    public Config(final Configuration configuration) {
        this.configuration = configuration;
        this.loadConfiguration();
    }

    public void loadConfiguration() {
        this.enabledAllServers = this.configuration.getBoolean(
                "Enable on all servers",
                "Server",
                true,
                "If the mod should be enabled on all servers, regardless of the \"Enabled servers\" option."
        );
        this.enabledServers = this.configuration.getStringList(
                "Enabled servers",
                "Server",
                defaultEnabledServers,
                "Servers where the mod takes effect. No usage if \"Enable on all servers\" is on."
        );
        this.slurs = this.configuration.getStringList(
                "Words blacklist",
                "Words",
                defaultBlacklist,
                "Blacklisted wordlist."
        );
        this.showMessageInChat = this.configuration.getBoolean(
                "Show message in chat",
                "General",
                true,
                "If a message should be shown saying your message wasn't sent."
        );
        // NON WORKING FOR NOW
        this.addBypassClick = this.configuration.getBoolean(
                "Enable on all servers",
                "General",
                false,
                "If you want a clickable message that lets you send the message anyways."
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