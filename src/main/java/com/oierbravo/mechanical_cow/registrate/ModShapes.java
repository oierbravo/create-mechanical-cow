package com.oierbravo.mechanical_cow.registrate;

import com.simibubi.create.AllShapes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModShapes {
    public static final VoxelShape MECHANICAL_COW =
            shape(1, 0, 1, 15, 4, 15)
                    .add(3,4,2,14,12,14)
                    .add(4,14,4,12,18,12)
                    .build();
    private static AllShapes.Builder shape(VoxelShape shape) {
        return new AllShapes.Builder(shape);
    }
    private static AllShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }

    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }
}
