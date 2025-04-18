package com.oierbravo.mechanical_cow.content;

import com.oierbravo.mechanical_cow.MechanicalCow;
import com.oierbravo.mechanical_cow.ModLang;
import com.oierbravo.mechanical_cow.infrastructure.config.MConfigs;
import com.oierbravo.mechanical_cow.registrate.ModBlockEntities;
import com.oierbravo.mechanicals.compat.jade.IHavePercent;
import com.oierbravo.mechanicals.foundation.blockEntity.behaviour.DynamicCycleBehavior;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class MechanicalCowBlockEntity extends KineticBlockEntity implements DynamicCycleBehavior.DynamicCycleBehaviorSpecifics, IHavePercent {

    private DynamicCycleBehavior cycleBehaviour;
    public SmartFluidTankBehaviour outputTank;
    private Ingredient requiredItemIngredient;
    private int requiredIngredientAmount;

    private FluidStack outputFluid;

    public final ItemStackHandler inputInventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    };
    private final Lazy<IItemHandler> itemCapability = Lazy.of(() -> inputInventory);

    public MechanicalCowBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        verifyConfig(MechanicalCow.LOGGER);
    }

    @Override
    public int getProcessingTime() {
        return MConfigs.server().mechanicalCow.processingTime.get();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        outputTank = SmartFluidTankBehaviour.single(this, MConfigs.server().mechanicalCow.fluidCapacity.get());
        behaviours.add(outputTank);
        

        cycleBehaviour = new DynamicCycleBehavior(this);
        behaviours.add(cycleBehaviour);
    }

    @Override
    public void remove() {
        super.remove();
    }

   @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.MECHANICAL_COW.get(),
                (be, context) -> {
                    Direction localDir = be.getBlockState().getValue(MechanicalCowBlock.HORIZONTAL_FACING);
                    if(context != null && localDir == context)
                        return be.getItemHandler();
                    if(context == null)
                        return be.getItemHandler();
                    return null;
                }
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.MECHANICAL_COW.get(),
                (be, context) -> {
                    Direction localDir = be.getBlockState().getValue(MechanicalCowBlock.HORIZONTAL_FACING);
                    if(context != null && localDir == context.getOpposite())
                        return be.outputTank.getCapability();
                    if(context == null)
                        return be.outputTank.getCapability();
                    return null;
                }
        );
    }

    private @Nullable IItemHandler getItemHandler() {
        return inputInventory;
    }

    public DynamicCycleBehavior getCycleBehaviour() {
        return cycleBehaviour;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.put("InputInventory", inputInventory.serializeNBT(registries));
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        inputInventory.deserializeNBT(registries, compound.getCompound("InputInventory"));
        super.read(compound, registries, clientPacket);
    }


    @Override
    public float getKineticSpeed() {
        return getSpeed();
    }

    @Override
    public boolean tryProcess(boolean simulate) {
        if(inputInventory.getStackInSlot(0).isEmpty())
            return false;
        if(!requiredItemIngredient.test(inputInventory.getStackInSlot(0)))
            return false;
        int a = inputInventory.extractItem(0,requiredIngredientAmount,true).getCount();
        if(inputInventory.extractItem(0,requiredIngredientAmount,true).getCount() != requiredIngredientAmount){
            return false;
        }
        if(outputTank.getPrimaryHandler().fill(outputFluid, IFluidHandler.FluidAction.SIMULATE) == 0){
            return false;
        }

        if(simulate)
            return true;
        inputInventory.extractItem(0,requiredIngredientAmount,false);
        outputTank.getPrimaryHandler().fill(outputFluid, IFluidHandler.FluidAction.EXECUTE);
        return true;
    }

    @Override
    public void playCompletionSound() {
        level.playSound((Entity) null, worldPosition, SoundEvents.COW_MILK, SoundSource.BLOCKS, MConfigs.server().mechanicalCow.soundVolume.get().floatValue(),1.0f);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if( this.cycleBehaviour.isRunning()) {
            ModLang.translate("mechanical_cow.tooltip.progress", getProgressPercent()).style(ChatFormatting.YELLOW).forGoggles(tooltip);
            added = true;
        }
        return added;
    }

    @Override
    public int getProgressPercent() {
        return this.cycleBehaviour.getProgressPercent();
    }


    public void verifyConfig(final Logger logger) {
        requiredIngredientAmount = MConfigs.server().mechanicalCow.requiredIngredientAmount.get();
        if (requiredItemIngredient == null) {
            // verify and set the configured ingredient
            requiredItemIngredient = MechanicalCowConfigUtils.getRequiredIngredient();
        }

        if( outputFluid == null){
            outputFluid = MechanicalCowConfigUtils.getOutputFluidStack();
        }
    }

}
