package com.ores.datagen;

import com.ores.core.Materials;
import com.ores.core.Variants;
import com.ores.registries.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        for (Materials material : Materials.values()) {
            for (Variants variant : material.getVariants()) {
                String itemId = variant.getFormattedId(material.getId());
                var itemSupplier = ModItems.getItem(itemId);
                if (itemSupplier == null) {
                    continue;
                }
                Item item = itemSupplier.get();
                Integer materialBurnTime = material.getItemProps().burnTime();
                // Gère les items simples
                if (variant.getCategory() == Variants.Category.ITEM && variant.getItemProps() != null) {
                    Integer variantBurnTime = variant.getItemProps().burnTime();
                    if (materialBurnTime != null && variantBurnTime != null) {
                        int finalBurnTime = materialBurnTime * variantBurnTime / 100;
                        if (finalBurnTime > 0) {
                            this.builder(NeoForgeDataMaps.FURNACE_FUELS).add(item.builtInRegistryHolder(), new FurnaceFuel(finalBurnTime), false);
                        }
                    }
                }
                // Gère les BlockItems (qui peuvent aussi être des combustibles, comme le bloc de charbon)
                else if ((variant.getCategory() == Variants.Category.BLOCK || variant.getCategory() == Variants.Category.FALLING_BLOCK) && variant.getBlockProps() != null) {
                    Integer variantBurnTime = variant.getBlockProps().burnTime();
                    if (materialBurnTime != null && variantBurnTime != null) {
                        int finalBurnTime = materialBurnTime * variantBurnTime / 100;
                        if (finalBurnTime > 0) {
                            this.builder(NeoForgeDataMaps.FURNACE_FUELS).add(item.builtInRegistryHolder(), new FurnaceFuel(finalBurnTime), false);
                        }
                    }
                }
            }
        }
    }
}
