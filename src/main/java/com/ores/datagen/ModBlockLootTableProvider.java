package com.ores.datagen;

import com.ores.OresMod;
import com.ores.core.Materials;
import com.ores.core.Variants;
import com.ores.registries.ModBlocks;
import com.ores.registries.ModItems;
import com.ores.core.Registry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        for (Registry.BlockRegistryEntry entry : Registry.BLOCKS_TO_REGISTER) {
            var category = entry.category();
            Block block = ModBlocks.getBlock(entry.ID()).get();

            if (category == Variants.Category.BLOCK || category == Variants.Category.FALLING_BLOCK) {
                this.dropSelf(block);
            } else if (category == Variants.Category.ORE || category == Variants.Category.FALLING_ORE) {
                findMaterialForBlock(entry.ID()).ifPresent(material -> {
                    Materials.OreProps oreProps = material.getOreProps();
                    String dropId = oreProps.idDrop();
                    Item dropItem = null;

                    if ("self".equalsIgnoreCase(dropId)) {
                        dropItem = block.asItem();
                    } else if (dropId.startsWith("minecraft:")) {
                        Optional<Item> optionalItem = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(dropId));
                        if (optionalItem.isPresent()) {
                            dropItem = optionalItem.get();
                        }
                    } else {
                        String path = dropId.startsWith(OresMod.MODID + ":") ? dropId.substring(OresMod.MODID.length() + 1) : dropId;
                        Supplier<? extends Item> itemSupplier = ModItems.getItem(path);
                        if (itemSupplier != null) {
                            dropItem = itemSupplier.get();
                        }
                    }

                    if (dropItem == null || dropItem == Items.AIR) {
                        System.err.println("ERREUR DE LOOT TABLE: Impossible de déterminer l'item à dropper pour '" + entry.ID() + "' avec l'ID de drop '" + dropId + "'");
                        return;
                    }

                    float minDrop = Objects.requireNonNullElse(oreProps.minDrop(), 1).floatValue();
                    float maxDrop = Objects.requireNonNullElse(oreProps.maxDrop(), 1).floatValue();

                    this.add(block, createOreDrop(block, dropItem, minDrop, maxDrop));
                });
            }
        }
    }

    protected LootTable.Builder createOreDrop(Block block, Item dropItem, float min, float max) {
        HolderLookup.RegistryLookup<Enchantment> registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return createSilkTouchDispatchTable(block,
                this.applyExplosionDecay(block,
                        LootItem.lootTableItem(dropItem)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                                .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE)))
                )
        );
    }

    private Optional<Materials> findMaterialForBlock(String blockId) {
        for (Materials material : Materials.values()) {
            for (Variants variant : material.getVariants()) {
                if (variant.getFormattedId(material.getId()).equals(blockId)) {
                    return Optional.of(material);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(net.neoforged.neoforge.registries.DeferredHolder::get)
                .collect(Collectors.toList());
    }
}
