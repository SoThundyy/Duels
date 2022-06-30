package it.matty.duels.tasks.impl;

import it.matty.duels.DuelsPlugin;
import it.matty.duels.config.Language;
import it.matty.duels.duel.Duel;
import it.matty.duels.duel.manager.DuelManager;
import it.matty.duels.tasks.Task;
import it.matty.duels.utils.BukkitUtils;
import it.matty.duels.utils.Placeholder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class DuelPlayingTask extends Task {
    private final DuelsPlugin plugin;
    private final DuelManager duelManager;
    
    @Override
    public void run() {
        Duel duel = duelManager.getCurrentDuel();
        if (duel == null || !duel.isStarted()) return;
        
        if (duel.hasFighters()) return;
        
        switch (duel.getPlayingPlayers().size()) {
            case 0:
                disable();
                break;
            case 1:
                Player winner = Bukkit.getPlayer(duel.getPlayingPlayers().get(0));
                if (winner == null || !winner.isOnline()) return;
                
                BukkitUtils.sendMessageToAll(plugin, Language.PLAYER_WINS_DUEL,
                        new Placeholder("player", winner.getName()));
                break;
            default:
                duelManager.pickRandomFighters(duel);
                break;
        }
    }
    
    @Override
    public void enable() {
        runTaskTimerAsynchronously(plugin, 0, 20L);
    }
    
    @Override
    public void disable() {
        cancel();
    }
}
