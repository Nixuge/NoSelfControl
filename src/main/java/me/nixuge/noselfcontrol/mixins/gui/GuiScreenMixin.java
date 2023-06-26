package me.nixuge.noselfcontrol.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.nixuge.noselfcontrol.McMod;
import me.nixuge.noselfcontrol.config.ConfigCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.ChatComponentText;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Shadow
    private Minecraft mc;

    private ConfigCache config = McMod.getInstance().getConfigCache();

    public void printChat(String text) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(text));
    }

    /**
     * Processes the wordlist & regex matches
     * 
     * @param msg message to check
     * @return message valid or not
     */
    public boolean isMessageValid(String msg) {
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
    public boolean validateMessage(String msg) {
        // Global checks
        if (config.isEnabledAllServers()) {
            return isMessageValid(msg);
        } else if (config.isEnabledSolo() && mc.isIntegratedServerRunning()) {
            return isMessageValid(msg);
        }

        // Server specific checks
        ServerData sd = this.mc.getCurrentServerData();
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

    public void displayPreventChatMessage() {
        if (!config.isShowMessageInChat()) 
            return;
        
        if (config.isBypassClick()) {
            printChat("§6[NoSelfControl] §eMessage filtered out (with bypass !).");
        } else {
            printChat("§6[NoSelfControl] §eMessage filtered out.");
        }
    }

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String msg, boolean addToChat, CallbackInfo ci) {
        if (!validateMessage(msg)) {
            if (addToChat) // Add to chat history anyways even if failed to send.
                this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
            displayPreventChatMessage();
            ci.cancel();
            return;
        }
    }

    public void sendChatMessagePassthrough(String msg, boolean addToChat) {
        if (addToChat) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
        }
        if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0)
            return;

        this.mc.thePlayer.sendChatMessage(msg);
    }

}
