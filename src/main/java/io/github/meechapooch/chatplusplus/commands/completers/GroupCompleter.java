package io.github.meechapooch.chatplusplus.commands.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import io.github.meechapooch.chatplusplus.ProfileMap;
import io.github.meechapooch.chatplusplus.UUIDManager;
import io.github.meechapooch.chatplusplus.chat.ChatGroup;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GroupCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //group <existing or new group> [add | remove] <players>
        if (args.length == 0 || args.length == 1) {
            LinkedList<String> ret = new LinkedList<>(ProfileMap.getProfile(sender.getName()).getGroupNames());
            ret.removeIf(string -> !string.toLowerCase().contains(args[0].toLowerCase()));
            return ret;
        } else if (args.length == 2) {
            return Arrays.asList("add", "remove");
        } else {
            boolean add;

            if (args[1].equals("add")) {
                add = true;
            } else if (args[1].equals("remove")) {
                add = false;
            } else {
                return new LinkedList<>();
            }

            LinkedList<String> ret = new LinkedList<>(UUIDManager.getPlayerNames());
            ret.removeIf((string) -> !string.toLowerCase().contains(args[args.length - 1].toLowerCase()));

            ChatGroup group = ProfileMap.getProfile(sender.getName()).getGroup(args[0]);

            if (add) {
                if (group == null) return ret;
                ret.removeIf(group::contains);
            } else {
                if (group == null) return new LinkedList<>();
                ret.removeIf((player) -> !group.contains(player));
            }

            return ret;
        }
    }
}
