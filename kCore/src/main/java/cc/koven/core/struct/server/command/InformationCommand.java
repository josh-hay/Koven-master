package cc.koven.core.struct.server.command;

import cc.koven.core.kCore;
import cc.koven.core.struct.db.RedisHandler;
import cc.koven.core.struct.server.ServerHandler;
import cc.koven.core.utils.command.Command;
import cc.koven.core.utils.command.CommandArgs;
import cc.koven.core.utils.command.Completer;
import cc.koven.core.utils.enums.Messages;
import cc.koven.core.utils.generic.Maps;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Map;

@AllArgsConstructor
public class InformationCommand {

    private kCore kCore;

    @Command(name = "information", aliases = {"info"}, permission = "core.command.information")
    public void information(CommandArgs args) {
        if (args.length() < 1) {
            args.getSender().sendMessage(ChatColor.RED + "Usage: /" + args.getLabel() + " <server>");
            return;
        }
        String server = args.getArgs(0);
        RedisHandler redisHandler = kCore.getRedisHandler();
        ServerHandler serverHandler = kCore.getServerHandler();

        long searchTime = System.currentTimeMillis();
        Map<String, Object> map = Maps.createMap("server", server, "return", kCore.getServerName(), "player", args.getSender().getName(), "time", searchTime);

        redisHandler.sendAsync("server-information", map);

        Bukkit.getScheduler().runTaskLater(kCore, () -> {

            if (!serverHandler.getCompletedSearches().contains(searchTime)) {
                args.getSender().sendMessage(Messages.SEARCH_FAILED.getMessage().replaceAll("%server%", server));
            }

        }, 15L);
    }
}
