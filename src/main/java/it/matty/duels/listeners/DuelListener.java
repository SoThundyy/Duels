package it.matty.duels.listeners;

import it.matty.duels.DuelsPlugin;
import it.matty.duels.config.Language;
import it.matty.duels.duel.Duel;
import it.matty.duels.duel.manager.DuelManager;
import it.matty.duels.utils.BukkitUtils;
import it.matty.duels.utils.Placeholder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class DuelListener implements Listener {
    private final DuelsPlugin plugin;
    private final DuelManager duelManager;
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        Duel duel = duelManager.getCurrentDuel();
        if (duel == null ||
                !duel.isStarted() ||
                !duel.getFighting().contains(player.getUniqueId())) return;
    
        duelManager.handleFighters(duel.getFighting());
        duel.getFighting().remove(player.getUniqueId());
        duelManager.greetBetters(duel, duel.getFighting().get(0));
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        Duel duel = duelManager.getCurrentDuel();
        if (duel == null) return;
        
        if (!duel.isStarted()) {
            duel.getPlaying().remove(player.getUniqueId());
            
            BukkitUtils.sendMessageToAll(plugin, duel::isPlaying, Language.PLAYER_LEFT_DUEL,
                    new Placeholder("player", player.getName()));
            return;
        }
        
        if (duel.hasFighters() && duel.getFighting().contains(player.getUniqueId())) {
            duel.getFighting().remove(player.getUniqueId());
            duelManager.greetBetters(duel, duel.getFighting().get(0));
            duelManager.handleFighters(duel.getFighting());
        }
    }
}
