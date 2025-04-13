package com.oierbravo.mechanical_cow.registrate;

import com.oierbravo.mechanical_cow.ModConstants;
import com.oierbravo.mechanical_cow.content.MechanicalCowBlock;
import com.oierbravo.mechanical_cow.infrastructure.config.ModStress;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;

import static com.oierbravo.mechanical_cow.MechanicalCow.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ModBlocks {
    public static final BlockEntry<MechanicalCowBlock> MECHANICAL_COW = REGISTRATE.block("mechanical_cow", MechanicalCowBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.METAL))
            .transform(pickaxeOnly())
            .transform(ModStress.setImpact(4.0))
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
                    .define('S',AllBlocks.SHAFT)
                    .define('V', AllBlocks.FLUID_TANK)
                    .define('C', AllBlocks.COGWHEEL)
                    .define('E', Tags.Items.CROPS_WHEAT)
                    .define('B', AllItems.BRASS_SHEET)
                    .pattern(" C ")
                    .pattern("EVE")
                    .pattern("BSB")
                    .unlockedBy("has_brass_sheet",RegistrateRecipeProvider.has(AllTags.AllItemTags.CASING.tag))
                    .save(p, ModConstants.asResource("crafting/" + c.getName())))
            .item()
            .transform(customItemModel())
            .register();

    public static void register() {}
}
