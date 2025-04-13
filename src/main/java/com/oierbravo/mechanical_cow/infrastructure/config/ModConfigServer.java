package com.oierbravo.mechanical_cow.infrastructure.config;

import com.oierbravo.mechanical_cow.content.MechanicalCowConfigs;
import net.createmod.catnip.config.ConfigBase;

public class ModConfigServer extends ConfigBase {
    public final MechanicalCowConfigs mechanicalCow = nested(0, MechanicalCowConfigs::new, "Mechanical Cow");

    public final ModStress stressValues = nested(0, ModStress::new, "Stress values");

    @Override
    public String getName() {
        return "server";
    }
}
