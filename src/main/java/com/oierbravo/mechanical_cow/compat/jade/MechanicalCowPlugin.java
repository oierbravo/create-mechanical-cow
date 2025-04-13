package com.oierbravo.mechanical_cow.compat.jade;

import com.oierbravo.mechanical_cow.content.MechanicalCowBlock;
import com.oierbravo.mechanical_cow.content.MechanicalCowBlockEntity;
import com.oierbravo.mechanicals.compat.jade.MechanicalProgressComponentProvider;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class MechanicalCowPlugin implements IWailaPlugin {
    public static final ResourceLocation MECHANICAL_COW_DATA = ResourceLocation.parse("mechanical_cow:data");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new MechanicalProgressComponentProvider(MECHANICAL_COW_DATA), MechanicalCowBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new MechanicalProgressComponentProvider(MECHANICAL_COW_DATA), MechanicalCowBlock.class);
    }
}
