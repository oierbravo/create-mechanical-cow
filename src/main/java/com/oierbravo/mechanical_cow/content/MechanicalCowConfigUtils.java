package com.oierbravo.mechanical_cow.content;

import com.oierbravo.mechanical_cow.MechanicalCow;
import com.oierbravo.mechanical_cow.infrastructure.config.MConfigs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;

public class MechanicalCowConfigUtils {
    public static Ingredient getRequiredIngredient(){
        Ingredient requiredItemIngredient = Ingredient.EMPTY;
        final String itemResourceRaw = MConfigs.server().mechanicalCow.requiredIngredient.get();
        if(itemResourceRaw.startsWith("#")){
            ResourceLocation itemTag = ResourceLocation.tryParse(itemResourceRaw.replace("#",""));
            assert itemTag != null;
            return Ingredient.of(ItemTags.create(itemTag));
        }
        final ResourceLocation desiredItem = ResourceLocation.parse(itemResourceRaw);

        if (BuiltInRegistries.ITEM.containsKey(desiredItem)) {
            requiredItemIngredient = Ingredient.of(BuiltInRegistries.ITEM.get(desiredItem));
        } else {
            MechanicalCow.LOGGER.error("Unknown item '{}' in config, using default '{}' instead", itemResourceRaw, "minecraft:wheat");
            requiredItemIngredient = Ingredient.of(Tags.Items.CROPS_WHEAT);
        }
        return requiredItemIngredient;
    }
    public static FluidStack getOutputFluidStack(){
        FluidStack outputFluid = FluidStack.EMPTY;
        int outputAmount = MConfigs.server().mechanicalCow.outputAmount.get();

        final String fluidResource = MConfigs.server().mechanicalCow.outputFluid.get();
        final ResourceLocation desiredFluid = ResourceLocation.parse(fluidResource);

        if (BuiltInRegistries.FLUID.containsKey(desiredFluid)) {
            outputFluid = new FluidStack(BuiltInRegistries.FLUID.get(desiredFluid),outputAmount);
        } else {
            MechanicalCow.LOGGER.error("Unknown fluid '{}' in config, using default '{}' instead", fluidResource, "minecraft:milk");
            outputFluid = new FluidStack(BuiltInRegistries.FLUID.get(ResourceLocation.parse("minecraft:milk")),outputAmount);
        }
        return outputFluid;
    }
}
