package de.thejeterlp.BukkitInventoryTweaks.events.replaceItems;

import de.thejeterlp.BukkitInventoryTweaks.utils.Config;
import de.thejeterlp.BukkitInventoryTweaks.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BlockPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (e.isCancelled() || !Config.REPLACE_ITEMS_ON_BLOCK_PLACE.getBoolean() || !p.hasPermission("bit.replaceitems.blockplace"))
            return;
        if (p.getGameMode() == GameMode.CREATIVE && !Config.REPLACE_ITEMS_IN_CREATIVE.getBoolean()) return;
        Block placed = e.getBlockPlaced();
        ItemStack item = new ItemStack(placed.getType(), 1);
        PlayerInventory inv = p.getInventory();

        Utils.debug(e.getClass().getName() + " was fired! " + p.getName() + " Placed " + item);

        ItemStack held = null;
        boolean mainHand = false;

        if (inv.getItemInMainHand().isSimilar(inv.getItemInOffHand())) {
            Utils.debug("Item in Main hand is the same as in off hand! Doing nothing...");
            return;
        }

        if (inv.getItemInMainHand().isSimilar(item)) {
            held = inv.getItemInMainHand();
            mainHand = true;
            Utils.debug("Item in Mainhand is similar to block placed!");
        } else if (inv.getItemInOffHand().isSimilar(item)) {
            held = inv.getItemInOffHand();
            Utils.debug("Item in Offhand is similar to block placed!");
        }

        if (held == null || !held.getType().isBlock()) return;

        Utils.debug("Item held before actually removing: " + held);
        if (held.getAmount() == 1) {
            held = null;
            if (mainHand) {
                inv.setItemInMainHand(new ItemStack(Material.AIR));
                Utils.debug("Held after removing: " + inv.getItemInMainHand());
            } else {
                inv.setItemInOffHand(new ItemStack(Material.AIR));
                Utils.debug("Held after removing: " + inv.getItemInOffHand());
            }
        } else if(held.getAmount() == 0) {
            Utils.debug("Amount of held is already 0, no removing necessary.");
        } else {
            Utils.debug("Amount of held is bigger than 1, no replacing of ItemStack necessary.");
        }


        //Ensure that we dropped everything of that itemstack
        if (held == null || held.getType() == Material.AIR) {
            ItemStack target = Utils.getMatchingItemIfExisting(item, inv);
            if (target == null) {
                Utils.debug("No matching item found in Inventory.");
                return;
            }

            Utils.debug("Setting target to AIR!");
            inv.setItem(inv.first(target), new ItemStack(Material.AIR));

            if (mainHand) {
                Utils.debug("Matching Item found! Replacing mainHand with " + target);
                inv.setItemInMainHand(target);
            } else {
                Utils.debug("Matching Item found! Replacing offHand with " + target);
                inv.setItemInOffHand(target);
            }

            Utils.playSound(p);
        }
    }

}
