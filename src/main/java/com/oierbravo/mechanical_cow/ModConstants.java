package com.oierbravo.mechanical_cow;

import net.minecraft.resources.ResourceLocation;

public class ModConstants {
    public static final String MODID = "mechanical_cow";
    public static final String DISPLAY_NAME = "Create Mechanical Cow";
    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
