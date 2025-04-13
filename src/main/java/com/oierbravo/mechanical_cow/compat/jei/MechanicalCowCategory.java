package com.oierbravo.mechanical_cow.compat.jei;

import com.oierbravo.mechanical_cow.MechanicalCow;
import com.oierbravo.mechanical_cow.content.MechanicalCowConfigUtils;
import com.oierbravo.mechanical_cow.foundation.utility.ModLang;
import com.oierbravo.mechanical_cow.infrastructure.config.MConfigs;
import com.oierbravo.mechanical_cow.registrate.ModBlocks;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MechanicalCowCategory implements IRecipeCategory<MechanicalCowCategory.MechanicalCowRecipe> {

    public final static RecipeType<MechanicalCowRecipe> TYPE = RecipeType.create("mechanical_cow", "production", MechanicalCowRecipe.class);

    //private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotDrawable;

    private AnimatedCow animatedCow = new AnimatedCow();

    @Override
    public int getWidth() {
        return 176;
    }
    public int getHeight() {
        return 40;
    }


    public MechanicalCowCategory(IGuiHelper guiHelper) {

        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MECHANICAL_COW));
        this.slotDrawable = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<MechanicalCowRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return ModLang.translate("recipe").component();
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MechanicalCowRecipe recipe, IFocusGroup iFocusGroup) {
        var input = builder.addSlot(RecipeIngredientRole.INPUT, 4, 15)
                .setBackground(slotDrawable, -1, -1);

        if (recipe.ingredient != null) {
            input.addIngredients(recipe.ingredient)
                    .setBackground(slotDrawable, -1, -1);
        }

        var output = builder.addSlot(RecipeIngredientRole.OUTPUT, 155,15);
        if (recipe.fluid != null) {
            output.addFluidStack(recipe.fluid().getFluid(), recipe.fluid().getAmount())
                    .addRichTooltipCallback(MechanicalCowCategory::addFluidAmountTooltip);
        }

    }

    private static void addFluidAmountTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip){
        Optional<FluidStack> displayed = recipeSlotView.getDisplayedIngredient(NeoForgeTypes.FLUID_STACK);
        if (displayed.isEmpty())
            return;

        FluidStack fluidStack = displayed.get();
        tooltip.add(Component.literal(fluidStack.getAmount() + "mB"));
    }
    @Override
    public void draw(MechanicalCowRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        AllGuiTextures.JEI_ARROW.render(guiGraphics, 30, 16); //Output arrow
        AllGuiTextures.JEI_ARROW.render(guiGraphics, 110, 16); //Output arrow
        animatedCow.draw(guiGraphics, 82, 35);
    }

    public static List<MechanicalCowRecipe> getRecipes() {
        List<MechanicalCowRecipe> recipes = new ArrayList<>();
        recipes.add(new MechanicalCowRecipe(
                MechanicalCowConfigUtils.getRequiredIngredient(),
                MechanicalCowConfigUtils.getOutputFluidStack()
                ));
        return recipes;
    }

    public record MechanicalCowRecipe(Ingredient ingredient,FluidStack fluid) {

    }
}