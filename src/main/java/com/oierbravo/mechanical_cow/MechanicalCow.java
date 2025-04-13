package com.oierbravo.mechanical_cow;

import com.mojang.logging.LogUtils;
import com.oierbravo.mechanical_cow.content.MechanicalCowBlockEntity;
import com.oierbravo.mechanical_cow.infrastructure.data.ModDataGen;
import com.oierbravo.mechanical_cow.infrastructure.config.MConfigs;
import com.oierbravo.mechanical_cow.ponders.ModPonderPlugin;
import com.oierbravo.mechanical_cow.registrate.*;
import com.oierbravo.mechanicals.utility.RegistrateLangBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

import static com.oierbravo.mechanical_cow.ModConstants.MODID;

@Mod(MODID)
public class MechanicalCow
{

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID).defaultCreativeTab(ModCreativeTabs.MAIN_TAB.getKey());
    static {
        REGISTRATE.setTooltipModifierFactory(item ->
                new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                        .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
        );
    }
    public static final Logger LOGGER = LogUtils.getLogger();

    public MechanicalCow(IEventBus modEventBus, ModContainer modContainer)
    {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        REGISTRATE.registerEventListeners(modEventBus);

        ModCreativeTabs.register(modEventBus);

        ModBlocks.register();
        ModBlockEntities.register();
        

        MConfigs.register(modLoadingContext,modContainer);


        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::registerCapabilities);

        modEventBus.addListener(ModDataGen::gatherData);

        generateLangEntries();
    }
    private void generateLangEntries(){
        new RegistrateLangBuilder(MODID, registrate())
            .addRaw("config.jade.plugin_mechanical_cow.data", "Mechanical Cow")
            .add("itemGroup.mechanical_cow:main", "Mechanical Cow")
            .addRaw("block.mechanical_cow.mechanical_cow.tooltip", "MECHANICAL Cow")
            .addRaw("block.mechanical_cow.mechanical_cow.tooltip.summary", "Cow _Milk_ generator.")
            .add("mechanical_cow.tooltip.progress", "Progress: %d%%")
            .add("ponder.mechanical_cow.header", "Mechanical Milk generator")
            .add("ponder.mechanical_cow.text_1", "The Mechanical Cow uses rotational force and an specific ingredient to generate milk")
            .add("ponder.mechanical_cow.text_2", "Its powered from the bottom")
            .add("ponder.mechanical_cow.text_3", "Item input can ONLY go from the FRONT side")
            .add("ponder.mechanical_cow.text_4", "Can only be inserted by automation")
            .add("ponder.mechanical_cow.text_5", "Milk output is ONLY located on the BACK side")
            .add("ponder.mechanical_cow.text_6", "Can only be extracted by automation")
            .add("recipe", "Mechanical Cow Recipe");

    }
    @net.neoforged.bus.api.SubscribeEvent
    public void registerCapabilities(net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent event) {
        MechanicalCowBlockEntity.registerCapabilities(event);
    }
    private void doClientStuff(final FMLClientSetupEvent event) {

        ModPartials.init();
        PonderIndex.addPlugin(new ModPonderPlugin());

    }
    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
