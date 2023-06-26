package me.nixuge.noselfcontrol;

import java.io.File;

import lombok.Getter;
import lombok.Setter;
import me.nixuge.noselfcontrol.config.ConfigCache;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = McMod.MOD_ID,
        name = McMod.NAME,
        version = McMod.VERSION,
        guiFactory = "me.nixuge.noselfcontrol.gui.GuiFactory",
        clientSideOnly = true
)

@Setter
public class McMod {
    public static final String MOD_ID = "noselfcontrol";
    public static final String NAME = "No Self Control";
    public static final String VERSION = "1.0.0";


    @Getter
    @Mod.Instance(value = McMod.MOD_ID)
    private static McMod instance;
    @Getter
    private Configuration configuration;
    @Getter
    private String configDirectory;
    @Getter
    private ConfigCache configCache;
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        this.configDirectory = event.getModConfigurationDirectory().toString();
        final File path = new File(this.configDirectory + File.separator + McMod.MOD_ID + ".cfg");
        this.configuration = new Configuration(path);
        this.configCache = new ConfigCache(this.configuration);
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this.configCache);
    }
}
