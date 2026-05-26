package games.voided.voidaesp.core.view;

import games.voided.locatables.BlockLocatable;

// Used to cache visibility changes until the player's netty thread next processes them.
public record BlockViewTransition(Type type, BlockLocatable location) {
    public enum Type {
        SHOW,
        HIDE,
    }
}
