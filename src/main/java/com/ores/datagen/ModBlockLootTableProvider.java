package com.ores.datagen;

import com.ores.core.Materials;
import com.ores.core.Variants;
import com.ores.registries.ModBlocks;
import com.ores.registries.ModItems;
import com.ores.core.Registry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
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
import java.util.stream.Collectors;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        // On parcourt tous les blocs qui ont été préparés pour l'enregistrement.
        for (Registry.BlockRegistryEntry entry : Registry.BLOCKS_TO_REGISTER) {
            var category = entry.category();
            Block block = ModBlocks.getBlock(entry.ID()).get();

            if (category == Variants.Category.BLOCK || category == Variants.Category.FALLING_BLOCK) {
                // Les blocs de stockage se dropent eux-mêmes.
                this.dropSelf(block);
            } else if (category == Variants.Category.ORE || category == Variants.Category.FALLING_ORE) {
                // Les minerais ont une logique de butin plus complexe.
                // On doit retrouver le matériau correspondant pour connaître le butin.
                findMaterialForBlock(entry.ID()).ifPresent(material -> {
                    Materials.OreProps oreProps = material.getOreProps();
                    Item dropItem = ModItems.getItem(oreProps.idDrop()).get();
                    float minDrop = Objects.requireNonNullElse(oreProps.minDrop(), 1).floatValue();
                    float maxDrop = Objects.requireNonNullElse(oreProps.maxDrop(), 1).floatValue();

                    this.add(block, createOreDrop(block, dropItem, minDrop, maxDrop));
                });
            }
        }
    }

    /**
     * Crée une table de butin pour un minerai, gérant l'enchantement Fortune.
     */
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

    /**
     * Retrouve le matériau d'origine à partir de l'ID d'un bloc.
     */
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
        // Retourne tous les blocs enregistrés par le mod.
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(net.neoforged.neoforge.registries.DeferredHolder::get)
                .collect(Collectors.toList());
    }
}
