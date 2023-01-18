package de.crafttogether;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class EggRespawn extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        new Metrics(this, 17459);
        getLogger().info(getDescription().getName() + " v" + getDescription().getVersion() + " enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info(getDescription().getName() + " v" + getDescription().getVersion() + " disabled");
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        final World world = e.getEntity().getLocation().getWorld();

        if (!e.getEntityType().equals(EntityType.ENDER_DRAGON))
            return;

        if (!world.getEnvironment().equals(World.Environment.THE_END))
            return;

        Bukkit.getScheduler().runTaskLater(this, () -> {
            Location loc = new Location(world, 0.0D, 0.0D, 0.0D);
            loc.setY(loc.getWorld().getHighestBlockYAt(0, 0));
            Block highestBlock = world.getBlockAt(loc);

            loc.setY((loc.getWorld().getHighestBlockYAt(0, 0) - 1));
            Block secondHighestBlock = world.getBlockAt(loc);

            if (highestBlock.getType().equals(Material.BEDROCK)) {
                loc.setY((loc.getWorld().getHighestBlockYAt(0, 0) + 1));
                Block topBlock = world.getBlockAt(loc);
                topBlock.setType(Material.DRAGON_EGG);
            } else if (secondHighestBlock.getType().equals(Material.BEDROCK)) {
                highestBlock.setType(Material.DRAGON_EGG);
            } else {
                getLogger().warning("Error: Unable to respawn DragonEgg at world " + loc.getWorld().getName());
                return;
            }

            getLogger().info("DragonEgg respawned at world " + loc.getWorld().getName());
        }, 200L);
    }
}
