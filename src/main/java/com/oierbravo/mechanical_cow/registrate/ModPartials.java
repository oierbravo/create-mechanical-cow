package com.oierbravo.mechanical_cow.registrate;

import com.oierbravo.mechanical_cow.ModConstants;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class ModPartials {
    public static final PartialModel COG_HORIZONTAL = block("mechanical_cow/cog");
    public static final PartialModel HEAD = block("mechanical_cow/head");

    private static PartialModel block(String path) {
        return PartialModel.of(ModConstants.asResource("block/" + path));
    }
    private static PartialModel item(String path) {
        return PartialModel.of(ModConstants.asResource("item/" + path));
    }
    public static void init() {
        // init static fields
    }

}
