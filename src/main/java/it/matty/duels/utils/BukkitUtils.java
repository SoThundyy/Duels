package it.matty.duels.utils;

import com.sun.istack.internal.Nullable;
import it.matty.duels.DuelsPlugin;
import it.matty.duels.config.Language;
import it.matty.duels.conversation.BetPrompt;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

@UtilityClass
public class BukkitUtils {
    
    public void sendMessageToAll(DuelsPlugin plugin, Language language, @Nullable Placeholder... placeholders) {
        sendMessageToAll(plugin, player -> true, language, placeholders);
    }
    
    public void sendMessageToAll(DuelsPlugin plugin, Predicate<Player> filter, Language language,
                                 @Nullable Placeholder... placeholders) {
        Bukkit.getOnlinePlayers().stream()
                .filter(filter)
                .forEach(player -> language.send(plugin, player, placeholders));
    }
    
    public void sendMessageToAll(ConfigurationSection section, String path, @Nullable Placeholder... placeholders) {
        Bukkit.getOnlinePlayers()
                .forEach(player -> player.sendMessage(ChatUtils.textColor(section.getString(path), placeholders)));
    }
    
    public Location deserializeLocation(String path) {
        String[] args = path.split(";");
        
        return new Location(
                Bukkit.getWorld(args[0]),
                Double.parseDouble(args[1]),
                Double.parseDouble(args[2]),
                Double.parseDouble(args[3]),
                Float.parseFloat(args[4]),
                Float.parseFloat(args[5])
        );
    }
    
    public Conversation getBetConversation(DuelsPlugin plugin, Player who, Player selected) {
        return new ConversationFactory(plugin)
                .withLocalEcho(false)
                .withEscapeSequence("cancel")
                .withTimeout(30)
                .withFirstPrompt(new BetPrompt(plugin, plugin.getDuelManager(), selected))
                .buildConversation(who);
    }
}
