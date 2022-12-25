package de.thejeterlp.BukkitInventoryTweaks.events.replaceItems;

import de.thejeterlp.BukkitInventoryTweaks.utils.Config;
import de.thejeterlp.BukkitInventoryTweaks.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class DropItemListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (e.isCancelled() || !Config.REPLACE_ITEMS_ON_DROP.getBoolean() || !p.hasPermission("bit.replaceitems.itemdropped")) return;
        if (p.getGameMode() == GameMode.CREATIVE && !Config.REPLACE_ITEMS_IN_CREATIVE.getBoolean()) return;
        ItemStack item = e.getItemDrop().getItemStack();
        PlayerInventory inv = p.getInventory();

        Utils.debug(e.getClass().getName() + " was fired! " + p.getName() + " dropped " + item);

        ItemStack held = inv.getItemInMainHand();

        //Ensure that we dropped everything of that itemstack
        if (held == null || held.getType() == Material.AIR) {
            ItemStack target = Utils.getMatchingItemIfExisting(item, inv);
            if (target == null) return;
            inv.setItem(inv.first(target), new ItemStack(Material.AIR));
            inv.setItemInMainHand(target);
            Utils.playSound(p);
            Utils.debug("Matching Item found! Replacing with " + target);
        }
    }

}
