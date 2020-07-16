package cc.koven.core.command;

import cc.koven.core.kCore;
import cc.koven.core.utils.command.Command;
import cc.koven.core.utils.command.CommandArgs;
import cc.koven.core.utils.enums.Messages;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CoreCommand {

    private kCore kCore;

    @Command(name = "core.reload", permission = "core.reload")
    public void reload(CommandArgs args) {
        kCore.getFileManager().reloadConfig();
        kCore.getFileManager().reloadMessages();
        args.getSender().sendMessage(Messages.PLUGIN_RELOADED.getMessage());
    }
}
