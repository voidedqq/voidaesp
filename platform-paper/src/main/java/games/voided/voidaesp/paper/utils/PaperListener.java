package games.voided.voidaesp.paper.utils;

import games.voided.voidaesp.paper.VoidAESP;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class PaperListener implements Listener {
    public PaperListener() {
        Bukkit.getPluginManager().registerEvents(this, VoidAESP.get());
    }
}
