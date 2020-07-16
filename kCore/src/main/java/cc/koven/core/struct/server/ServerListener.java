package cc.koven.core.struct.server;

import cc.koven.core.kCore;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

@AllArgsConstructor
public class ServerListener implements Listener {

    private kCore kCore;

    @EventHandler
    public void onStop(PluginDisableEvent event) {
        ServerHandler serverHandler = kCore.getServerHandler();
        serverHandler.announceServerStatus(false);
    }
}
