package cc.koven.core.utils.enums;

import cc.koven.core.kCore;
import cc.koven.core.utils.generic.Text;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Messages {

    PLUGIN_RELOADED("&aSuccessfully reloaded all configuration files."),

    SERVER_ONLINE("&b[S] &c%server% &fis now &aonline&f."),
    SERVER_OFFLINE("&b[S] &c%server% &fis now &coffline&f."),
    SEARCH_FAILED("&cThat server doesn't exist or is offline."),
    SERVER_INFORMATION("&c&lServer Information:\n &7* &fName: &c%server%\n &7* &fOnline: &cTrue\n &7* &fWhitelisted: &c%whitelisted%\n &7* &fPlayers: &c%players%&7/&c%maxPlayers% \n \n&aSearch completed in %time%ms."),

    ALREADY_LOGGED_IN("&cYou are already logged in.")
    ;

    private String defaultMessage;

    public String getMessage() {
        return kCore.getInstance().getFileManager().getMessages().get(getPath()) != null ?
                Text.colorize(kCore.getInstance().getFileManager().getMessages().getString(getPath())) :
                Text.colorize(defaultMessage);
    }
    public String getPath() {
        return this.toString().replace("_", "-");
    }
}
