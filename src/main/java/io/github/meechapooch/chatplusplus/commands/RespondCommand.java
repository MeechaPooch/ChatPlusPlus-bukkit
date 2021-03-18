package io.github.meechapooch.chatplusplus.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import io.github.meechapooch.chatplusplus.ProfileMap;
import io.github.meechapooch.chatplusplus.chat.ChatThing;

public class RespondCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return false;
        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }
        ChatThing activeChat = ProfileMap.getProfile(sender.getName()).getActiveChat();
        if (activeChat == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You need to be in an active DM or group to respond!");
        } else {
            ProfileMap.getProfile(sender.getName()).getActiveChat().sendMessage(sender.getName(), message.toString());
        }

        return true;
    }
}
