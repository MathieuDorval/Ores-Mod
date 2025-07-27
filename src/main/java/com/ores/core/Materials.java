package com.ores.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Définit tous les matériaux à ajouter par le mod.
 * Chaque matériau a un ID unique, un niveau de minage,
 * et peut avoir des configurations personnalisées pour ses blocs et items.
 */
public enum Materials {

    // id, miningLevel, blockPropertiesConfig, itemPropertiesConfig
    COAL("coal", 0,
            blockProps -> blockProps.strength(2.0f, 3.0f),
            null
    ),
    COPPER("copper", 1,
            blockProps -> blockProps.strength(3.0f, 5.0f).sound(SoundType.COPPER),
            null
    ),
    IRON("iron", 1,
            blockProps -> blockProps.strength(5.0f, 6.0f).sound(SoundType.METAL),
            null
    ),
    LAPIS("lapis", 1,
            blockProps -> blockProps.strength(3.0f, 3.0f),
            null
    ),
    GOLD("gold", 2,
            blockProps -> blockProps.strength(3.0f, 6.0f).sound(SoundType.METAL),
            null
    ),
    REDSTONE("redstone", 2,
            blockProps -> blockProps.strength(3.0f, 3.0f),
            null
    ),
    EMERALD("emerald", 2,
            blockProps -> blockProps.strength(3.0f, 9.0f),
            null
    ),
    DIAMOND("diamond", 2,
            blockProps -> blockProps.strength(5.0f, 1200.0f),
            null
    ),
    QUARTZ("quartz", 1,
            blockProps -> blockProps.strength(3.0f, 3.0f),
            null
    ),
    NETHERITE("netherite", 3,
            blockProps -> blockProps.strength(50.0f, 1200.0f),
            itemProps -> itemProps.fireResistant().stacksTo(32)
    );

    private final String id;
    private final int miningLevel;
    private final Consumer<BlockBehaviour.Properties> blockPropertiesConfig;
    private final Consumer<Item.Properties> itemPropertiesConfig;

    /**
     * Constructeur principal pour un nouveau matériau.
     * @param id Le nom de base pour le matériau.
     * @param miningLevel Le niveau d'outil minimum requis.
     * @param blockPropertiesConfig Une fonction pour configurer les propriétés du bloc.
     * @param itemPropertiesConfig Une fonction pour configurer les propriétés de l'item.
     */
    Materials(String id, int miningLevel, Consumer<BlockBehaviour.Properties> blockPropertiesConfig, Consumer<Item.Properties> itemPropertiesConfig) {
        this.id = id;
        this.miningLevel = miningLevel;
        this.blockPropertiesConfig = blockPropertiesConfig;
        this.itemPropertiesConfig = itemPropertiesConfig;
    }


    /**
     * Constructeur pour les matériaux qui n'ont pas de configuration d'item spéciale.
     */
    Materials(String id, int miningLevel, Consumer<BlockBehaviour.Properties> blockPropertiesConfig) {
        this(id, miningLevel, blockPropertiesConfig, itemProps -> {});
    }

    /**
     * Constructeur pour les matériaux sans configuration spéciale.
     */
    Materials(String id, int miningLevel) {
        this(id, miningLevel, blockProps -> {}, itemProps -> {});
    }


    public String getId() {
        return id;
    }

    public int getMiningLevel() {
        return miningLevel;
    }

    public Consumer<BlockBehaviour.Properties> getBlockPropertiesConfig() {
        return blockPropertiesConfig;
    }

    public Consumer<Item.Properties> getItemPropertiesConfig() {
        return itemPropertiesConfig;
    }

    public List<Variants> getVariants() {
        return Arrays.asList(Variants.values());
    }
}
