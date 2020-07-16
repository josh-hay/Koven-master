package cc.koven.core.struct.server.packets;

import cc.koven.core.kCore;
import cc.koven.core.struct.db.obj.Packet;
import cc.koven.core.struct.server.ServerHandler;
import cc.koven.core.utils.enums.Messages;
import cc.koven.core.utils.generic.Strings;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.Map;

@AllArgsConstructor
public class ServerStatusPacket {

    private kCore kCore;

    @Packet("server-status")
    public void onServerStatus(Map<String, Object> map) {
        ServerHandler serverHandler = kCore.getServerHandler();
        if (!serverHandler.isAnnounceServerStatus()) return;

        String server = (String) map.get("server");
        boolean start = Boolean.parseBoolean((String) map.get("start"));

        if(start) {
            serverHandler.getOfflineServers().remove(server);
            Bukkit.broadcast(Strings.replace(Messages.SERVER_ONLINE.getMessage(), "%server%", server), Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
        } else {
            if(!serverHandler.getOfflineServers().contains(server)) serverHandler.getOfflineServers().add(server);
            Bukkit.broadcast(Strings.replace(Messages.SERVER_OFFLINE.getMessage(), "%server%", server), Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
        }
    }
}
