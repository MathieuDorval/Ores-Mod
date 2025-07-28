package com.ores.registries;


import com.ores.OresMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OresMod.MODID);
    public static final Map<String, Supplier<? extends Item>> REGISTERED_ITEMS = new HashMap<>();

    public static void registerItems() {
        // --- 1. Enregistrement des Items Simples ---
        for (Registry.ItemRegistryEntry entry : Registry.ITEMS_TO_REGISTER) {
            Supplier<Item> itemSupplier = ITEMS.registerSimpleItem(entry.ID(), entry.properties());
            REGISTERED_ITEMS.put(entry.ID(), itemSupplier);
        }

        // --- 2. Enregistrement des Items de Blocs (BlockItems) ---
        for (Registry.BlockRegistryEntry entry : Registry.BLOCKS_TO_REGISTER) {
            Supplier<Block> blockSupplier = ModBlocks.getBlock(entry.ID());
            if (blockSupplier != null) {
                Supplier<BlockItem> blockItemSupplier = ITEMS.registerSimpleBlockItem(entry.ID(),blockSupplier, entry.itemProperties());
                REGISTERED_ITEMS.put(entry.ID(), blockItemSupplier);
            }
        }
    }

    public static Supplier<? extends Item> getItem(String name) {
        return REGISTERED_ITEMS.get(name);
    }
}