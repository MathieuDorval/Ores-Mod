package com.ores.worldgen;

import com.ores.OresMod;
import com.ores.core.Materials;
import com.ores.core.Variants;
import com.ores.registries.ModBlocks;
import com.ores.registries.ModFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Classe unique pour définir les ConfiguredFeatures et PlacedFeatures des minerais.
 * Chaque type de filon est défini explicitement pour un contrôle maximal.
 */
public class ModOreFeatures {

    // -=-=-=- VANILLA -=-=-=-
    // === ORE ===
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COAL_KEY = registerConfiguredKey("ore_coal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COAL_BURIED_KEY = registerConfiguredKey("ore_coal_buried");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_KEY = registerConfiguredKey("ore_iron");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_SMALL_KEY = registerConfiguredKey("ore_iron_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COPPER_LARGE_KEY = registerConfiguredKey("ore_copper_large");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COPPER_SMALL_KEY = registerConfiguredKey("ore_copper_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD_KEY = registerConfiguredKey("ore_gold");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD_BURIED_KEY = registerConfiguredKey("ore_gold_buried");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD_NETHER_KEY = registerConfiguredKey("ore_gold_nether");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_REDSTONE_KEY = registerConfiguredKey("ore_redstone");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LAPIS_KEY = registerConfiguredKey("ore_lapis");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LAPIS_BURIED_KEY = registerConfiguredKey("ore_lapis_buried");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_BURIED_KEY = registerConfiguredKey("ore_diamond_buried");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_LARGE_KEY = registerConfiguredKey("ore_diamond_large");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_MEDIUM_KEY = registerConfiguredKey("ore_diamond_medium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_SMALL_KEY = registerConfiguredKey("ore_diamond_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_EMERALD_KEY = registerConfiguredKey("ore_emerald");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_QUARTZ_KEY = registerConfiguredKey("ore_quartz");
    public static final ResourceKey<PlacedFeature> ORE_COAL_LOWER_PLACED_KEY = registerPlacedKey("ore_coal_lower_placed");
    public static final ResourceKey<PlacedFeature> ORE_COAL_UPPER_PLACED_KEY = registerPlacedKey("ore_coal_upper_placed");
    public static final ResourceKey<PlacedFeature> ORE_IRON_MIDDLE_PLACED_KEY = registerPlacedKey("ore_iron_middle_placed");
    public static final ResourceKey<PlacedFeature> ORE_IRON_SMALL_PLACED_KEY = registerPlacedKey("ore_iron_small_placed");
    public static final ResourceKey<PlacedFeature> ORE_IRON_UPPER_PLACED_KEY = registerPlacedKey("ore_iron_upper_placed");
    public static final ResourceKey<PlacedFeature> ORE_COPPER_PLACED_KEY = registerPlacedKey("ore_copper_placed");
    public static final ResourceKey<PlacedFeature> ORE_COPPER_LARGE_PLACED_KEY = registerPlacedKey("ore_copper_large_placed");
    public static final ResourceKey<PlacedFeature> ORE_GOLD_PLACED_KEY = registerPlacedKey("ore_gold_placed");
    public static final ResourceKey<PlacedFeature> ORE_GOLD_DELTAS_PLACED_KEY = registerPlacedKey("ore_gold_deltas_placed");
    public static final ResourceKey<PlacedFeature> ORE_GOLD_EXTRA_PLACED_KEY = registerPlacedKey("ore_gold_extra_placed");
    public static final ResourceKey<PlacedFeature> ORE_GOLD_LOWER_PLACED_KEY = registerPlacedKey("ore_gold_lower_placed");
    public static final ResourceKey<PlacedFeature> ORE_GOLD_NETHER_PLACED_KEY = registerPlacedKey("ore_gold_nether_placed");
    public static final ResourceKey<PlacedFeature> ORE_REDSTONE_PLACED_KEY = registerPlacedKey("ore_redstone_placed");
    public static final ResourceKey<PlacedFeature> ORE_REDSTONE_LOWER_PLACED_KEY = registerPlacedKey("ore_redstone_lower_placed");
    public static final ResourceKey<PlacedFeature> ORE_LAPIS_PLACED_KEY = registerPlacedKey("ore_lapis_placed");
    public static final ResourceKey<PlacedFeature> ORE_LAPIS_BURIED_PLACED_KEY = registerPlacedKey("ore_lapis_buried_placed");
    public static final ResourceKey<PlacedFeature> ORE_DIAMOND_PLACED_KEY = registerPlacedKey("ore_diamond_placed");
    public static final ResourceKey<PlacedFeature> ORE_DIAMOND_BURIED_PLACED_KEY = registerPlacedKey("ore_diamond_buried_placed");
    public static final ResourceKey<PlacedFeature> ORE_DIAMOND_LARGE_PLACED_KEY = registerPlacedKey("ore_diamond_large_placed");
    public static final ResourceKey<PlacedFeature> ORE_DIAMOND_MEDIUM_PLACED_KEY = registerPlacedKey("ore_diamond_medium_placed");
    public static final ResourceKey<PlacedFeature> ORE_EMERALD_PLACED_KEY = registerPlacedKey("ore_emerald_placed");
    public static final ResourceKey<PlacedFeature> ORE_QUARTZ_DELTAS_PLACED_KEY = registerPlacedKey("ore_quartz_deltas_placed");
    public static final ResourceKey<PlacedFeature> ORE_QUARTZ_NETHER_PLACED_KEY = registerPlacedKey("ore_quartz_nether_placed");
    // === ORE VEIN ===
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_VEIN_COPPER_KEY = registerConfiguredKey("ore_vein_copper");
    public static final ResourceKey<PlacedFeature> ORE_VEIN_COPPER_PLACED_KEY = registerPlacedKey("ore_vein_copper_placed");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_VEIN_IRON_KEY = registerConfiguredKey("ore_vein_iron");
    public static final ResourceKey<PlacedFeature> ORE_VEIN_IRON_PLACED_KEY = registerPlacedKey("ore_vein_iron_placed");




    public static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // -=-=-=- VANILLA -=-=-=-
        // === ORE ===
        // --- COAL ---
        register(context, ORE_COAL_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.COAL), 17, 0.0F));
        register(context, ORE_COAL_BURIED_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.COAL), 17, 0.5F));
        // --- IRON ---
        register(context, ORE_IRON_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.IRON), 9, 0.0F));
        register(context, ORE_IRON_SMALL_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.IRON), 4, 0.0F));
        // --- COPPER ---
        register(context, ORE_COPPER_LARGE_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.COPPER), 20, 0.0F));
        register(context, ORE_COPPER_SMALL_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.COPPER), 10, 0.0F));
        // --- GOLD ---
        register(context, ORE_GOLD_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.GOLD), 9, 0.0F));
        register(context, ORE_GOLD_BURIED_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.GOLD), 9, 0.5F));
        register(context, ORE_GOLD_NETHER_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.GOLD), 10, 0.0F));
        // --- REDSTONE ---
        register(context, ORE_REDSTONE_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.REDSTONE), 8, 0.0F));
        // --- LAPIS ---
        register(context, ORE_LAPIS_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.LAPIS), 7, 0.0F));
        register(context, ORE_LAPIS_BURIED_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.LAPIS), 7, 1.0F));
        // --- DIAMOND ---
        register(context, ORE_DIAMOND_BURIED_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.DIAMOND), 8, 1.0F));
        register(context, ORE_DIAMOND_LARGE_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.DIAMOND), 12, 0.7F));
        register(context, ORE_DIAMOND_MEDIUM_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.DIAMOND), 8, 0.9F));
        register(context, ORE_DIAMOND_SMALL_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.DIAMOND), 4, 0.5F));
        // --- EMERALD ---
        register(context, ORE_EMERALD_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.EMERALD), 3, 0.0F));
        // --- QUARTZ ---
        register(context, ORE_QUARTZ_KEY, Feature.ORE, new OreConfiguration(getAllOreTargetsForMaterial(Materials.QUARTZ), 14, 0.0F));
        // === ORE VEIN ===
        // --- COPPER ---
        register(context, ORE_VEIN_COPPER_KEY, ModFeatures.GIANT_ORE_VEIN,
                new OreVeinConfiguration(
                        ModBlocks.getBlock("granite_copper_ore").get().defaultBlockState(),
                        BuiltInRegistries.BLOCK.getOrThrow(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse("minecraft:granite"))).value().defaultBlockState(),
                        BuiltInRegistries.BLOCK.getOrThrow(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse("minecraft:raw_copper_block"))).value().defaultBlockState(),
                        new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES),
                        2.8f, 0.35f, 0.05f
                )
        );
        // --- IRON ---
        register(context, ORE_VEIN_IRON_KEY, ModFeatures.GIANT_ORE_VEIN,
                new OreVeinConfiguration(
                        ModBlocks.getBlock("tuff_iron_ore").get().defaultBlockState(),
                        BuiltInRegistries.BLOCK.getOrThrow(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse("minecraft:tuff"))).value().defaultBlockState(),
                        BuiltInRegistries.BLOCK.getOrThrow(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse("minecraft:raw_iron_block"))).value().defaultBlockState(),
                        new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES),
                        2.5f, 0.4f, 0.05f
                )
        );
    }

    public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        var configuredFeatureRegistry = context.lookup(Registries.CONFIGURED_FEATURE);
        // === VANILLA ===
        // --- COAL ---
        register(context, ORE_COAL_LOWER_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_COAL_BURIED_KEY), OrePlacement.commonOrePlacement(20, HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(192))));
        register(context, ORE_COAL_UPPER_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_COAL_KEY), OrePlacement.commonOrePlacement(30, HeightRangePlacement.uniform(VerticalAnchor.absolute(90), VerticalAnchor.absolute(136))));
        // --- IRON ---
        register(context, ORE_IRON_MIDDLE_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_IRON_KEY), OrePlacement.commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));
        register(context, ORE_IRON_SMALL_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_IRON_SMALL_KEY), OrePlacement.commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(72))));
        register(context, ORE_IRON_UPPER_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_IRON_KEY), OrePlacement.commonOrePlacement(90, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))));
        // --- COPPER ---
        register(context, ORE_COPPER_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_COPPER_SMALL_KEY), OrePlacement.commonOrePlacement(16, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112))));
        register(context, ORE_COPPER_LARGE_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_COPPER_LARGE_KEY), OrePlacement.commonOrePlacement(16, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112))));
        // --- GOLD ---
        register(context, ORE_GOLD_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_GOLD_BURIED_KEY), OrePlacement.commonOrePlacement(8, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
        register(context, ORE_GOLD_DELTAS_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_GOLD_NETHER_KEY), OrePlacement.commonOrePlacement(20, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10))));
        register(context, ORE_GOLD_EXTRA_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_GOLD_KEY), OrePlacement.commonOrePlacement(50, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256))));
        register(context, ORE_GOLD_LOWER_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_GOLD_BURIED_KEY), List.of(CountPlacement.of(UniformInt.of(0, 1)), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48)), BiomeFilter.biome()));
        register(context, ORE_GOLD_NETHER_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_GOLD_NETHER_KEY), OrePlacement.commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10))));
        // --- REDSTONE ---
        register(context, ORE_REDSTONE_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_REDSTONE_KEY), OrePlacement.commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(15))));
        register(context, ORE_REDSTONE_LOWER_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_REDSTONE_KEY), OrePlacement.commonOrePlacement(8, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-32), VerticalAnchor.aboveBottom(32))));
        // --- LAPIS ---
        register(context, ORE_LAPIS_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_LAPIS_KEY), OrePlacement.commonOrePlacement(2, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(32))));
        register(context, ORE_LAPIS_BURIED_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_LAPIS_BURIED_KEY), OrePlacement.commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(64))));
        // --- DIAMOND ---
        register(context, ORE_DIAMOND_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_DIAMOND_SMALL_KEY), OrePlacement.commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
        register(context, ORE_DIAMOND_BURIED_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_DIAMOND_BURIED_KEY), OrePlacement.commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
        register(context, ORE_DIAMOND_LARGE_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_DIAMOND_LARGE_KEY), OrePlacement.rareOrePlacement(9, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
        register(context, ORE_DIAMOND_MEDIUM_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_DIAMOND_MEDIUM_KEY), OrePlacement.commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-4))));
        // --- EMERALD ---
        register(context, ORE_EMERALD_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_EMERALD_KEY), OrePlacement.commonOrePlacement(100, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480))));
        // --- QUARTZ ---
        register(context, ORE_QUARTZ_DELTAS_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_QUARTZ_KEY), OrePlacement.commonOrePlacement(32, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10))));
        register(context, ORE_QUARTZ_NETHER_PLACED_KEY, configuredFeatureRegistry.getOrThrow(ORE_QUARTZ_KEY), OrePlacement.commonOrePlacement(16, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10))));
        // === ORE VEIN ===
        context.register(ORE_VEIN_COPPER_PLACED_KEY, new PlacedFeature(
                configuredFeatureRegistry.getOrThrow(ORE_VEIN_COPPER_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(96),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(96)),
                        BiomeFilter.biome()
                )
        ));
        context.register(ORE_VEIN_IRON_PLACED_KEY, new PlacedFeature(
                configuredFeatureRegistry.getOrThrow(ORE_VEIN_IRON_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(128),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56)),
                        BiomeFilter.biome()
                )
        ));
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

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, DeferredHolder<Feature<?>, F> feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature.get(), configuration));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placement) {
        context.register(key, new PlacedFeature(feature, placement));
    }
}
