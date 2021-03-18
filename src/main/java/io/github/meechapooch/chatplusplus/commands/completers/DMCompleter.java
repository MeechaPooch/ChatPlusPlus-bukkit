package io.github.meechapooch.chatplusplus.commands.completers;

import io.github.meechapooch.chatplusplus.ProfileMap;
import io.github.meechapooch.chatplusplus.chat.ChatGroup;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DMCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0 || args.length == 1) {
            List<String> ret = new LinkedList<>();
//            Bukkit.getOnlinePlayers().stream().map(player -> player.getName()).collect(Collectors.toList());
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                // TODO Strip chat colors in display names
                ret.add(onlinePlayer.getDisplayName());
            }
            ret.addAll(ProfileMap.getProfile(sender.getName()).getGroups().stream().map(ChatGroup::getName).collect(Collectors.toList()));
            ret.removeIf((string) -> !string.toLowerCase().contains(args[0].toLowerCase()));
            return ret;
        } else return new LinkedList<>();
    }
}
