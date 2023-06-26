package me.nixuge.noselfcontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand extends CommandBase {
    private final String name;

    public AbstractCommand(final String name) {
        this.name = name;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return this.name;
    }

    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "/" + this.name;
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(final ICommandSender sender, final String[] args) {
        this.onCommand(sender, args);
    }

    public abstract void onCommand(final ICommandSender sender, final String[] args);
}
