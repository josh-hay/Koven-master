package cc.koven.core.utils.generic;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Text {

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> colorize(List<String> s) {
        return s.stream().map(Text::colorize).collect(Collectors.toList());
    }

    public static List<String> colorize(String... s) {
        return Arrays.stream(s).map(Text::colorize).collect(Collectors.toList());
    }
}
