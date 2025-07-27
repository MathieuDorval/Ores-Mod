package com.ores.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public enum Variants {
    // --- Minerais ---
    STONE_ORE("{material}_ore", Category.ORE, "minecraft:stone", 0, List.of("c:ores", "c:{material}_ores", "minecraft:mineable/pickaxe"),
            props -> props.sound(SoundType.STONE).mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0f, 3.0f).requiresCorrectToolForDrops()
    ),
    DEEPSLATE_ORE("deepslate_{material}_ore", Category.ORE, "minecraft:deepslate", 0, List.of("c:ores", "c:{material}_ores", "minecraft:mineable/pickaxe"),
            props -> props.sound(SoundType.DEEPSLATE).mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0f, 3.0f).requiresCorrectToolForDrops()
    ),
    GRANITE_ORE("granite_{material}_ore", Category.ORE, "minecraft:granite", 0, List.of("c:ores", "c:{material}_ores", "minecraft:mineable/pickaxe"),
            props -> props.sound(SoundType.STONE).mapColor(MapColor.TERRACOTTA_RED).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0f, 3.0f).requiresCorrectToolForDrops()
    ),
    GRAVEL_ORE("gravel_{material}_ore", Category.ORE, "minecraft:gravel", 0, List.of("c:ores", "c:{material}_ores", "minecraft:mineable/shovel"),
            props -> props.sound(SoundType.GRAVEL).mapColor(MapColor.TERRACOTTA_GRAY).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0f, 3.0f).requiresCorrectToolForDrops()
    ),

    // --- Items ---
    SELF("{material}", Category.ITEM,
            null
    ),
    INGOT("{material}_ingot", Category.ITEM,
            null
    ),
    NUGGET("{material}_nugget", Category.ITEM,
            props -> props.stacksTo(16).rarity(Rarity.RARE).fireResistant()
    ),

    // --- Blocs ---
    BLOCK("{material}_block", Category.BLOCK, 0, List.of("c:storage_blocks", "minecraft:mineable/pickaxe"),
            props -> props.strength(0.0f, 0.0f)
    ),
    RAW_BLOCK("raw_{material}_block", Category.BLOCK, 0, List.of("c:storage_blocks", "minecraft:mineable/pickaxe"),
            props -> props.strength(0.0f, 0.0f)
    );


    private final String idTemplate;
    private final Category category;
    @Nullable private final String baseBlockId;
    private final int toolLevel;
    private final List<String> tags;
    @Nullable private final Consumer<BlockBehaviour.Properties> blockPropertiesConfig;
    @Nullable private final Consumer<Item.Properties> itemPropertiesConfig;

    // Constructeur principal (privé)
    private Variants(String idTemplate, Category category, @Nullable String baseBlockId, int toolLevel, List<String> tags, @Nullable Consumer<BlockBehaviour.Properties> blockConfig, @Nullable Consumer<Item.Properties> itemConfig) {
        this.idTemplate = idTemplate;
        this.category = category;
        this.baseBlockId = baseBlockId;
        this.toolLevel = toolLevel;
        this.tags = tags;
        this.blockPropertiesConfig = blockConfig;
        this.itemPropertiesConfig = itemConfig;
    }

    // --- Constructeurs publics et spécifiques ---

    /**
     * Constructeur pour les variantes de type ORE.
     */
    Variants(String idTemplate, Category category, String baseBlockId, int toolLevel, List<String> tags, Consumer<BlockBehaviour.Properties> blockConfig) {
        this(idTemplate, category, baseBlockId, toolLevel, tags, blockConfig, null);
    }

    /**
     * Constructeur pour les variantes de type BLOCK.
     */
    Variants(String idTemplate, Category category, int toolLevel, List<String> tags, Consumer<BlockBehaviour.Properties> blockConfig) {
        this(idTemplate, category, null, toolLevel, tags, blockConfig, null);
    }

    /**
     * Constructeur pour les variantes de type ITEM.
     */
    Variants(String idTemplate, Category category, Consumer<Item.Properties> itemConfig) {
        this(idTemplate, category, null, -1, Collections.emptyList(), null, itemConfig);
    }

    public String getFormattedId(@NotNull String materialName) {
        return this.idTemplate.replace("{material}", materialName);
    }

    public List<String> getFormattedTags(@NotNull String materialName) {
        if (this.tags.isEmpty()) return Collections.emptyList();
        return this.tags.stream().map(tag -> tag.replace("{material}", materialName)).collect(Collectors.toList());
    }

    // Getters
    public String getIdTemplate() { return idTemplate; }
    public Category getCategory() { return category; }
    @Nullable public String getBaseBlockId() { return baseBlockId; }
    public int getToolLevel() { return toolLevel; }
    public List<String> getTags() { return tags; }
    @Nullable public Consumer<BlockBehaviour.Properties> getBlockPropertiesConfig() { return blockPropertiesConfig; }
    @Nullable public Consumer<Item.Properties> getItemPropertiesConfig() { return itemPropertiesConfig; }

    public enum Category { ITEM, BLOCK, ORE }
}
