package com.ores.core;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Définit tous les matériaux à ajouter par le mod.
 * Chaque propriété est maintenant une variable distincte pour permettre une combinaison flexible.
 * La structure utilise des records imbriqués pour une meilleure organisation.
 */
public enum Materials {

    // CORRECTION: idDrop a été mis à jour pour ne pas inclure de préfixe et pour utiliser "self" quand c'est approprié.
    COAL("coal", "minecraft:coal",
            new ItemProps(64, null, false, null, null, null, false, true, 1600, false, MaterialTexture.COAL, MaterialIngotTexture.COPPER, RawMaterialTexture.COPPER, MaterialNuggetTexture.IRON, MaterialScrapTexture.NETHERITE),
            new BlockProps(0.0f, 0.0f, SoundType.STONE, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, 0, null, null, null, 0, null, 0, Tools.PICKAXE, MaterialBlockTexture.COAL, RawMaterialBlockTexture.IRON),
            new OreProps(0.0f, 0.0f, 0, 2, false, 0, null, 0, 1, 1, "minecraft:coal", OreOverlayTexture.COAL),
            new VanillaExclusions(List.of("coal","coal_block"))
            ),
    COPPER("copper", "minecraft:copper_ingot",
            new ItemProps(64, Rarity.COMMON, false, null, null, null, false, true, null, false, MaterialTexture.COAL, MaterialIngotTexture.COPPER, RawMaterialTexture.COPPER, MaterialNuggetTexture.IRON, MaterialScrapTexture.NETHERITE),
            new BlockProps(0.0f, 0.0f, SoundType.COPPER, MapColor.COLOR_ORANGE, NoteBlockInstrument.BIT, false, 0, null, null, null, null, null, 1, Tools.PICKAXE, MaterialBlockTexture.IRON, RawMaterialBlockTexture.COPPER),
            new OreProps(0.0f, 0.0f, 0, 0, false, 0, null, 1, 2, 5, "minecraft:raw_copper", OreOverlayTexture.COPPER),
            new VanillaExclusions(List.of("copper_ingot", "raw_copper", "copper_block", "raw_copper_block"))
    ),
    IRON("iron", "minecraft:iron_ingot",
            new ItemProps(64, Rarity.COMMON, false, null, null, null, false, true, null, true, MaterialTexture.COAL, MaterialIngotTexture.IRON, RawMaterialTexture.IRON, MaterialNuggetTexture.IRON, MaterialScrapTexture.NETHERITE),
            new BlockProps(0.0f, 0.0f, SoundType.METAL, MapColor.METAL, NoteBlockInstrument.IRON_XYLOPHONE, true, 0, null, null, null, null, null, 1, Tools.PICKAXE, MaterialBlockTexture.IRON, RawMaterialBlockTexture.IRON),
            new OreProps(0.0f, 0.0f, 0, 0, false, 0, null, 1, 1, 1, "minecraft:raw_iron", OreOverlayTexture.IRON),
            new VanillaExclusions(List.of("iron_ingot", "iron_nugget", "raw_iron", "iron_block", "raw_iron_block"))
    ),
    LAPIS("lapis", "minecraft:lapis_lazuli",
            new ItemProps(64, Rarity.COMMON, null, null, null, null, false, true, null, false, MaterialTexture.LAPIS, MaterialIngotTexture.COPPER, RawMaterialTexture.COPPER, MaterialNuggetTexture.IRON, MaterialScrapTexture.NETHERITE),
            new BlockProps(0.0f, 0.0f, SoundType.STONE, MapColor.LAPIS, NoteBlockInstrument.BASEDRUM, true, 0, null, null, null, null, null, 1, Tools.PICKAXE, MaterialBlockTexture.LAPIS, RawMaterialBlockTexture.IRON),
            new OreProps(0.0f, 0.0f, 2, 5, false, 0, null, 1, 4, 9, "minecraft:lapis_lazuli", OreOverlayTexture.LAPIS),
            new VanillaExclusions(List.of("lapis", "lapis_block"))
    ),
    GOLD("gold", "minecraft:gold_ingot",
            new ItemProps(64, Rarity.COMMON, false, null, null, null, false, true, null, true, MaterialTexture.COAL, MaterialIngotTexture.GOLD, RawMaterialTexture.GOLD, MaterialNuggetTexture.GOLD, MaterialScrapTexture.NETHERITE),
            new BlockProps(0.0f, 0.0f, SoundType.METAL, MapColor.GOLD, NoteBlockInstrument.BELL, true, 0, null, null, null, null, null, 2, Tools.PICKAXE, MaterialBlockTexture.IRON, RawMaterialBlockTexture.GOLD),
            new OreProps(0.0f, 0.0f, 0, 0, false, 0, null, 2, 1, 1, "minecraft:raw_gold", OreOverlayTexture.GOLD),
            new VanillaExclusions(List.of("gold_ingot", "gold_nugget", "raw_gold", "gold_block", "raw_gold_block"))
    ),
    REDSTONE("redstone", "minecraft:redstone",
            new ItemProps(64, Rarity.COMMON, false, null, null, null, false, true, null, false, MaterialTexture.REDSTONE, MaterialIngotTexture.COPPER, RawMaterialTexture.COPPER, MaterialNuggetTexture.IRON, MaterialScrapTexture.NETHERITE),
            new BlockProps(0.0f, 0.0f, SoundType.STONE, MapColor.COLOR_RED, NoteBlockInstrument.BASEDRUM, true, 0, null, null, null, null, null, 2, Tools.PICKAXE, MaterialBlockTexture.IRON, RawMaterialBlockTexture.IRON),
            new OreProps(0.0f, 0.0f, 1, 5, true, 7, null, 2, 4, 5, "minecraft:redstone", OreOverlayTexture.REDSTONE),
            new VanillaExclusions(List.of("redstone", "redstone_block"))

    ),
    EMERALD("emerald", "minecraft:emerald",
            new ItemProps(64, Rarity.RARE, false, null, null, null, false, true, null, true, MaterialTexture.EMERALD, MaterialIngotTexture.COPPER, RawMaterialTexture.COPPER, MaterialNuggetTexture.IRON, MaterialScrapTexture.NETHERITE),
            new BlockProps(0.5f, 0.0f, SoundType.STONE, MapColor.EMERALD, NoteBlockInstrument.BASEDRUM, true, 0, null, null, null, null, null, 2, Tools.PICKAXE, MaterialBlockTexture.EMERALD, RawMaterialBlockTexture.IRON),
            new OreProps(0.5f, 0.0f, 3, 7, false, 0, null, 2, 1, 1, "minecraft:emerald", OreOverlayTexture.EMERALD),
            new VanillaExclusions(List.of("emerald", "emerald_block"))
    ),
    DIAMOND("diamond",  "minecraft:diamond",
            new ItemProps(64, Rarity.RARE, false, null, null, null, false, true, null, true, MaterialTexture.DIAMOND, MaterialIngotTexture.COPPER, RawMaterialTexture.COPPER, MaterialNuggetTexture.IRON, MaterialScrapTexture.NETHERITE),
            new BlockProps(0.5f, 0.0f, SoundType.STONE, MapColor.DIAMOND, NoteBlockInstrument.BASEDRUM, true, 0, null, null, null, null, null, 2, Tools.PICKAXE, MaterialBlockTexture.DIAMOND, RawMaterialBlockTexture.IRON),
            new OreProps(0.5f, 0.0f, 3, 7, false, 0, null, 2, 1, 1, "minecraft:diamond", OreOverlayTexture.DIAMOND),
            new VanillaExclusions(List.of("diamond", "diamond_block"))
    ),
    QUARTZ("quartz",  "minecraft:quartz",
            new ItemProps(64, Rarity.COMMON, false, false, 5.0f, 1.0f, false, true, null, false, MaterialTexture.QUARTZ, MaterialIngotTexture.COPPER, RawMaterialTexture.COPPER, MaterialNuggetTexture.IRON, MaterialScrapTexture.NETHERITE),
            new BlockProps(0.0f, 0.0f, SoundType.STONE, MapColor.QUARTZ, NoteBlockInstrument.BASEDRUM, true, 0, null, null, null, null, null, 0, Tools.PICKAXE, MaterialBlockTexture.IRON, RawMaterialBlockTexture.IRON),
            new OreProps(0.0f, 0.0f, 2, 5, false, 0, null, 0, 1, 1, "minecraft:quartz", OreOverlayTexture.QUARTZ),
            new VanillaExclusions(List.of("quartz", "quartz_block"))
    ),
    NETHERITE("netherite",  "minecraft:netherite_ingot",
            new ItemProps(64, Rarity.EPIC, true, null, null, null, false, true, 0, true, MaterialTexture.COAL, MaterialIngotTexture.NETHERITE, RawMaterialTexture.COPPER, MaterialNuggetTexture.IRON, MaterialScrapTexture.NETHERITE),
            new BlockProps(50.0f, 1200.0f, SoundType.NETHERITE_BLOCK, MapColor.COLOR_BLACK, NoteBlockInstrument.BASEDRUM, true, 0, null, null, null, null, null, 3, Tools.PICKAXE, MaterialBlockTexture.IRON, RawMaterialBlockTexture.IRON),
            new OreProps(30.0f, 1200.0f, 0, 0, false, 0, null, 3, 1, 1, "minecraft:netherite_ingot", OreOverlayTexture.DIAMOND),
            new VanillaExclusions(List.of("netherite_ingot", "netherite_block", "netherite_scrap"))
    );

