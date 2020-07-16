package cc.koven.core.struct.server.packets;

import cc.koven.core.kCore;
import cc.koven.core.struct.db.obj.Packet;
import cc.koven.core.struct.server.ServerHandler;
import cc.koven.core.utils.enums.Messages;
import cc.koven.core.utils.generic.Strings;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

@AllArgsConstructor
public class ServerInformationReturnPacket {

    private kCore kCore;

    @Packet("server-information-return")
    public void onServerInformationReturn(Map<String, Object> map) {
        ServerHandler serverHandler = kCore.getServerHandler();

        if (!serverHandler.isAnnounceServerStatus()) return;

        String server = (String) map.get("server");
        String returnServer = (String) map.get("return");
        String player = (String) map.get("player");
        long time = Long.parseLong((String) map.get("time"));
        String whitelisted = (String) map.get("whitelisted");
        String online = (String) map.get("online");
        String maxPlayers = (String) map.get("max-players");

        if (kCore.getServerName().equalsIgnoreCase(returnServer)) {
            serverHandler.getCompletedSearches().add(time);

            String message = Strings.replace(Messages.SERVER_INFORMATION.getMessage(),
                    "%server%", server,
                    "%whitelisted%", (whitelisted.substring(0, 1).toUpperCase() + whitelisted.substring(1)),
                    "%players%", online,
                    "%maxPlayers%", maxPlayers,
                    "%time%", String.valueOf(System.currentTimeMillis() - time)
            );

            if (player.equalsIgnoreCase("console")) {
                Bukkit.getConsoleSender().sendMessage(message);
            } else {
                Player bukkitPlayer = Bukkit.getPlayer(player);
                if (bukkitPlayer != null) {
                    bukkitPlayer.sendMessage(message);
                }
            }
        }
    }
}
