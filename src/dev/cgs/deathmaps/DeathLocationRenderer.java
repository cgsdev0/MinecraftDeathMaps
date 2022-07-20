package dev.cgs.deathmaps;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class DeathLocationRenderer extends MapRenderer {
    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (mapCanvas.getCursors().size() == 0) {
            mapCanvas.getCursors().addCursor(new MapCursor((byte)0, (byte)0, (byte)0, MapCursor.Type.RED_X, true));
        }
    }
}
