package games.voided.voidaesp.packetevents.view;

import games.voided.locatables.BlockLocatable;
import games.voided.locatables.implementations.ImmutableBlockLocatable;
import games.voided.voidaesp.core.view.AbstractBlockView;
import games.voided.voidaesp.packetevents.locatables.PacketEventsTileEntity;

import java.util.UUID;

public class PacketEventsBlockView extends AbstractBlockView<PacketEventsTileEntity> {
    @Override
    protected PacketEventsTileEntity createTrackedTileEntity(BlockLocatable location, int blockID, boolean visible) {
        return new PacketEventsTileEntity(location, visible, 0, blockID);
    }

    @Override
    protected PacketEventsTileEntity createTrackedTileEntity(UUID world, int x, int y, int z, int blockID) {
        return new PacketEventsTileEntity(world, x, y, z, false, blockID);
    }
}
