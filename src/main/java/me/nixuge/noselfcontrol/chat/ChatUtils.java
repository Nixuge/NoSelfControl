package me.nixuge.noselfcontrol.chat;

import me.nixuge.noselfcontrol.McMod;
import me.nixuge.noselfcontrol.config.ConfigCache;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class ChatUtils {
    private static ConfigCache config = McMod.getInstance().getConfigCache();
    private static Minecraft mc = Minecraft.getMinecraft();

    public static void printChat(String text) {
        printChat(new ChatComponentText(text));
    }
    public static void printChat(IChatComponent chatComponent) {
        mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
    }

    
    public static void displayPreventChatMessage(String msg) {
        if (!config.isShowMessageInChat()) 
            return;
        
        if (config.isBypassClick()) {
            IChatComponent comp = new ChatComponentText("§6[NoSelfControl] §eMessage filtered out. §c[Click to send anyways]");
            ChatStyle style = new ChatStyle().setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/sendchatmsg " + msg));
            comp.setChatStyle(style);
            printChat(comp);
        } else {
            printChat("§6[NoSelfControl] §eMessage filtered out.");
        }
    }


    public static void sendChatMessagePassthrough(String msg, boolean addToChat) {
        System.out.println("Bypassing function called !");
        System.out.println(msg);
        // if (addToChat) {
        //     mc.ingameGUI.getChatGUI().addToSentMessages(msg);
        // }
        // if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0)
        //     return;

        // mc.thePlayer.sendChatMessage(msg);
    }
}
