package com.ores.registries;

import com.ores.OresMod;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(OresMod.MODID);
    public static final Map<String, Supplier<Block>> REGISTERED_BLOCKS = new HashMap<>();

    public static void registerBlocks() {
        for (Registry.BlockRegistryEntry entry : Registry.BLOCKS_TO_REGISTER) {
            Supplier<Block> blockSupplier = BLOCKS.registerBlock(entry.ID(), entry.blockConstructor(), entry.properties());
            REGISTERED_BLOCKS.put(entry.ID(), blockSupplier);
        }
    }

    public static Supplier<Block> getBlock(String name) {
        return REGISTERED_BLOCKS.get(name);
    }
}