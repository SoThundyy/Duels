package it.matty.duels.duel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.matty.duels.prediction.Prediction;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class Duel {
    private final Long startingTime;
    private final List<String> commands;
    private final boolean bet;
    private final Map<UUID, PlayerStatus> playing = Maps.newHashMap();
    private final Map<UUID, Prediction> betters = Maps.newHashMap();
    private final List<UUID> fighting = Lists.newArrayListWithCapacity(2);
    private DuelStatus status;
    
    public boolean isStarted() {
        return status == DuelStatus.PLAYING;
    }
    
    public boolean isPlaying(Player player) {
        return playing.containsKey(player.getUniqueId());
    }
    
    public List<UUID> getSpectators() {
        return playing.keySet().stream()
                .filter(uuid -> playing.get(uuid) == PlayerStatus.SPECTATOR)
                .collect(Collectors.toList());
    }
    
    public List<UUID> getPlayingPlayers() {
        return playing.keySet().stream()
                .filter(uuid -> playing.get(uuid) == PlayerStatus.PLAYING)
                .collect(Collectors.toList());
    }
    
    public boolean hasFighters() {
        return fighting.size() != 0;
    }
}