    // --- Enums Internes ---
    public enum Tools { PICKAXE, SHOVEL, AXE, HOE, SWORD }
    public enum MaterialTexture { DIAMOND, EMERALD, COAL, LAPIS, REDSTONE, QUARTZ }
    public enum RawMaterialTexture { COPPER, IRON, GOLD }
    public enum MaterialIngotTexture { COPPER, IRON, GOLD, NETHERITE }
    public enum MaterialNuggetTexture { IRON, GOLD }
    public enum MaterialScrapTexture { NETHERITE }
    public enum MaterialBlockTexture { DIAMOND, EMERALD, COAL, LAPIS, IRON }
    public enum RawMaterialBlockTexture { COPPER, IRON, GOLD }
    public enum OreOverlayTexture { COAL, COPPER, DIAMOND, EMERALD, GOLD, IRON, LAPIS, REDSTONE, QUARTZ }

    public record ItemProps(
            @Nullable Integer maxStackSize, @Nullable Rarity rarity, @Nullable Boolean isFireResistant,
            @Nullable Boolean isFood, @Nullable Float nutrition, @Nullable Float saturationModifier, @Nullable Boolean alwaysEdible,
            Boolean trim, @Nullable Integer burnTime, Boolean beacon,
            MaterialTexture selfTexture, MaterialIngotTexture ingotTexture, RawMaterialTexture rawTexture, MaterialNuggetTexture nuggetTexture, MaterialScrapTexture scrapTexture
    ) {}

