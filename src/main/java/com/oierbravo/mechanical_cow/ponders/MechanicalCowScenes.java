package com.oierbravo.mechanical_cow.ponders;

import com.oierbravo.mechanical_cow.content.MechanicalCowBlock;
import com.oierbravo.mechanical_cow.content.MechanicalCowBlockEntity;
import com.oierbravo.mechanical_cow.content.MechanicalCowConfigUtils;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class MechanicalCowScenes {
    public static void mechanicalCow(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("mechanical_cow", "Mechanical cow");
        scene.configureBasePlate(0, 0, 5);
        //scene.showBasePlate();

        BlockPos cowPos = util.grid().at(2, 2, 3);
        Selection cowSelection = util.select().position(2, 2, 3);
        Selection cowShaftSelection = util.select().fromTo(2, 0, 3,2, 1, 3);

        scene.world().showSection(util.select().layer(0).substract(cowShaftSelection), Direction.DOWN);
        scene.idle(10);

        scene.world().modifyBlock(cowPos, s -> s.setValue(MechanicalCowBlock.HORIZONTAL_FACING, Direction.NORTH), false);

        scene.world().showSection(cowSelection,Direction.UP);

        scene.addKeyframe();
        scene.idle(10);



        scene.overlay().showOutlineWithText(cowSelection, 60)
                .text("The Mechanical Chicken uses rotational force and an specific fluid to generate eggs")
                .pointAt(util.vector().blockSurface(cowPos, Direction.WEST)
                        //.add(-.5, .4, 0)
                        )
                .placeNearTarget();
        scene.idle(70);

        scene.world().showSection(cowShaftSelection,Direction.UP);


        scene.world().setKineticSpeed(cowSelection, 32);
        scene.world().setKineticSpeed(cowShaftSelection, 32);
        scene.effects().indicateSuccess(cowPos);
        scene.effects().indicateSuccess(cowPos.below());
        scene.effects().indicateSuccess(cowPos.below(2));


        scene.overlay().showText(60)
                .attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Its powered from the bottom")
                .pointAt(util.vector().centerOf(cowPos.below()))
                .placeNearTarget();
        scene.idle(70);

        //Vec3 millstoneTop = util.vector().topOf(millstone);
        //scene.addKeyframe();

        Selection itemInputSelection = util.select().fromTo(2, 1, 0, 2,2,2);
        scene.world().setKineticSpeed(itemInputSelection,32);
        scene.world().showSection(itemInputSelection,Direction.WEST);

        scene.overlay().showText(60)
                .attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Item input can ONLY go from the FRONT side")
                .pointAt(util.vector().centerOf(cowPos.north()))
                .placeNearTarget();
        scene.idle(70);

        scene.overlay().showText(25)
                .attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Can only be inserted by automation")
                .pointAt(util.vector().centerOf(cowPos.north()))
                .placeNearTarget();
        scene.idle(30);

        BlockPos beltStartPos = util.grid().at(2, 1, 0);
        ItemStack wheatItemStack = new ItemStack(Items.WHEAT,4);
        scene.world().createItemOnBeltLike(beltStartPos, Direction.UP, wheatItemStack);

        scene.idle(40);
        BlockPos beltEndPos = util.grid().at(2, 1, 2);
        scene.world().removeItemsFromBelt(beltEndPos);


        Class<MechanicalCowBlockEntity> type = MechanicalCowBlockEntity.class;
        scene.world().modifyBlockEntity(cowPos, type, pte -> pte.getCycleBehaviour()
                .start());

        scene.idle(70);



        FluidStack outputFluidStack = MechanicalCowConfigUtils.getOutputFluidStack();
        outputFluidStack.setAmount(1000);
        scene.world().modifyBlockEntity(cowPos, MechanicalCowBlockEntity.class,
                ms -> ms.outputTank.getPrimaryHandler().fill(outputFluidStack, IFluidHandler.FluidAction.EXECUTE));

        scene.overlay().showText(45)
                .attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Milk output is ONLY located on the BACK side")
                .pointAt(util.vector().centerOf(cowPos.south()))
                .placeNearTarget();
        scene.idle(55);


        scene.overlay().showText(45)
                .attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Can only be extracted by automation")
                .pointAt(util.vector().centerOf(cowPos.south()))
                .placeNearTarget();
        scene.idle(55);

        Selection outputPumpSelection = util.select().fromTo(0, 2, 4, 2,2,4);
        scene.world().showSection(outputPumpSelection,Direction.EAST);

        BlockPos cowPumpPos = util.grid().at(0, 2, 4);
        scene.world().setKineticSpeed(outputPumpSelection,32);
        scene.world().propagatePipeChange(cowPumpPos);


        scene.idle(60);

        scene.markAsFinished();

    }
}
