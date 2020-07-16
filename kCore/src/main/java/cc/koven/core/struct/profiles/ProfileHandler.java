package cc.koven.core.struct.profiles;

import cc.koven.core.kCore;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class ProfileHandler {

    private Map<UUID, Profile> profiles;

    public ProfileHandler(kCore kCore) {
        profiles = Maps.newHashMap();
        kCore.getServer().getPluginManager().registerEvents(new ProfileListener(kCore), kCore);
    }

    public void addProfile(Player player) {
        profiles.put(player.getUniqueId(), new Profile());
    }

    public void removeProfile(Player player) {
        profiles.remove(player.getUniqueId());
    }

    public Profile getProfile(Player player) {
        return profiles.get(player.getUniqueId());
    }

    public boolean hasProfile(Player player) {
        return profiles.containsKey(player.getUniqueId());
    }


}
