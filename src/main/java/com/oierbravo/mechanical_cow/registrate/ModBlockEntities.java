package com.oierbravo.mechanical_cow.registrate;


import com.oierbravo.mechanical_cow.MechanicalCow;
import com.oierbravo.mechanical_cow.content.MechanicalCowBlockEntity;
import com.oierbravo.mechanical_cow.content.MechanicalCowRenderer;
import com.oierbravo.mechanical_cow.content.MechanicalCowVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
public class ModBlockEntities {

    public static final BlockEntityEntry<MechanicalCowBlockEntity> MECHANICAL_COW = MechanicalCow.registrate()
            .blockEntity("mechanical_chicken_block_entity", MechanicalCowBlockEntity::new)
            .visual(() -> MechanicalCowVisual::new)
            .renderer(() -> MechanicalCowRenderer::new)
            .validBlocks(ModBlocks.MECHANICAL_COW)
            .register();

    public static void register() {}
}