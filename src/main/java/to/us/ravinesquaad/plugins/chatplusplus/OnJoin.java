package to.us.ravinesquaad.plugins.chatplusplus;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUIDManager.playerJoined(event.getPlayer().getName(),event.getPlayer().getUniqueId().toString());
    }
}
