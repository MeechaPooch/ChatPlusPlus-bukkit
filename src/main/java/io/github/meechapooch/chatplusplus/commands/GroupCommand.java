package io.github.meechapooch.chatplusplus.commands;

import io.github.meechapooch.chatplusplus.UUIDManager;
import io.github.meechapooch.chatplusplus.chat.ChatGroup;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import io.github.meechapooch.chatplusplus.ProfileMap;

import java.util.HashSet;

public class GroupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // group pogPEOPLE add TurtleMasterPDX palight pinkylemon21
        if (args.length < 3) return false;
        String name = args[0];
        if (!(args[1].equals("add") || args[1].equals("remove"))) {
            sender.sendMessage(ChatColor.DARK_RED + "'" + args[1] + "' is not a recognised operation! Use '/group add <players>' or '/group remove <players>'");
            return true;
        }

        //collect users to add/remove
        HashSet<String> players = new HashSet<>();
        int i;
        for (i = 0; i < args.length; i++) {
            if (i <= 1) continue;
            if (UUIDManager.players.containsKey(args[i]) || args[i].equalsIgnoreCase("console"))
                players.add(args[i]);
            else {
                sender.sendMessage(ChatColor.DARK_RED + "Cannot find player '" + args[i] + "'");
                continue;
            }
        }

        //record whether to add or delete members
        boolean add = args[1].equals("add");

        //Create new / get existing group
        if (ProfileMap.groupExists(name)) {
            ChatGroup group = ProfileMap.getProfile(sender.getName()).getGroup(name);
            if (group == null) {
                sender.sendMessage(ChatColor.DARK_RED + "Group exists, but you're not invited!");
                return true;
            } else {
                if (add) {
                    group.addMembers(sender.getName(), players);
                } else {
                    group.removeMembers(sender.getName(), players);
                }
            }
        } else {
            //group doesnt exist
            if (add) {
//                sender.sendMessage("[DEBUG] group doesnt exist, making one");
                players.add(sender.getName());
                ProfileMap.newGroup(sender.getName(), name, players);
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "Group '" + name + "' doesnt exist.");
                return true;
            }
        }
        ProfileMap.writeFromProfiles();


        return true;
    }
}
