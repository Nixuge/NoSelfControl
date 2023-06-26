package me.nixuge.noselfcontrol.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.nixuge.noselfcontrol.chat.ChatChecker;
import me.nixuge.noselfcontrol.chat.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Shadow
    private Minecraft mc;

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String msg, boolean addToChat, CallbackInfo ci) {
        if (!ChatChecker.validateMessage(msg)) {
            if (addToChat) // Add to chat history anyways even if failed to send.
                this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
            ChatUtils.displayPreventChatMessage(msg);
            ci.cancel();
            return;
        }
    }
}
