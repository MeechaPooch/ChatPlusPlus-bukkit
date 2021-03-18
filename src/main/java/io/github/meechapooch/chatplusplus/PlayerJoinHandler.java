package io.github.meechapooch.chatplusplus;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinHandler implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUIDManager.playerJoined(event.getPlayer().getName(), event.getPlayer().getUniqueId().toString());
    }
}
