package com.ores.core;

import net.minecraft.world.level.block.Block;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Registry {
    public record BlockRegistryEntry(String ID, Variants.Category category, Function<BlockBehaviour.Properties, ? extends Block> blockConstructor, BlockBehaviour.Properties properties, Item.Properties itemProperties) {}
    public record ItemRegistryEntry(String ID, Variants.Category category, Item.Properties properties) {}

    public static final List<BlockRegistryEntry> BLOCKS_TO_REGISTER = new ArrayList<>();
    public static final List<ItemRegistryEntry> ITEMS_TO_REGISTER = new ArrayList<>();

    public static void initialize() {
        BLOCKS_TO_REGISTER.clear();
        ITEMS_TO_REGISTER.clear();

        for (Materials material : Materials.values()) {
            for (Variants variant : material.getVariants()) {
                String id = variant.getFormattedId(material.getId());
                var category = variant.getCategory();

                if (category == Variants.Category.ITEM) {
                    Item.Properties itemProperties = new Item.Properties();
                    combineItemProperties(itemProperties, material.getItemProps(), variant.getItemProps());
                    // MODIFICATION: Passe la catégorie lors de la création
                    ITEMS_TO_REGISTER.add(new ItemRegistryEntry(id, category, itemProperties));
                } else {
                    BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties.of();
                    Item.Properties blockItemProperties = new Item.Properties();
                    Function<BlockBehaviour.Properties, ? extends Block> blockConstructor;

                    if (category == Variants.Category.BLOCK || category == Variants.Category.FALLING_BLOCK) {
                        combineBlockProperties(blockProperties, material.getBlockProps(), Objects.requireNonNull(variant.getBlockProps()));
                        combineItemProperties(blockItemProperties, material.getItemProps(), null);
                        blockConstructor = (category == Variants.Category.FALLING_BLOCK) ?
                                (props) -> new CustomBlocks.CustomFallingBlock(props, Objects.requireNonNullElse(variant.getBlockProps().dropsOnFalling(), true)) :
                                Block::new;
                    } else { // ORE ou FALLING_ORE
                        combineOreProperties(blockProperties, material.getBlockProps(), Objects.requireNonNull(variant.getOreProps()));
                        combineItemProperties(blockItemProperties, material.getItemProps(), null);

                        boolean isRedstoneLike = material.getOreProps().isRedstoneLike() != null && material.getOreProps().isRedstoneLike();
                        int minXp = Objects.requireNonNullElse(material.getOreProps().minXp(), 0);
                        int maxXp = Objects.requireNonNullElse(material.getOreProps().maxXp(), 0);
                        UniformInt xpRange = UniformInt.of(minXp, maxXp);
                        int particleColor = material.getBlockProps().mapColor() != null ? material.getBlockProps().mapColor().col : 0xFF0000;

                        if (isRedstoneLike && category == Variants.Category.FALLING_ORE) {
                            blockConstructor = (props) -> new CustomBlocks.CustomFallingRedstoneOreBlock(props, xpRange, particleColor);
                        } else if (isRedstoneLike) {
                            blockConstructor = (props) -> new CustomBlocks.CustomRedstoneOreBlock(props, xpRange, particleColor);
                        } else if (category == Variants.Category.FALLING_ORE) {
                            blockConstructor = (props) -> new CustomBlocks.CustomFallingOreBlock(props, xpRange);
                        } else {
                            blockConstructor = (props) -> new DropExperienceBlock(xpRange, props);
                        }
                    }
                    // MODIFICATION: Passe la catégorie lors de la création
                    BLOCKS_TO_REGISTER.add(new BlockRegistryEntry(id, category, blockConstructor, blockProperties, blockItemProperties));
                }
            }
        }
    }

    private static void combineItemProperties(Item.Properties props, Materials.ItemProps matProps, @Nullable Variants.ItemProps varProps) {
        if (varProps == null) {
            if (matProps.maxStackSize() != null) props.stacksTo(matProps.maxStackSize());
            if (matProps.rarity() != null) props.rarity(matProps.rarity());
            if (matProps.isFireResistant() != null && matProps.isFireResistant()) props.fireResistant();
            return;
        }

        if (matProps.maxStackSize() != null && varProps.maxStackSize() != null) {
            props.stacksTo(Math.min(matProps.maxStackSize(), varProps.maxStackSize()));
        }
        if (matProps.rarity() != null && varProps.rarity() != null) {
            props.rarity(matProps.rarity().ordinal() > varProps.rarity().ordinal() ? matProps.rarity() : varProps.rarity());
        }
        if (matProps.isFireResistant() != null && varProps.isFireResistant() != null) {
            if (matProps.isFireResistant() || varProps.isFireResistant()) {
                props.fireResistant();
            }
        }
    }

    private static void combineBlockProperties(BlockBehaviour.Properties props, Materials.BlockProps matProps, Variants.BlockProps varProps) {
        props.strength(matProps.destroyTime() + varProps.destroyTime(), matProps.explosionResistance() + varProps.explosionResistance());

        if (matProps.requiresCorrectToolForDrops() != null && varProps.requiresCorrectToolForDrops() != null) {
            if (matProps.requiresCorrectToolForDrops() || varProps.requiresCorrectToolForDrops()) {
                props.requiresCorrectToolForDrops();
            }
        }
        if (matProps.lightLevel() != null && varProps.lightLevel() != null && (matProps.lightLevel() > 0 || varProps.lightLevel() > 0)) {
            props.lightLevel(state -> Math.max(matProps.lightLevel(), varProps.lightLevel()));
        }
        if (matProps.friction() != null && varProps.friction() != null) {
            props.friction(Math.max(matProps.friction(), varProps.friction()));
        }
        if (matProps.jumpFactor() != null && varProps.jumpFactor() != null) {
            props.jumpFactor(Math.max(matProps.jumpFactor(), varProps.jumpFactor()));
        }
        if (matProps.speedFactor() != null && varProps.speedFactor() != null) {
            props.speedFactor(Math.max(matProps.speedFactor(), varProps.speedFactor()));
        }

        props.sound(Objects.requireNonNullElse(matProps.soundType(), SoundType.STONE));
        props.mapColor(Objects.requireNonNullElse(matProps.mapColor(), MapColor.STONE));
        props.instrument(Objects.requireNonNullElse(matProps.instrument(), NoteBlockInstrument.BASEDRUM));
        if (varProps.pushReaction() != null) props.pushReaction(varProps.pushReaction());
    }

    private static void combineOreProperties(BlockBehaviour.Properties props, Materials.BlockProps matProps, Variants.OreProps varProps) {
        props.strength(matProps.destroyTime() + varProps.destroyTime(), matProps.explosionResistance() + varProps.explosionResistance());
        props.requiresCorrectToolForDrops();

        int combinedLight = 0;
        if (matProps.lightLevel() != null && varProps.lightLevel() != null && (matProps.lightLevel() > 0 || varProps.lightLevel() > 0)) {
            combinedLight = Math.max(matProps.lightLevel(), varProps.lightLevel());
        }
        if (combinedLight > 0) {
            int finalLight = combinedLight;
            props.lightLevel(state -> finalLight);
        }

        props.sound(varProps.soundType());
        props.mapColor(varProps.mapColor());
        props.instrument(varProps.instrument());
    }
}
