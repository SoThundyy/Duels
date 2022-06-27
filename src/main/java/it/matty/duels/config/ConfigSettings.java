package it.matty.duels.config;

import it.matty.duels.DuelsPlugin;
import it.matty.duels.utils.BukkitUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.Objects;

@RequiredArgsConstructor
public enum ConfigSettings {
    
    MAX_PLAYERS("system.max-player"),
    MIN_PLAYERS("system.min-players"),
    LOCATION_SPAWN_ONE("system.spawn-player-one"),
    LOCATION_SPAWN_TWO("system.spawn-player-two"),
    LOCATION_SPAWN_WAITING("system.spawn-waiting"),
    WAITING_TIME("system.waiting-time"),
    ANNOUNCE_WAITING_TIME("system.announce-waiting-time"),
    BET_GUI_SIZE("system.bet-gui.size"),
    FIRST_FIGHTER_SLOT("system.player-one.slot"),
    SECOND_FIGHTER_SLOT("system.player-two.slot")
    ;
    
    private final String path;
    
    public <T> T get(DuelsPlugin plugin, Class<T> clazz) {
        return clazz.cast(plugin.getConfig().get(path));
    }
    
    public Location get(DuelsPlugin plugin) {
        return BukkitUtils.deserializeLocation(Objects.requireNonNull(plugin.getConfig().getString(path)));
    }
}
