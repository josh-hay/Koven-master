package cc.koven.core.struct.profiles;

import cc.koven.core.kCore;
import cc.koven.core.utils.enums.Messages;
import cc.koven.core.utils.generic.Text;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class ProfileListener implements Listener {

    private kCore kCore;

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        Player player = Bukkit.getPlayer(event.getName());

        if (player != null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Messages.ALREADY_LOGGED_IN.getMessage());
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        ProfileHandler profileHandler = kCore.getProfileHandler();
        if (!profileHandler.hasProfile(player)) {
            profileHandler.addProfile(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ProfileHandler profileHandler = kCore.getProfileHandler();
        profileHandler.removeProfile(player);
    }

/*    public String checkTag(Player player) {
        Profile profile = kCore.getProfileHandler().getProfile(player);
        if (profile.getTag() != null) {
            return " " + profile.getTag().getSuffix();
        } else {
            return "";
        }
    }*/

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        if(!kCore.getConfig().getBoolean("settings.formatChat")) return;
        Player player = event.getPlayer();
        Profile profile = kCore.getProfileHandler().getProfile(player);
        String format = Text.colorize(kCore.getConfig().getString("settings.chatFormat"));
        format = format.replaceAll("%tag%", "")
                .replaceAll("%prefix%", "")
                .replaceAll("%player%", player.getName())
                .replaceAll("%message%", player.hasPermission("core.chatcolor") ? Text.colorize(event.getMessage()) : event.getMessage())
                .replaceAll("%", "%%");
        event.setFormat(format);
    }

}
