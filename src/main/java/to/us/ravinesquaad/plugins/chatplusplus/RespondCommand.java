package to.us.ravinesquaad.plugins.chatplusplus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RespondCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) return false;
        String message = "";
        for (String arg : args) {
            message = message + arg + " ";
        }
        ChatThing activeChat = ProfileMap.getProfile(sender.getName()).activeChat;
        if(activeChat == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You need to be in an active DM or group to respond!");
        } else {
            ProfileMap.getProfile(sender.getName()).activeChat.sendMessage(sender.getName(),message);
        }

        return true;
    }
}
