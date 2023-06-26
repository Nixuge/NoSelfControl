package me.nixuge.noselfcontrol.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatComponentText;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Shadow
    private Minecraft mc;

    public void printChat(String text) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(text));
    }

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String msg, boolean addToChat, CallbackInfo ci) {
        if (msg.contains("testmsg")) {
            printChat("§eUnfortunately your message was considered offensive, and so not sent.");
            ci.cancel();
        }
    }

    public void sendChatMessagePassthrough(String msg, boolean addToChat) {
        if (addToChat) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
        }
        if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0) return;

        this.mc.thePlayer.sendChatMessage(msg);
    }

}
