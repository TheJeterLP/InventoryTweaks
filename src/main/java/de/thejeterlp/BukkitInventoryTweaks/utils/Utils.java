package de.thejeterlp.BukkitInventoryTweaks.utils;

import de.thejeterlp.BukkitInventoryTweaks.BukkitInventoryTweaks;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static void debug(String msg) {
        if (Config.DEBUG.getBoolean()) {
            BukkitInventoryTweaks.getInstance().getLogger().info(msg);
        }
    }

    public static ItemStack getMatchingItemIfExisting(ItemStack target, Inventory inv) {
        Material m = target.getType();

        //First check if exact same item is existing in INV
        if (inv.contains(m, 1)) {
            for (ItemStack stack : inv.getContents()) {
                if (stack == null || stack.getType() != m) continue;
                //Ensure stack is the same material as the broken item here
                return stack;
            }
        }

        //Now check if similar item is existing
        if (!Config.REPLACE_ITEMS_EXACT.getBoolean()) {
            //Exact matches are disabled, we can also switch other items.

            for (ItemStack stack : inv.getContents()) {
                if (stack == null || stack.getType() == Material.AIR) continue;
                Material sMat = stack.getType();
                if (ItemGroups.isFood(m) && ItemGroups.isFood(sMat)) {
                    return stack;
                } else if (ItemGroups.isSword(m) && ItemGroups.isSword(sMat)) {
                    return stack;
                } else if (ItemGroups.isPickaxe(m) && ItemGroups.isPickaxe(sMat)) {
                    return stack;
                } else if (ItemGroups.isShovel(m) && ItemGroups.isShovel(sMat)) {
                    return stack;
                } else if (ItemGroups.isAxe(m) && ItemGroups.isAxe(sMat)) {
                    return stack;
                } else if (ItemGroups.isHoe(m) && ItemGroups.isHoe(sMat)) {
                    return stack;
                } else {
                    continue;
                }

            }


        }


        return null;
    }

    public static void playSound(Player p) {
        if (Config.REPLACE_ITEMS_PLAY_SOUND.getBoolean()) {
            p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 10, 10);
        }
    }

}
