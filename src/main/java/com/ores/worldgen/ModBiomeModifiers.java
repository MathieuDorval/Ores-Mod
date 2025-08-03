package com.ores.worldgen;

import com.ores.OresMod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Définit les 'Biome Modifiers' du mod.
 * Cette classe indique au jeu d'ajouter nos listes de minerais (tags)
 * aux biomes des dimensions correspondantes.
 */
public class ModBiomeModifiers {

    // --- Tags de Features par Dimension ---
    public static final TagKey<PlacedFeature> OVERWORLD_ORES_TAG = createPlacedFeatureTag("overworld_ores");
    public static final TagKey<PlacedFeature> NETHER_ORES_TAG = createPlacedFeatureTag("nether_ores");
    public static final TagKey<PlacedFeature> END_ORES_TAG = createPlacedFeatureTag("end_ores");


    // --- Clés de Modificateurs de Biome par Dimension ---
    public static final ResourceKey<BiomeModifier> ADD_OVERWORLD_ORES = registerKey("add_overworld_ores");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_ORES = registerKey("add_nether_ores");
    public static final ResourceKey<BiomeModifier> ADD_END_ORES = registerKey("add_end_ores");


    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // Enregistre un modificateur pour l'Overworld.
        context.register(ADD_OVERWORLD_ORES, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                placedFeatures.getOrThrow(OVERWORLD_ORES_TAG),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));

        // Enregistre un modificateur pour le Nether.
        context.register(ADD_NETHER_ORES, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                placedFeatures.getOrThrow(NETHER_ORES_TAG),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));

        // Enregistre un modificateur pour l'End.
        context.register(ADD_END_ORES, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_END),
                placedFeatures.getOrThrow(END_ORES_TAG),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(OresMod.MODID, name));
    }

    private static TagKey<PlacedFeature> createPlacedFeatureTag(String name) {
        return TagKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(OresMod.MODID, name));
    }
}
