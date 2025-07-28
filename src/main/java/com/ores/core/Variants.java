package com.ores.core;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum Variants {

    // --- Items ---
    SELF("%s", Category.ITEM,
            new ItemProps(64, Rarity.COMMON, false, false, 0.0f, 0.0f, false, true, 100, true, ColorType.BASE)
    ),
    INGOT("%s_ingot", Category.ITEM,
            new ItemProps(64, Rarity.COMMON, false, null, null, null, false, false, 100, true, ColorType.BASE)
    ),
    RAW("raw_%s", Category.ITEM,
            new ItemProps(null, null, null, null, null, null, false, false, null, false, ColorType.RAW)
    ),
    NUGGET("%s_nugget", Category.ITEM,
            new ItemProps(16, Rarity.RARE, true, null, null, null, false, false, 12, false, ColorType.BASE)
    ),

    // --- Blocs ---
    BLOCK("%s_block", Category.BLOCK,
            new BlockProps(5.0f, 6.0f, true, 0, null, null, null, null, null, null, 64, 0, Materials.Tools.PICKAXE, 900, true, ColorType.BASE)
    ),
    RAW_BLOCK("raw_%s_block", Category.BLOCK,
            new BlockProps(5.0f, 6.0f, true, 0, null, null, null, null, null, null, null, 0, Materials.Tools.PICKAXE, null, false, ColorType.RAW)
    ),
    DUST_BLOCK("dust_%s_block", Category.FALLING_BLOCK,
            new BlockProps(0.5f, 0.5f, true, 0, null, null, null, null, PushReaction.DESTROY, true, 32, 0, Materials.Tools.SHOVEL, null, false, ColorType.BASE)
    ),

    // --- Minerais ---
    STONE_ORE("%s_ore", Category.ORE,
            new OreProps(3.0f, 3.0f, SoundType.STONE, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, null, 0, "minecraft:stone", ColorType.BASE)
    ),
    DEEPSLATE_ORE("deepslate_%s_ore", Category.ORE,
            new OreProps(4.5f, 3.0f, SoundType.DEEPSLATE, MapColor.DEEPSLATE, NoteBlockInstrument.BASEDRUM, null, null, 0, "minecraft:deepslate", ColorType.BASE)
    ),
    GRAVEL_ORE("gravel_%s_ore", Category.FALLING_ORE,
            new OreProps(0.6f, 0.6f, SoundType.GRAVEL, MapColor.STONE, NoteBlockInstrument.BASEDRUM, null, null, 0, "minecraft:gravel", ColorType.BASE)
    );


    // --- Enums Internes ---
    public enum Category { ITEM, BLOCK, ORE, FALLING_BLOCK, FALLING_ORE }
    public enum ColorType { BASE, RAW }

    // --- Records pour les configurations ---
    public record ItemProps(
            @Nullable Integer maxStackSize, @Nullable Rarity rarity, @Nullable Boolean isFireResistant,
            @Nullable Boolean isFood, @Nullable Float nutrition, @Nullable Float saturationModifier, @Nullable Boolean alwaysEdible,
            Boolean trimable, @Nullable Integer burnTime, Boolean beacon, ColorType colorType
    ) {}

    public record BlockProps(
            Float destroyTime, Float explosionResistance, @Nullable Boolean requiresCorrectToolForDrops,
            @Nullable Integer lightLevel, @Nullable Float friction, @Nullable Float jumpFactor, @Nullable Float speedFactor,
            @Nullable Integer redstonePower, @Nullable PushReaction pushReaction, @Nullable Boolean dropsOnFalling,
            @Nullable Integer maxStackSize, Integer toolLevel, Materials.Tools tool, @Nullable Integer burnTime, Boolean beacon, ColorType colorType
    ) {}

    public record OreProps(
            Float destroyTime, Float explosionResistance, SoundType soundType, MapColor mapColor,
            NoteBlockInstrument instrument, @Nullable Integer lightLevel, @Nullable Integer redstonePower,
            Integer toolLevel, String idStone, ColorType colorType
    ) {}


    // --- Variables de l'énumération ---
    private final String idFormat;
    private final Category category;
    @Nullable private final ItemProps itemProps;
    @Nullable private final BlockProps blockProps;
    @Nullable private final OreProps oreProps;


    // --- Constructeurs ---
    Variants(String idFormat, Category category, @Nullable ItemProps itemProps, @Nullable BlockProps blockProps, @Nullable OreProps oreProps) {
        this.idFormat = idFormat;
        this.category = category;
        this.itemProps = itemProps;
        this.blockProps = blockProps;
        this.oreProps = oreProps;
    }

    // Constructeur pour ITEM
    Variants(String idFormat, Category category, ItemProps itemProps) {
        this(idFormat, category, itemProps, null, null);
    }

    // Constructeur pour BLOCK et FALLING_BLOCK
    Variants(String idFormat, Category category, BlockProps blockProps) {
        this(idFormat, category, null, blockProps, null);
    }

    // Constructeur pour ORE et FALLING_ORE
    Variants(String idFormat, Category category, OreProps oreProps) {
        this(idFormat, category, null, null, oreProps);
    }


    // --- Getters ---
    public String getFormattedId(@NotNull String materialName) {
        return String.format(idFormat, materialName);
    }
    public Category getCategory() { return category; }
    @Nullable public ItemProps getItemProps() { return itemProps; }
    @Nullable public BlockProps getBlockProps() { return blockProps; }
    @Nullable public OreProps getOreProps() { return oreProps; }
}
