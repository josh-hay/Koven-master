package cc.koven.core;

import cc.koven.core.command.CoreCommand;
import cc.koven.core.struct.config.FileManager;
import cc.koven.core.struct.db.MongoHandler;
import cc.koven.core.struct.db.RedisHandler;
import cc.koven.core.utils.command.CommandFramework;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class kCore extends JavaPlugin {

    @Getter private static kCore instance;
    @Getter private static Gson gson;
    @Getter private static String serverName;

    @Getter private FileManager fileManager;
    @Getter private MongoHandler mongoHandler;
    @Getter private RedisHandler redisHandler;

    @Override
    public void onEnable() {
        instance = this;
        gson = new GsonBuilder().setPrettyPrinting().create();
        fileManager = new FileManager(this);
        serverName = fileManager.getConfig().getString("settings.serverName");
        setupHandlers();
        registerCommands(new CoreCommand(this));
        registerListeners();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void setupHandlers() {
        fileManager = new FileManager(this);
        mongoHandler = new MongoHandler(this);
        redisHandler = new RedisHandler(this);
        redisHandler.registerPacketListeners();
        redisHandler.initialize();
    }

    private void registerCommands(Object... commands) {
        Arrays.stream(commands).forEach(new CommandFramework(this)::registerCommands);
    }
    private void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(l -> getServer().getPluginManager().registerEvents(l, this));
    }

}
