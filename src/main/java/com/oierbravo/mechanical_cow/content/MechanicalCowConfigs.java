package com.oierbravo.mechanical_cow.content;

import net.createmod.catnip.config.ConfigBase;
import net.neoforged.neoforge.common.ModConfigSpec;

public class MechanicalCowConfigs extends ConfigBase {
    private static final int VERSION = 1;

    public final ConfigInt processingTime = i(500, 0, Integer.MAX_VALUE, "processingTime", Comments.processingTime);
    public final ConfigInt outputAmount = i(100, 1, "outputAmount", Comments.outputAmount);
    public final ConfigInt fluidCapacity = i(1000, "fluidCapacity", Comments.fluidCapacity);
    public final ConfigInt requiredIngredientAmount = i(4, "requiredIngredientAmount", Comments.requiredIngredientAmount);
    public final ConfigString requiredIngredient = s("minecraft:wheat","requiredIngredient", Comments.requiredIngredient);
    public final ConfigString outputFluid = s("minecraft:milk","outputFluid", Comments.outputFluid);
    public final ConfigFloat soundVolume = f(0.5f, 0, 1f, "soundVolume", Comments.soundVolume);

    private static class Comments {
        static String processingTime = "Processing time (in ticks).";
        static String fluidCapacity = "Fluid capacity.";
        static String requiredIngredientAmount = "Required ingredient amount.";
        static String requiredIngredient = "Required ingredient.";
        static String outputFluid = "Output fluid.";
        static String outputAmount = "Output amount.";
        static String soundVolume = "Sound volume.";

    }

    @Override
    public String getName() {
        return "mechanical_cow.v" + VERSION;
    }



    public class ConfigString extends CValue<String, ModConfigSpec.ConfigValue<String>> {

        public ConfigString(String name, String current, String... comment) {
            super(name, builder -> builder.define(name, current), comment);
        }
    }
    protected ConfigString s(String current, String name, String... comment) {
        return new ConfigString(name, current, comment);
    }
}
