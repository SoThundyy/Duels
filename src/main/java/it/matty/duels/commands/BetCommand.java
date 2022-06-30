package it.matty.duels.commands;

import it.matty.duels.DuelsPlugin;
import it.matty.duels.config.Language;
import it.matty.duels.duel.Duel;
import it.matty.duels.duel.manager.DuelManager;
import it.matty.duels.prediction.Prediction;
import it.matty.duels.utils.Placeholder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class BetCommand implements CommandExecutor {
    private final DuelsPlugin plugin;
    private final DuelManager duelManager;
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            Language.BET_HELP_MESSAGE.send(plugin, player);
            return true;
        }
        
        if (args.length > 2) {
            Duel duel = duelManager.getCurrentDuel();
            
            if (duel == null || !duel.isStarted()) {
                Language.NO_DUEL_STARTED.send(plugin, player);
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            
            if (target == null || !target.isOnline() || !duel.isPlaying(target)) {
                Language.PLAYER_NOT_PLAYING.send(plugin, player, new Placeholder("player", args[0]));
                return true;
            }
            
            if (!duel.hasFighters() || duel.getFighting().contains(target.getUniqueId())) {
                Language.PLAYER_NOT_FIGHTER.send(plugin, player, new Placeholder("player", target.getName()));
                return true;
            }
            
            if (!NumberUtils.isNumber(args[1])) {
                Language.VALUE_IS_NOT_NUMERIC.send(plugin, player);
                return true;
            }
            
            double money = Double.parseDouble(args[1]);
            if (!plugin.getEconomy().has(player, money)) {
                Language.PLAYER_NOT_ENOUGH_MONEY.send(plugin, player,
                        new Placeholder("money", money + ""));
                return true;
            }
            
            duel.getBetters().put(player.getUniqueId(), new Prediction(target.getUniqueId(), new BigDecimal(money)));
        }
        return true;
    }
}
