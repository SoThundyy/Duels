package it.matty.duels.tasks.impl;

import it.matty.duels.DuelsPlugin;
import it.matty.duels.config.ConfigSettings;
import it.matty.duels.config.Language;
import it.matty.duels.duel.Duel;
import it.matty.duels.duel.manager.DuelManager;
import it.matty.duels.tasks.Task;
import it.matty.duels.utils.BukkitUtils;
import it.matty.duels.utils.Placeholder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class DuelStartingTask extends Task {
    private final DuelsPlugin plugin;
    private final DuelManager duelManager;
    private final AtomicInteger waitingTime = new AtomicInteger(ConfigSettings.WAITING_TIME.get(plugin, Integer.class));
    
    @Override
    public void run() {
        Duel duel = duelManager.getCurrentDuel();
        if (duel == null) return;
        if (duel.isStarted()) {
            disable();
            return;
        }
        
        if (waitingTime.get() == 0) {
            new DuelPlayingTask(plugin, duelManager).enable();
            disable();
            return;
        }
        
        if (!duelManager.hasEnoughPlayers(duel)) return;
        
        if (ConfigSettings.ANNOUNCE_WAITING_TIME.get(plugin, List.class).contains(waitingTime.get())) {
            BukkitUtils.sendMessageToAll(plugin, duel::isPlaying, Language.ANNOUNCE_WAITING_TIME,
                    new Placeholder("time", String.valueOf(waitingTime.get())));
            duel.getPlaying().forEach(((uuid, playerStatus) -> {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null || !player.isOnline()) return;
                
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }));
        }
        
        waitingTime.getAndDecrement();
    }
    
    @Override
    public void enable() {
        runTaskTimerAsynchronously(plugin, 0L, 20L);
    }
    
    @Override
    public void disable() {
        cancel();
    }
    
}
