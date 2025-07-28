package com.ores.datagen;

import com.ores.core.Materials;
import com.ores.core.Variants;
import com.ores.registries.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, "ores");
    }

    protected void addTags(HolderLookup.@NotNull Provider provider) {
        for (Materials material : Materials.values()) {
            for (Variants variant : material.getVariants()) {
                String blockId = variant.getFormattedId(material.getId());
                Supplier<Block> blockSupplier = ModBlocks.getBlock(blockId);

                if (blockSupplier == null) {
                    continue;
                }

                var category = variant.getCategory();

                if (category == Variants.Category.BLOCK || category == Variants.Category.FALLING_BLOCK) {
                    Variants.BlockProps varProps = Objects.requireNonNull(variant.getBlockProps());
                    Materials.BlockProps matProps = material.getBlockProps();

                    int maxToolLevel = Math.max(matProps.toolLevel(), varProps.toolLevel());
                    this.applyToolTags(blockSupplier.get(), varProps.tool(), maxToolLevel);

                    if (material.getItemProps().beacon() && varProps.beacon()) {
                        this.tag(BlockTags.BEACON_BASE_BLOCKS).add(blockSupplier.get());
                    }

                } else if (category == Variants.Category.ORE || category == Variants.Category.FALLING_ORE) {
                    Variants.OreProps varProps = Objects.requireNonNull(variant.getOreProps());
                    Materials.OreProps matProps = material.getOreProps();

                    int maxToolLevel = Math.max(matProps.toolLevel(), varProps.toolLevel());
                    this.applyToolTags(blockSupplier.get(), Materials.Tools.PICKAXE, maxToolLevel);
                }
            }
        }
    }

    private void applyToolTags(Block block, Materials.Tools toolType, int maxToolLevel) {
        switch (toolType) {
            case PICKAXE -> this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
            case SHOVEL -> this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(block);
            case AXE -> this.tag(BlockTags.MINEABLE_WITH_AXE).add(block);
            case HOE -> this.tag(BlockTags.MINEABLE_WITH_HOE).add(block);
            case SWORD -> this.tag(BlockTags.SWORD_EFFICIENT).add(block);
        }

        switch (maxToolLevel) {
            case 1 -> this.tag(BlockTags.NEEDS_STONE_TOOL).add(block);
            case 2 -> this.tag(BlockTags.NEEDS_IRON_TOOL).add(block);
            case 3 -> this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(block);
            case 4 -> this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL).add(block);
        }
    }
}
