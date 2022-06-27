package it.matty.duels.utils;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ChatUtils {
    
    private static final Pattern PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");
    
    public String textColor(String text, @Nullable Placeholder... placeholders) {
        if (placeholders != null)
            for (Placeholder placeholder : placeholders)
                text = text.replace(placeholder.getKey(), placeholder.getReplaced());
        
        Matcher match = PATTERN.matcher(text);
        while (match.find()) {
            String color = text.substring(match.start(), match.end());
            text = text.replace(color, ChatColor.of(color) + "");
            match = PATTERN.matcher(text);
        }
        
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
