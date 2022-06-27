package it.matty.duels.commands;

import com.google.common.collect.ImmutableList;
import it.matty.duels.DuelsPlugin;
import it.matty.duels.config.ConfigSettings;
import it.matty.duels.config.Language;
import it.matty.duels.duel.Duel;
import it.matty.duels.duel.PlayerStatus;
import it.matty.duels.duel.manager.DuelManager;
import it.matty.duels.utils.BukkitUtils;
import it.matty.duels.utils.Placeholder;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("NullableProblems")
@RequiredArgsConstructor
public class DuelCommand implements CommandExecutor, TabCompleter {
    private final DuelsPlugin plugin;
    private final DuelManager duelManager;
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        
        Player player = (Player) sender;
        if (args.length == 0) {
            Language.HELP_MESSAGE.send(plugin, player);
            return true;
        }
        
        Duel duel = duelManager.getCurrentDuel();
        switch (args[0].toLowerCase()) {
            case "join":
                if (duel == null) {
                    Language.NO_DUEL_STARTED.send(plugin, player);
                    return true;
                }
                
                if (duel.isStarted() || duelManager.hasReachedMaxPlayers(duel)) {
                    duel.getPlaying().put(player.getUniqueId(), PlayerStatus.SPECTATOR);
                    Language.DUEL_ALREADY_STARTED.send(plugin, player);
                } else {
                    duel.getPlaying().put(player.getUniqueId(), PlayerStatus.PLAYING);
                    Language.JOINED_DUEL.send(plugin, player);
                }
                
                player.teleport(ConfigSettings.LOCATION_SPAWN_WAITING.get(plugin));
                break;
            case "left":
                if (duel == null) {
                    Language.NO_DUEL_STARTED.send(plugin, player);
                    return true;
                }
                
                if (!duel.isPlaying(player)) {
                    Language.PLAYER_NOT_PLAYING.send(plugin, player);
                    return true;
                }
                
                duel.getPlaying().remove(player.getUniqueId());
                Language.SELF_PLAYER_LEFT.send(plugin, player);
                BukkitUtils.sendMessageToAll(plugin, duel::isPlaying, Language.PLAYER_LEFT_DUEL,
                        new Placeholder("player", player.getName()));
                break;
            default:
                Language.HELP_MESSAGE.send(plugin, player);
                break;
        }
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) return null;
        
        return ImmutableList.of("join", "left");
    }
}
