package it.matty.duels.tasks.impl;

import it.matty.duels.DuelsPlugin;
import it.matty.duels.duel.Duel;
import it.matty.duels.duel.manager.DuelManager;
import it.matty.duels.tasks.Task;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class DuelCheckTask extends Task {
    private final DuelsPlugin plugin;
    private final DuelManager duelManager;
    
    @Override
    public void run() {
        Duel duel = duelManager.getCurrentDuel();
        if (duel != null && duel.isStarted()) return;
        
        for (Map.Entry<String, Duel> entry : duelManager.getDuels().entrySet()) {
            Duel aDuel = entry.getValue();
            
            if (!duelManager.isDuelActionable(aDuel)) continue;
            
            duelManager.setCurrentDuel(aDuel);
            duelManager.startDuel(aDuel);
            new DuelStartingTask(plugin, duelManager).enable();
        }
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
