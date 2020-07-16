package cc.koven.core.struct.config;

import cc.koven.core.kCore;
import cc.koven.core.utils.enums.Messages;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Getter
public class FileManager {

    private File userCacheFolder, configFile, messagesFile;
    private YamlConfiguration config, messages;

    public FileManager(kCore kCore) {
        userCacheFolder = new File(kCore.getDataFolder() + File.separator + "usercache");
        configFile = new File(kCore.getDataFolder(), "config.yml");
        messagesFile = new File(kCore.getDataFolder(), "messages.yml");

        if(!userCacheFolder.exists()) { userCacheFolder.mkdir(); }
        if(!configFile.exists()) { kCore.saveResource("config.yml", false); }
        if(!messagesFile.exists()) { kCore.saveResource("messages.yml", false); }

        config = YamlConfiguration.loadConfiguration(configFile);
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        Arrays.stream(Messages.values()).filter(m -> messages.get(m.getPath()) == null).forEach(m -> messages.set(m.getPath(), m.getDefaultMessage()));
        saveMessages();
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveMessages() {
        try {
            messages.save(messagesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    public void reloadMessages() {
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }
}
