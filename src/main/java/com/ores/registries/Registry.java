package com.ores.registries;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Registry {
    public record BlockRegistryEntry(String ID, Function<BlockBehaviour.Properties, ? extends Block> blockConstructor, BlockBehaviour.Properties properties, Item.Properties itemProperties) {}
    public record ItemRegistryEntry(String ID, Item.Properties properties) {}

    public static final List<BlockRegistryEntry> BLOCKS_TO_REGISTER = new ArrayList<>();
    public static final List<ItemRegistryEntry> ITEMS_TO_REGISTER = new ArrayList<>();

    public static void initialize() {
        BLOCKS_TO_REGISTER.clear();
        ITEMS_TO_REGISTER.clear();



    }
}
