package com.ores.worldgen;

import com.ores.OresMod;
import com.ores.core.Materials;
import com.ores.core.Variants;
import com.ores.registries.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Classe unique pour définir les ConfiguredFeatures et PlacedFeatures des minerais.
 * Chaque type de filon est défini explicitement pour un contrôle maximal.
 */
public class ModOreFeatures {

    // --- Clés de Ressources pour chaque type de filon ---
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COAL_UPPER_KEY = registerConfiguredKey("ore_coal_upper");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COAL_LOWER_KEY = registerConfiguredKey("ore_coal_lower");
    public static final ResourceKey<PlacedFeature> ORE_COAL_UPPER_PLACED_KEY = registerPlacedKey("ore_coal_upper_placed");
    public static final ResourceKey<PlacedFeature> ORE_COAL_LOWER_PLACED_KEY = registerPlacedKey("ore_coal_lower_placed");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_UPPER_KEY = registerConfiguredKey("ore_iron_upper");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_MIDDLE_KEY = registerConfiguredKey("ore_iron_middle");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_SMALL_KEY = registerConfiguredKey("ore_iron_small");
    public static final ResourceKey<PlacedFeature> ORE_IRON_UPPER_PLACED_KEY = registerPlacedKey("ore_iron_upper_placed");
    public static final ResourceKey<PlacedFeature> ORE_IRON_MIDDLE_PLACED_KEY = registerPlacedKey("ore_iron_middle_placed");
    public static final ResourceKey<PlacedFeature> ORE_IRON_SMALL_PLACED_KEY = registerPlacedKey("ore_iron_small_placed");

    // Ajoutez d'autres clés ici...


    public static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // --- CHARBON ---
        register(context, ORE_COAL_UPPER_KEY, Feature.ORE, new OreConfiguration(
                getAllOreTargetsForMaterial(Materials.COAL), 17
        ));
        register(context, ORE_COAL_LOWER_KEY, Feature.ORE, new OreConfiguration(
                getAllOreTargetsForMaterial(Materials.COAL), 9
        ));

        // --- FER ---
        register(context, ORE_IRON_UPPER_KEY, Feature.ORE, new OreConfiguration(
                getAllOreTargetsForMaterial(Materials.IRON), 9
        ));
        register(context, ORE_IRON_MIDDLE_KEY, Feature.ORE, new OreConfiguration(
                getAllOreTargetsForMaterial(Materials.IRON), 9
        ));
        register(context, ORE_IRON_SMALL_KEY, Feature.ORE, new OreConfiguration(
                List.of(createOreTarget(Materials.IRON, Variants.TUFF_ORE)), 4
        ));

        // Ajoutez d'autres ConfiguredFeatures ici...
    }

    public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        var configuredFeatureRegistry = context.lookup(Registries.CONFIGURED_FEATURE);

        // --- CHARBON ---
        register(context, ORE_COAL_UPPER_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_COAL_UPPER_KEY),
                OrePlacement.commonOrePlacement(30, HeightRangePlacement.uniform(VerticalAnchor.absolute(90), VerticalAnchor.absolute(192)))
        );
        register(context, ORE_COAL_LOWER_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_COAL_LOWER_KEY),
                OrePlacement.commonOrePlacement(20, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(120)))
        );

        // --- FER ---
        register(context, ORE_IRON_UPPER_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_IRON_UPPER_KEY),
                OrePlacement.commonOrePlacement(20, HeightRangePlacement.uniform(VerticalAnchor.absolute(80), VerticalAnchor.absolute(256)))
        );
        register(context, ORE_IRON_MIDDLE_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_IRON_MIDDLE_KEY),
                OrePlacement.commonOrePlacement(30, HeightRangePlacement.uniform(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56)))
        );
        register(context, ORE_IRON_SMALL_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_IRON_SMALL_KEY),
                OrePlacement.commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()))
        );

        // Ajoutez d'autres PlacedFeatures ici...
    }

    private static List<OreConfiguration.TargetBlockState> getAllOreTargetsForMaterial(Materials material) {
        List<OreConfiguration.TargetBlockState> targets = new ArrayList<>();
        for (Variants variant : Variants.values()) {
            if (variant.getCategory() == Variants.Category.ORE || variant.getCategory() == Variants.Category.FALLING_ORE) {
                if (ModBlocks.getBlock(variant.getFormattedId(material.getId())) != null) {
                    targets.add(createOreTarget(material, variant));
                }
            }
        }
        return targets;
    }

    private static OreConfiguration.TargetBlockState createOreTarget(Materials material, Variants variant) {
        Variants.OreProps oreProps = variant.getOreProps();
        String oreBlockId = variant.getFormattedId(material.getId());
        Supplier<Block> oreBlockSupplier = ModBlocks.getBlock(oreBlockId);

        if (oreProps != null && oreBlockSupplier != null) {
            Optional<Block> targetBlock = Optional.ofNullable(ResourceLocation.tryParse(oreProps.idStone()))
                    .flatMap(BuiltInRegistries.BLOCK::getOptional);

            if (targetBlock.isPresent()) {
                RuleTest ruleTest = new BlockMatchTest(targetBlock.get());
                return OreConfiguration.target(ruleTest, oreBlockSupplier.get().defaultBlockState());
            } else {
                throw new IllegalStateException("Could not find block with ID '" + oreProps.idStone() + "' for variant " + variant.name());
            }
        }
        throw new IllegalStateException("Missing OreProps or Block Supplier for " + material.getId() + " with variant " + variant.name());
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerConfiguredKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(OresMod.MODID, name));
    }

    private static ResourceKey<PlacedFeature> registerPlacedKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(OresMod.MODID, name));
    }

    private static <FC extends net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placement) {
        context.register(key, new PlacedFeature(feature, placement));
    }
}
