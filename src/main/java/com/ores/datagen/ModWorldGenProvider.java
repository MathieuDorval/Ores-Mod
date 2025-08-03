package com.ores.datagen;

import com.ores.OresMod;
import com.ores.worldgen.ModBiomeModifiers;
import com.ores.worldgen.ModOreFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Le fournisseur de données principal pour tous les éléments de worldgen (monde).
 * Il utilise les classes Bootstrap pour générer les fichiers JSON correspondants.
 */
public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {

    // Construit un ensemble de registres à générer.
    // On ajoute nos features configurées, features placées et modificateurs de biome.
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModOreFeatures::bootstrapConfiguredFeatures)
            .add(Registries.PLACED_FEATURE, ModOreFeatures::bootstrapPlacedFeatures)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(OresMod.MODID));
    }
}