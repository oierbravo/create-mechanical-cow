package com.oierbravo.mechanical_cow.infrastructure.data;

import com.oierbravo.mechanical_cow.MechanicalCow;
import com.oierbravo.mechanical_cow.ModConstants;
import com.simibubi.create.Create;
import com.tterrag.registrate.providers.RegistrateDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class ModDataGen {
    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(true, MechanicalCow.registrate().setDataProvider(new RegistrateDataProvider(MechanicalCow.registrate(), ModConstants.MODID, event)));
    }
}
