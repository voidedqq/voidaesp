package games.voided.voidaesp.paper.utils;

import org.bukkit.Location;
import java.util.UUID;

public record EntityLocationPair(UUID entity, Location loc, double offset) {
}
