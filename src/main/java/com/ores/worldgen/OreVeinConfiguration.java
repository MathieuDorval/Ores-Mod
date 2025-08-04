package com.ores.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public record OreVeinConfiguration(BlockState primaryOre, BlockState fillerStone, BlockState rareBlock, RuleTest target,
                                   float veinSize, float primaryOreDensity,
                                   float rareBlockChance) implements FeatureConfiguration {

    public static final Codec<OreVeinConfiguration> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(
                BlockState.CODEC.fieldOf("primary_ore").forGetter((config) -> config.primaryOre),
                BlockState.CODEC.fieldOf("filler_stone").forGetter((config) -> config.fillerStone),
                BlockState.CODEC.fieldOf("rare_block").forGetter((config) -> config.rareBlock),
                RuleTest.CODEC.fieldOf("target").forGetter((config) -> config.target),
                Codec.FLOAT.fieldOf("vein_size").forGetter((config) -> config.veinSize),
                Codec.FLOAT.fieldOf("primary_ore_density").forGetter((config) -> config.primaryOreDensity),
                Codec.FLOAT.fieldOf("rare_block_chance").forGetter((config) -> config.rareBlockChance)
        ).apply(instance, OreVeinConfiguration::new);
    });

}
