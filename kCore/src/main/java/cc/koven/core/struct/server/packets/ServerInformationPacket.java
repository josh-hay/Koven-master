package cc.koven.core.struct.server.packets;

import cc.koven.core.kCore;
import cc.koven.core.struct.db.RedisHandler;
import cc.koven.core.struct.db.obj.Packet;
import cc.koven.core.struct.server.ServerHandler;
import cc.koven.core.utils.generic.Maps;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;

import java.util.Map;

@AllArgsConstructor
public class ServerInformationPacket {

    private kCore kCore;

    @Packet("server-information")
    public void onServerInformation(Map<String, Object> map) {
        ServerHandler serverHandler = kCore.getServerHandler();

        if (!serverHandler.isAnnounceServerStatus()) return;

        String server = (String) map.get("server");
        String returnServer = (String) map.get("return");
        String player = (String) map.get("player");
        long time = Long.parseLong((String) map.get("time"));

        if (server.equalsIgnoreCase(kCore.getServerName())) {
            RedisHandler redisHandler = kCore.getRedisHandler();
            Map<String, Object> returnMap = Maps.createMap("server", server, "return", returnServer, "player", player, "time", time, "whitelisted", Bukkit.hasWhitelist(), "online", Bukkit.getOnlinePlayers().size(), "max-players", Bukkit.getMaxPlayers());
            redisHandler.sendAsync("server-information-return", returnMap);
        }
    }
}
