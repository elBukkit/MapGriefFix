package com.elmakers.mine.bukkit.plugins;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MapGriefFix extends JavaPlugin implements Listener {
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);

        // Check preloaded chunks
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                checkChunk(chunk);
            }
        }
    }

    private void checkChunk(Chunk chunk) {
        Entity[] entities = chunk.getEntities();
        for (Entity entity : entities) {
            if (entity instanceof ItemFrame) {
                ItemFrame itemFrame = (ItemFrame)entity;
                ItemStack item = itemFrame.getItem();
                if (item.getType() == Material.MAP) {
                    // TODO: Ok so turns out detecting these are invalid is pretty hard.
                    // Also this issue appears fixed in recent Spigot builds.
                    // So, whatever, remove them all.
                    itemFrame.setItem(null);
                    getLogger().info("Removed map from item frame at " + itemFrame.getLocation().getWorld().getName() + ": " +
                        itemFrame.getLocation().getBlockX() + "," + itemFrame.getLocation().getBlockY() + "," + itemFrame.getLocation().getBlockZ());
                }
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        checkChunk(event.getChunk());
    }
}
