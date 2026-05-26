package games.voided.voidaesp.core.locatables;

import games.voided.locatables.BlockLocatable;
import games.voided.locatables.ImmutableLocatable;
import games.voided.locatables.MutableLocatable;
import games.voided.voidaesp.core.utils.Clearable;

public interface TileEntityLocatable<T> extends BlockLocatable, ImmutableLocatable, Clearable {
    boolean visible();
    TileEntityLocatable<T> setVisible(boolean visible);

    int lastChecked();
    TileEntityLocatable<T> setLastChecked(int lastChecked);

    int blockID();

    T extraData();
    TileEntityLocatable<T> setExtraData(T extraData);

    @Override
    default boolean isMutable() {
        return false;
    }

    @Override
    default MutableLocatable castToMutableOrNull() {
        return null;
    }
}
