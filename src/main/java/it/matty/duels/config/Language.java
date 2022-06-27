package it.matty.duels.config;

import it.matty.duels.DuelsPlugin;
import it.matty.duels.utils.ChatUtils;
import it.matty.duels.utils.Placeholder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum Language {
    
    DUEL_STARTED_MESSAGE("duel-started"),
    HELP_MESSAGE("help-message"),
    DUEL_ALREADY_STARTED("game-already-started"),
    NO_DUEL_STARTED("duel-not-started"),
    JOINED_DUEL("joined-duel"),
    PLAYER_NOT_PLAYING("player-not-playing"),
    SELF_PLAYER_LEFT("self-left-duel"),
    PLAYER_LEFT_DUEL("player-left-duel"),
    ANNOUNCE_WAITING_TIME("announce-waiting-time"),
    GREET_PLAYER_BET("greet-player-bet"),
    LOSE_PLAYER_BET("lose-player-bet"),
    PLAYER_WINS_DUEL("player-wins-duel"),
    ;
    
    private final String key;
    
    public void send(DuelsPlugin plugin, CommandSender sender, @Nullable Placeholder... placeholders) {
        FileConfiguration configuration = plugin.getConfig();
        ConfigurationSection section = Objects.requireNonNull(configuration.getConfigurationSection("language"));
        
        if (section.get(key) instanceof Collection<?>) {
            section.getStringList(key).stream()
                    .map(s -> ChatUtils.textColor(s, placeholders))
                    .forEach(sender::sendMessage);
        } else {
            String message = section.getString(key);
            message = ChatUtils.textColor(message, placeholders);
            sender.sendMessage(message);
        }
    }
}
