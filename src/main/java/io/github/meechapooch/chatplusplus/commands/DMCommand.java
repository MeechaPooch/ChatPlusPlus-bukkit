package io.github.meechapooch.chatplusplus.commands;

import io.github.meechapooch.chatplusplus.PlayerProfile;
import io.github.meechapooch.chatplusplus.ProfileMap;
import io.github.meechapooch.chatplusplus.chat.ChatGroup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import io.github.meechapooch.chatplusplus.chat.ChatDM;

public class DMCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(command.getUsage());
            return false;
        }
        if (args.length == 1) return false;
        // test if to-arg is a player or the console

        String message = "";
        boolean offname = false;
        for (String arg : args) {
            if (offname) message = message + arg + " ";
            offname = true;
        }

        if (Bukkit.getPlayer(args[0]) != null && (Bukkit.getPlayer(args[0]).isOnline() || args[0].equalsIgnoreCase("console"))) {

            ChatDM dm = new ChatDM(args[0]);
            dm.sendMessage(sender, message);
            ProfileMap.getProfile(sender.getName()).setActiveChat(dm);

//            if(ChatPlusPlus.profiles.containsKey(sender));

            return true;

            // test if to-arg is a group
        } else if (ProfileMap.getProfile(sender.getName()).getGroup(args[0]) != null) {
//        } else if (ProfileMap.profiles.getOrDefault(sender.getName(),new PlayerProfile("null")).chats.stream().map(chat->chat.name).collect(Collectors.toList()).contains(args[0])) {
            PlayerProfile profile = ProfileMap.getProfile(sender.getName());
            ChatGroup group = profile.getGroup(args[0]);
            group.sendMessage(sender.getName(), message);
            profile.setActiveChat(group);

            return true;
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "Cannot find player or group '" + args[0] + "'");
            return false;
        }
    }
}
