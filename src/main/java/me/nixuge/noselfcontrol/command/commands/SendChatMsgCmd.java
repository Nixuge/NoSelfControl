package me.nixuge.noselfcontrol.command.commands;


import me.nixuge.noselfcontrol.chat.ChatUtils;
import me.nixuge.noselfcontrol.command.AbstractCommand;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class SendChatMsgCmd extends AbstractCommand {
    public SendChatMsgCmd() {
        super("sendchatmessage");
    }

    @Override
    public List<String> getCommandAliases() {
        ArrayList<String> al = new ArrayList<>();
        al.add("sendchatmsg");
        return al;
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        System.out.println(args);
        ChatUtils.sendChatMessagePassthrough("owo", true);
    }
}
