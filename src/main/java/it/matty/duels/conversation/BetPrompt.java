package it.matty.duels.conversation;

import it.matty.duels.DuelsPlugin;
import it.matty.duels.duel.Duel;
import it.matty.duels.duel.manager.DuelManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class BetPrompt implements Prompt {
    private final DuelsPlugin plugin;
    private final DuelManager duelManager;
    private final Player selected;
    
    @Override
    public String getPromptText(ConversationContext context) {
        return "Inserisci il valore da bettare!";
    }
    
    @Override
    public boolean blocksForInput(ConversationContext context) {
        return false;
    }
    
    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Duel duel = duelManager.getCurrentDuel();
        if (duel == null || !duel.isStarted()) return END_OF_CONVERSATION;
        if (!duel.hasFighters()) return END_OF_CONVERSATION;
        if (!NumberUtils.isNumber(input)) {
            context.getForWhom().sendRawMessage("Devi inserire un valore numerico!");
            return END_OF_CONVERSATION;
        }
        double money = Double.parseDouble(input);
        Player from = (Player) context.getForWhom();
        
        if (!plugin.getEconomy().has(from, money)) {
            from.sendMessage("Non hai abbastanza soldi!");
            return END_OF_CONVERSATION;
        }
        
        plugin.getEconomy().withdrawPlayer(from, money);
        context.setSessionData("bet_value", money);
        context.setSessionData("bet_player", selected.getName());
        return this;
    }
}
