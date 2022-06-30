package it.matty.duels.duel.manager;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.matty.duels.DuelsPlugin;
import it.matty.duels.Service;
import it.matty.duels.config.ConfigSettings;
import it.matty.duels.config.Language;
import it.matty.duels.duel.Duel;
import it.matty.duels.duel.DuelStatus;
import it.matty.duels.utils.BukkitUtils;
import it.matty.duels.utils.Placeholder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Getter
public class DuelManager implements Service {
    private static final DateTimeFormatter DATE_FORMAT;
    
    static {
        DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
    }
    
    private final DuelsPlugin plugin;
    private final Map<String, Duel> duels = Maps.newHashMap();
    @Setter
    private Duel currentDuel = null;
    
    @Override
    @SuppressWarnings("all")
    public void enable() {
        clear();
        ConfigurationSection generalSection = Objects.requireNonNull(
                plugin.getConfig().getConfigurationSection("events"));
        
        for (String key : generalSection.getKeys(false)) {
            ConfigurationSection section = Objects.requireNonNull(generalSection.getConfigurationSection(key));
            
            long startingTime = LocalDateTime.parse(section.getString("date"), DATE_FORMAT)
                    .toInstant(ZoneOffset.UTC)
                    .toEpochMilli();
            if (startingTime < System.currentTimeMillis()) continue;
            
            List<String> commands = section.getStringList("rewards");
            boolean bet = section.getBoolean("can-bet");
            
            add(key, new Duel(startingTime, commands, bet));
        }
    }
    
    @Override
    public void disable() {
        clear();
    }
    
    public void add(String identifier, Duel duel) {
        duels.put(identifier, duel);
    }
    
    public void clear() {
        duels.clear();
    }
    
    public boolean isDuelActionable(Duel duel) {
        long time = (duel.getStartingTime() - System.currentTimeMillis()) / 1000;
        
        return time <= 0;
    }
    
    public void startDuel(Duel duel) {
        BukkitUtils.sendMessageToAll(plugin, Language.DUEL_STARTED_MESSAGE);
        duel.setStatus(DuelStatus.WAITING);
    }
    
    public boolean hasEnoughPlayers(Duel duel) {
        return duel.getPlayingPlayers().isEmpty() ||
                duel.getPlayingPlayers().size() >= ConfigSettings.MIN_PLAYERS.get(plugin, Integer.class);
    }
    
    public boolean hasReachedMaxPlayers(Duel duel) {
        return duel.getPlayingPlayers().isEmpty() ||
                duel.getPlayingPlayers().size() == ConfigSettings.MIN_PLAYERS.get(plugin, Integer.class);
    }
    
    public void pickRandomFighters(Duel duel) {
        List<UUID> fighters = duel.getFighting();
        List<UUID> all = duel.getPlayingPlayers();
        
        for (int i = 0; i < 2; i++) {
            int random = ThreadLocalRandom.current().nextInt(duel.getPlayingPlayers().size());
            UUID uuid = duel.getPlayingPlayers().get(random);
            
            if (duel.getFighting().contains(uuid)) continue;
            fighters.add(uuid);
            all.removeAll(fighters);
        }
    }
    
    public void greetBetters(Duel duel, UUID winner) {
        duel.getBetters().forEach((uuid, prediction) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (!prediction.getWhoBet().equals(winner)) {
                Language.LOSE_PLAYER_BET.send(plugin, player,
                        new Placeholder("player", Bukkit.getPlayer(prediction.getWhoBet()).getName()),
                        new Placeholder("money", prediction.getMoney().doubleValue() + ""));
                return;
            }
    
            BigDecimal money = prediction.getMoney().multiply(new BigDecimal(2));
            plugin.getEconomy().depositPlayer(player, money.doubleValue());
            Language.GREET_PLAYER_BET.send(plugin, player,
                    new Placeholder("player", Bukkit.getPlayer(prediction.getWhoBet()).getName()),
                    new Placeholder("money", money.doubleValue() + ""));
        });
    }
    
    public void handleFighters(List<UUID> fighters) {
        fighters.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) return;
            
            player.teleport(ConfigSettings.LOCATION_SPAWN_WAITING.get(plugin));
            fighters.remove(uuid);
        });
    }
}
