package to.us.ravinesquaad.plugins.chatplusplus;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class ChatPlusPlus extends JavaPlugin {
    //TODO Add group event log for offline players... When you log in, you get a backlog of join and leave messages for each group chat you're in

    //TODO Add clickable player and group chat names which auto begin commands to send messages

    public static HashMap<Player,PlayerProfile> profiles = new HashMap<>();
    static ChatPlusPlus instance;
    public static final boolean debug = true;

    public ChatPlusPlus() {
        instance = this;
    }

    @Override
    public void onEnable() {
        System.out.println("PLUGIN INABLING!!!!!!!!!!!!!!!!!!!!! BRUUUUUUUUUUUUHHHHH");
        UUIDManager.init();
        ProfileMap.init();
        getServer().getPluginManager().registerEvents(new OnJoin(),this);


        getCommand("dm").setExecutor(new DMCommand());
        getCommand("dm").setTabCompleter(new DMCompleter());

        getCommand("group").setExecutor(new GroupCommand());
        getCommand("group").setTabCompleter(new GroupCompleter());

        getCommand("respond").setExecutor(new RespondCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ProfileMap.write();
    }
}
