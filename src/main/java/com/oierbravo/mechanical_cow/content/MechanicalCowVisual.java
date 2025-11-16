package com.oierbravo.mechanical_cow.content;

import com.mojang.math.Axis;
import com.oierbravo.mechanical_cow.registrate.ModPartials;
import com.oierbravo.mechanicals.foundation.blockEntity.behaviour.DynamicCycleBehavior;
import com.oierbravo.mechanicals.foundation.visual.HalfShaftVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlock;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;

import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class MechanicalCowVisual extends HalfShaftVisual<MechanicalCowBlockEntity> implements SimpleDynamicVisual {
    private final OrientedInstance cowHead;
    private final RotatingInstance cog;
    private final MechanicalCowBlockEntity mechanicalCowBlockEntity;
    final Direction horizontalDirection;
    private final Direction opposite;

    public MechanicalCowVisual(VisualizationContext context, MechanicalCowBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Direction.NORTH);

        horizontalDirection = blockState.getValue(HORIZONTAL_FACING);
        opposite = horizontalDirection.getOpposite();


        mechanicalCowBlockEntity = blockEntity;

        cowHead = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(ModPartials.HEAD)).createInstance();
        Quaternionf q = Axis.YP
                .rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(MechanicalPressBlock.HORIZONTAL_FACING).getOpposite()));

        cowHead.rotation(q);
        cog = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(ModPartials.COG_HORIZONTAL))
                .createInstance();
        cog.setup(blockEntity)
                .setPosition(getVisualPosition())
                .setChanged();
        transformModels(partialTick);

    }

    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        transformModels(ctx.partialTick());
    }

    private void transformModels(float pt) {
        cog.setup(blockEntity)
                .setChanged();

        float progress = getProgress(mechanicalCowBlockEntity);
        float yPos = 0.0f;
        if(getProgress(mechanicalCowBlockEntity) > 0)
            yPos = (float) Math.sin(progress) /10;

        cowHead.position(getVisualPosition())
                .translatePosition(0, .1f + yPos, 0)
                .setChanged();
    }
    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);
        relight(cog);
        relight(pos, cowHead);
    }

    private float getProgress(MechanicalCowBlockEntity pCowBlockEntity) {
        DynamicCycleBehavior cycleBehavior = pCowBlockEntity.getCycleBehaviour();
        return cycleBehavior.getProgress(AnimationTickHolder.getPartialTicks());
    }
    @Override
    protected void _delete() {
        super._delete();
        cog.delete();
        cowHead.delete();
    }
    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        consumer.accept(cog);
        consumer.accept(cowHead);
    }

}

