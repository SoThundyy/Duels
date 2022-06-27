package it.matty.duels.listeners;

import it.matty.duels.DuelsPlugin;
import it.matty.duels.conversation.BetPrompt;
import it.matty.duels.duel.Duel;
import it.matty.duels.duel.manager.DuelManager;
import it.matty.duels.utils.BukkitUtils;
import it.matty.duels.utils.InventoryUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class BetListener implements Listener {
    private final DuelsPlugin plugin;
    private final DuelManager duelManager;
    
    @EventHandler
    public void onBetItemInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
    
        Duel duel = duelManager.getCurrentDuel();
        if (event.getAction() != Action.RIGHT_CLICK_AIR ||
                event.getAction() != Action.RIGHT_CLICK_BLOCK ||
                !InventoryUtils.isBetItem(player)) return;
        
        if (duel == null || !duel.isStarted() || !duel.hasFighters()) return;
        
        player.openInventory(InventoryUtils.getBetInventory(plugin, duel.getFighting()));
    }
    
    @EventHandler
    public void onBetInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        
        
        if (event.getClickedInventory() == null ||
                !event.getInventory().getTitle().equals("BET INVENTORY")) return;
    
        switch (event.getSlot()) {
            case 12:
            
        }
    }
}
