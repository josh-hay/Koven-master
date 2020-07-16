package cc.koven.core.struct.server;

import cc.koven.core.kCore;
import cc.koven.core.struct.db.RedisHandler;
import cc.koven.core.struct.server.command.InformationCommand;
import cc.koven.core.utils.generic.Maps;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerHandler {

    private kCore kCore;

    @Getter private boolean announceServerStatus;
    @Getter private List<Long> completedSearches;
    @Getter private List<String> offlineServers;

    public ServerHandler(kCore kCore) {
        this.kCore = kCore;
        announceServerStatus = kCore.getConfig().getBoolean("settings.announceServerStatus");
        completedSearches = new ArrayList<>();
        offlineServers = new ArrayList<>();

        kCore.getCommandFramework().registerCommands(new InformationCommand(kCore));
        Bukkit.getPluginManager().registerEvents(new ServerListener(kCore), kCore);
        announceServerStatus(true);
    }

    public void announceServerStatus(boolean start) {
        RedisHandler redisHandler = kCore.getRedisHandler();
        Map<String, Object> map = Maps.createMap("server", kCore.getServerName(), "start", String.valueOf(start));
        if (start) {
            redisHandler.sendAsync("server-status", map);
        } else {
            redisHandler.sendSync("server-status", map);
        }
    }
}
