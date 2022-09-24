/*
 * This file is part of ChatEx
 * Copyright (C) 2022 ChatEx Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package de.thejeterlp.BukkitInventoryTweaks.utils;

import de.thejeterlp.BukkitInventoryTweaks.BukkitInventoryTweaks;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public enum Config {

    CHECK_UPDATE("check-for-updates", true, "Should the plugin check for updates by itself?"),
    B_STATS("enable-bstats", true, "Do you want to use bstats?"),
    DEBUG("debug", false, "Should the debug log be enabled?"),
    //LOCALE("locale", "en-EN", "Which language do you want?"),
    REPLACE_ITEMS_IN_CREATIVE("ReplaceItems.replaceInCreative", false, "Should the items be replaced in creative mode too?"),
    REPLACE_ITEMS_PLAY_SOUND("ReplaceItems.playSound", true, "Should a sound be played to the player after the item was replaced?"),
    REPLACE_ITEMS_EXACT("ReplaceItems.needsToMatchExactly", false, "Does the item to replace need to match exactly? (For ex. should a woodenpickaxe be replaced by a diamond picakxe)"),
    REPLACE_ITEMS_ON_BREAK("ReplaceItems.onBreak", true, "Should the item be replaced after it breaks?"),
    REPLACE_ITEMS_ON_CONSUME("ReplaceItems.onConsume", true, "Should the item be replaced when its consumed?"),
    REPLACE_ITEMS_ON_DROP("ReplaceItems.onDrop", false, "Should the item be replaced after it has been dropped by the player?"),
    REPLACE_ITEMS_ON_BLOCK_PLACE("ReplaceItems.onBlockPlace", true, "Should the item be replaced after a Block was placed?"),
    SWORD_LIST("ItemLists.Swords", List.of(
            Material.WOODEN_SWORD.name(),
            Material.STONE_SWORD.name(),
            Material.GOLDEN_SWORD.name(),
            Material.IRON_SWORD.name(),
            Material.DIAMOND_SWORD.name(),
            Material.NETHERITE_SWORD.name()),
            "All the swords that can be replaced with each other"),
    PICKAXE_LIST("ItemLists.Pickaxes", List.of(
            Material.WOODEN_PICKAXE.name(),
            Material.STONE_PICKAXE.name(),
            Material.GOLDEN_PICKAXE.name(),
            Material.IRON_PICKAXE.name(),
            Material.DIAMOND_PICKAXE.name(),
            Material.NETHERITE_PICKAXE.name()
    ), "All the pickaxes that can be replaced with each other"),
    SHOVEL_LIST("ItemLists.Shovels", List.of(
            Material.WOODEN_SHOVEL.name(),
            Material.STONE_SHOVEL.name(),
            Material.GOLDEN_SHOVEL.name(),
            Material.IRON_SHOVEL.name(),
            Material.DIAMOND_SHOVEL.name(),
            Material.NETHERITE_SHOVEL.name()
    ), "All the shovels that can be replaced with each other"),
    AXE_LIST("ItemLists.Axe", List.of(
            Material.WOODEN_AXE.name(),
            Material.STONE_AXE.name(),
            Material.GOLDEN_AXE.name(),
            Material.IRON_AXE.name(),
            Material.DIAMOND_AXE.name(),
            Material.NETHERITE_AXE.name()
    ), "All the axes that can be replaced with each other"),
    HOE_LIST("ItemLists.Hoe", List.of(
            Material.WOODEN_HOE.name(),
            Material.STONE_HOE.name(),
            Material.GOLDEN_HOE.name(),
            Material.IRON_HOE.name(),
            Material.DIAMOND_HOE.name(),
            Material.NETHERITE_HOE.name()
    ), "All the hoes that can be replaced with each other"),
    FOOD_LIST("ItemList.Food", ItemGroups.getEdibles(), "All the food that can be replaced with each other");

    private final Object value;
    private final String path;
    private final String description;
    private static YamlConfiguration cfg;
    private static final File f = new File(BukkitInventoryTweaks.getInstance().getDataFolder(), "config.yml");

    Config(String path, Object val, String description) {
        this.path = path;
        this.value = val;
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    public Object getDefaultValue() {
        return value;
    }

    public boolean getBoolean() {
        return cfg.getBoolean(path);
    }

    public double getDouble() {
        return cfg.getDouble(path);
    }

    public int getInt() {
        return cfg.getInt(path);
    }

    public String getString() {
        return cfg.getString(path);
    }

    public List<String> getStringList() {
        return cfg.getStringList(path);
    }

    public static ConfigurationSection getConfigurationSection(String path) {
        return cfg.getConfigurationSection(path);
    }

    public static void load() {
        BukkitInventoryTweaks.getInstance().getDataFolder().mkdirs();
        reload(false);
        List<String> header = new ArrayList<>();
        for (Config c : values()) {
            header.add(c.getPath() + ": " + c.getDescription());
            if (!cfg.contains(c.getPath())) {
                c.set(c.getDefaultValue(), false);
            }
        }
        cfg.options().setHeader(header);
        try {
            cfg.save(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void set(Object value, boolean save) {
        cfg.set(path, value);
        if (save) {
            try {
                cfg.save(f);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            reload(false);
        }
    }

    public static void reload(boolean complete) {
        if (!complete) {
            cfg = YamlConfiguration.loadConfiguration(f);
            return;
        }
        load();
    }
}
