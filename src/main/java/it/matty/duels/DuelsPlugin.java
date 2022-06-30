package it.matty.duels;

import it.matty.duels.commands.BetCommand;
import it.matty.duels.commands.DuelCommand;
import it.matty.duels.duel.manager.DuelManager;
import it.matty.duels.tasks.Task;
import it.matty.duels.tasks.impl.DuelCheckTask;
import it.matty.duels.tasks.impl.DuelStartingTask;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public final class DuelsPlugin extends JavaPlugin {
    private DuelManager duelManager;
    private Economy economy;
    
    @Override
    public void onEnable() {
        loadConfigurations();
        loadInstances();
        loadTasks(
                new DuelCheckTask(this, duelManager)
        );
    }
    
    @Override
    public void onDisable() {
    
    }
    
    private void loadConfigurations() {
        saveDefaultConfig();
        getLogger().info("Configurations files loaded.");
    }
    
    private void loadInstances() {
       duelManager = new DuelManager(this);
       duelManager.enable();
       economy = Objects.requireNonNull(getServer().getServicesManager().getRegistration(Economy.class)).getProvider();
    }
    
    private void loadCommands() {
        getCommand("duel").setExecutor(new DuelCommand(this, duelManager));
        getCommand("bet").setExecutor(new BetCommand(this, duelManager));
    }
    
    private void loadTasks(Task... tasks) {
        for (Task task : tasks) {
            task.enable();
        }
    }
}