    public record BlockProps(
            Float destroyTime, Float explosionResistance, SoundType soundType, MapColor mapColor,
            NoteBlockInstrument instrument, @Nullable Boolean requiresCorrectToolForDrops, @Nullable Integer lightLevel,
            @Nullable Float friction, @Nullable Float jumpFactor, @Nullable Float speedFactor,
            @Nullable Integer redstonePower, @Nullable PushReaction pushReaction,
            Integer toolLevel, Tools tool, MaterialBlockTexture blockTexture, RawMaterialBlockTexture rawBlockTexture
    ) {}

    public record OreProps(
            Float destroyTime, Float explosionResistance, Integer minXp, Integer maxXp,
            Boolean isRedstoneLike, @Nullable Integer lightLevel, @Nullable Integer redstonePower,
            Integer toolLevel, Integer minDrop, Integer maxDrop, String idDrop,
            OreOverlayTexture overlay
    ) {}
    public record VanillaExclusions(
            @Nullable List<String> excludedVariantIds
    ) {}

    private final String id;
    private final String idBase;
    private final ItemProps itemProps;
    private final BlockProps blockProps;
    private final OreProps oreProps;
    private final VanillaExclusions vanillaExclusions;

    Materials(String id, String idBase, ItemProps itemProps, BlockProps blockProps, OreProps oreProps, VanillaExclusions vanillaExclusions) {
        this.id = id;
        this.idBase = idBase;
        this.itemProps = itemProps;
        this.blockProps = blockProps;
        this.oreProps = oreProps;
        this.vanillaExclusions = vanillaExclusions;
    }

    public String getId() { return id; }
    public String getIdBase() { return idBase; }
    public ItemProps getItemProps() { return itemProps; }
    public BlockProps getBlockProps() { return blockProps; }
    public OreProps getOreProps() { return oreProps; }
    public VanillaExclusions getVanillaExclusions() { return vanillaExclusions; }

    public List<Variants> getVariants() { return Arrays.asList(Variants.values()); }
}
