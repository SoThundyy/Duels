package it.matty.duels.utils;

import it.matty.duels.DuelsPlugin;
import it.matty.duels.config.ConfigSettings;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class InventoryUtils {
    
    public Inventory getBetInventory(DuelsPlugin plugin, List<UUID> fighters) {
        Inventory inventory = Bukkit.createInventory(null, ConfigSettings.BET_GUI_SIZE.get(plugin, Integer.class),
                "BET INVENTORY");
        Player first = Bukkit.getPlayer(fighters.get(0));
        Player second = Bukkit.getPlayer(fighters.get(1));
        ItemStack firstItem = new ItemStack(Material.SKULL_ITEM);
        SkullMeta firstMeta = (SkullMeta) firstItem.getItemMeta();
        firstMeta.setOwner(first.getName());
        firstMeta.setDisplayName(first.getName());
        firstItem.setItemMeta(firstMeta);
        ItemStack secondItem = new ItemStack(Material.SKULL_ITEM);
        SkullMeta secondMeta = (SkullMeta) firstItem.getItemMeta();
        firstMeta.setOwner(second.getName());
        firstMeta.setDisplayName(second.getName());
        secondItem.setItemMeta(secondMeta);
        
        inventory.setItem(ConfigSettings.FIRST_FIGHTER_SLOT.get(plugin, Integer.class), firstItem);
        inventory.setItem(ConfigSettings.SECOND_FIGHTER_SLOT.get(plugin, Integer.class), secondItem);
        return inventory;
    }
    
    public ItemStack getBetItem() {
        ItemStack itemStack = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatUtils.textColor("&c&lVOTA!"));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
    public boolean isBetItem(Player player) {
        return player.getItemInHand().getType() == Material.BLAZE_POWDER &&
                player.getItemInHand().hasItemMeta() &&
                player.getItemInHand().getItemMeta().hasDisplayName() &&
                player.getItemInHand().getItemMeta().getDisplayName().equals(ChatUtils.textColor("&c&lVOTA!"));
    }
}
