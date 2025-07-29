package com.ores;

import com.ores.registries.ModBlocks;
import com.ores.registries.ModCreativeTabs;
import com.ores.registries.ModItems;
import com.ores.core.Registry;
import com.ores.registries.ModLootModifierSerializers;
import com.ores.unification.UnificationRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(OresMod.MODID)
public class OresMod {
    public static final String MODID = "ores";
    public OresMod(IEventBus modEventBus, ModContainer modContainer) {
        Registry.initialize();
        ModBlocks.registerBlocks();
        ModItems.registerItems();

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(UnificationRegistry::onRegisterItems);
        ModLootModifierSerializers.register(modEventBus);
    }
}
