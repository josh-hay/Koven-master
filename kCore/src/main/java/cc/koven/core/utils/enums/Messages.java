package cc.koven.core.utils.enums;

import cc.koven.core.kCore;
import cc.koven.core.utils.generic.Text;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Messages {

    PLUGIN_RELOADED("&aSuccessfully reloaded all configuration files.");

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
