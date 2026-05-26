package games.voided.voidaesp.core.raycast;

import games.voided.locatables.Locatable;

public interface ParticleSpawner {

    enum Colour {
        RED, GREEN, BLUE,
    }

    void spawnParticleAt(Locatable locatable, Colour color);
}
