package dev.cgs.deathmaps;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DeathMaps extends JavaPlugin implements Listener {

    @EventHandler
    public void onCraft(SmithItemEvent e) {
        if(e.getCurrentItem().getType() != Material.FILLED_MAP) {
            return;
        }
        ItemMeta meta = e.getCurrentItem().getItemMeta();
        if (!(meta instanceof MapMeta)) {
            return;
        }
        MapMeta mapMeta = (MapMeta)meta;
        mapMeta.getMapView().setUnlimitedTracking(true);
        e.getCurrentItem().setItemMeta(mapMeta);
    }
    @EventHandler
    public void onDeath(PlayerRespawnEvent e) {
        if (!Arrays.stream(e.getPlayer().getInventory().getContents()).allMatch(Objects::isNull)
                || e.getPlayer().getExp() > 0
                || e.getPlayer().getStatistic(Statistic.TIME_SINCE_DEATH) > 300) {
            // They're probably coming back from the End dimension in this case
            return;
        }
        Location deathLocation = e.getPlayer().getLastDeathLocation();
        if(deathLocation == null) {
            return;
        }
        ItemStack mapStack = new ItemStack(Material.FILLED_MAP);
        MapView mapView = Bukkit.createMap(deathLocation.getWorld());
        mapView.setCenterX(deathLocation.getBlockX());
        mapView.setCenterZ(deathLocation.getBlockZ());
        mapView.setUnlimitedTracking(true);
        mapView.setTrackingPosition(true);
        mapView.addRenderer(new DeathLocationRenderer());
        MapMeta mapMeta = (MapMeta)mapStack.getItemMeta();
        mapMeta.setMapView(mapView);
        mapMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.RED + e.getPlayer().getDisplayName() + "'s Death Map");
        mapStack.setItemMeta(mapMeta);
        e.getPlayer().getInventory().setItemInMainHand(mapStack);
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("DeathMaps is now enabled!");
    }

    @Override
    public void onDisable(){
        this.getLogger().info("DeathMaps is now disabled.");
    }
}
