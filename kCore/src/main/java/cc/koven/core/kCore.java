package cc.koven.core;

import cc.koven.core.command.CoreCommand;
import cc.koven.core.struct.cache.CacheHandler;
import cc.koven.core.struct.cache.packets.AddCachePacket;
import cc.koven.core.struct.config.FileManager;
import cc.koven.core.struct.db.MongoHandler;
import cc.koven.core.struct.db.RedisHandler;
import cc.koven.core.struct.profiles.ProfileHandler;
import cc.koven.core.struct.server.ServerHandler;
import cc.koven.core.struct.server.packets.ServerInformationPacket;
import cc.koven.core.struct.server.packets.ServerInformationReturnPacket;
import cc.koven.core.struct.server.packets.ServerStatusPacket;
import cc.koven.core.utils.command.CommandFramework;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class kCore extends JavaPlugin {

    @Getter private static kCore instance;
    @Getter private static Gson gson;
    @Getter private static String serverName;

    @Getter private CommandFramework commandFramework;

    @Getter private FileManager fileManager;
    @Getter private MongoHandler mongoHandler;
    @Getter private RedisHandler redisHandler;
    @Getter private ServerHandler serverHandler;
    @Getter private CacheHandler cacheHandler;
    @Getter private ProfileHandler profileHandler;

    @Override
    public void onEnable() {
        instance = this;
        gson = new GsonBuilder().setPrettyPrinting().create();
        fileManager = new FileManager(this);
        serverName = fileManager.getConfig().getString("settings.serverName");
        commandFramework = new CommandFramework(this);
        setupHandlers();
        commandFramework.registerCommands(new CoreCommand(this));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void setupHandlers() {
        fileManager = new FileManager(this);
        mongoHandler = new MongoHandler(this);
        redisHandler = new RedisHandler(this);
        redisHandler.registerPacketListeners(
                new ServerInformationPacket(this),
                new ServerInformationReturnPacket(this),
                new ServerStatusPacket(this),
                new AddCachePacket(this)
        );
        redisHandler.initialize();
        serverHandler = new ServerHandler(this);
        cacheHandler = new CacheHandler(this);
        profileHandler = new ProfileHandler(this);
    }
}
