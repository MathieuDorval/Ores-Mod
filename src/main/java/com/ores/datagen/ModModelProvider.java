package com.ores.datagen;

import com.ores.OresMod;
import com.ores.registries.ModBlocks;
import com.ores.registries.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import org.jetbrains.annotations.NotNull;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, OresMod.MODID);
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        for (var entry : ModBlocks.BLOCKS.getEntries()) {
            blockModels.createTrivialCube(entry.get());
        }
        for (var entry : ModItems.ITEMS.getEntries()) {
            if (!(entry.get() instanceof BlockItem)) {
                itemModels.generateFlatItem(entry.get(), ModelTemplates.FLAT_ITEM);
            }
        }
    }
}