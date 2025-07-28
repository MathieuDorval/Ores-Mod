package com.ores.datagen;

import com.ores.OresMod;
import com.ores.core.Materials;
import com.ores.core.Variants;
import com.ores.registries.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
    }

    // Runner class to integrate with the data generation system
    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new ModRecipeProvider(provider, recipeOutput);
        }

        @Override
        public @NotNull String getName() {
            return "Ores Recipes";
        }
    }

    @Override
    protected void buildRecipes() {
        for (Materials material : Materials.values()) {
            // --- SETUP ---
            Item baseItem = getItem(material.getIdBase());
            if (baseItem == null) continue;

            Item nuggetItem = getItem(Variants.NUGGET.getFormattedId(material.getId()));
            Item rawItem = getItem(Variants.RAW.getFormattedId(material.getId()));
            ItemLike storageBlock = getItem(Variants.BLOCK.getFormattedId(material.getId()));
            ItemLike rawStorageBlock = getItem(Variants.RAW_BLOCK.getFormattedId(material.getId()));

            // --- SMELTING ---
            List<ItemLike> materialSmeltables = new ArrayList<>();
            if (rawItem != null) {
                materialSmeltables.add(rawItem);
            }
            // Dynamically find all ore variants for the current material
            for (Variants variant : Variants.values()) {
                if (variant.getCategory() == Variants.Category.ORE || variant.getCategory() == Variants.Category.FALLING_ORE) {
                    Item oreItem = getItem(variant.getFormattedId(material.getId()));
                    if (oreItem != null) {
                        materialSmeltables.add(oreItem);
                    }
                }
            }

            // -> Item Recipes
            if (!materialSmeltables.isEmpty()) {
                oreSmelting(output, materialSmeltables, RecipeCategory.MISC, baseItem, 0.7f, 200, material.getId());
                oreBlasting(output, materialSmeltables, RecipeCategory.MISC, baseItem, 0.7f, 100, material.getId());
            }
            // -> Block Recipes (e.g., smelting raw ore blocks)
            if (storageBlock != null && rawStorageBlock != null) {
                oreSmelting(output, List.of(rawStorageBlock), RecipeCategory.BUILDING_BLOCKS, storageBlock, 0.7f, 1800, material.getId());
                oreBlasting(output, List.of(rawStorageBlock), RecipeCategory.BUILDING_BLOCKS, storageBlock, 0.7f, 900, material.getId());
            }

            // --- COMPRESSION / DECOMPRESSION ---
            // nuggets <-> baseItem
            if (nuggetItem != null) {
                shapeless(RecipeCategory.MISC, nuggetItem, 9).requires(baseItem).unlockedBy(getHasName(baseItem), has(baseItem)).save(output, OresMod.MODID + ":" + getItemName(nuggetItem) + "_from_" + getItemName(baseItem));
                shaped(RecipeCategory.MISC, baseItem, 1).pattern("NNN").pattern("NNN").pattern("NNN").define('N', nuggetItem).unlockedBy(getHasName(nuggetItem), has(nuggetItem)).save(output, OresMod.MODID + ":" + getItemName(baseItem) + "_from_" + getItemName(nuggetItem));
            }

            // Block <-> baseItem
            if (storageBlock != null) {
                shapeless(RecipeCategory.MISC, baseItem, 9).requires(storageBlock).unlockedBy(getHasName(storageBlock), has(storageBlock)).save(output, OresMod.MODID + ":" + getItemName(baseItem) + "_from_" + getItemName(storageBlock));
                shaped(RecipeCategory.BUILDING_BLOCKS, storageBlock, 1).pattern("BBB").pattern("BBB").pattern("BBB").define('B', baseItem).unlockedBy(getHasName(baseItem), has(baseItem)).save(output, OresMod.MODID + ":" + getItemName(storageBlock) + "_from_" + getItemName(baseItem));
            }

            // Raw Block <-> Raw Item
            if (rawStorageBlock != null && rawItem != null) {
                shapeless(RecipeCategory.MISC, rawItem, 9).requires(rawStorageBlock).unlockedBy(getHasName(rawStorageBlock), has(rawStorageBlock)).save(output, OresMod.MODID + ":" + getItemName(rawItem) + "_from_" + getItemName(rawStorageBlock));
                shaped(RecipeCategory.BUILDING_BLOCKS, rawStorageBlock, 1).pattern("RRR").pattern("RRR").pattern("RRR").define('R', rawItem).unlockedBy(getHasName(rawItem), has(rawItem)).save(output, OresMod.MODID + ":" + getItemName(rawStorageBlock) + "_from_" + getItemName(rawItem));
            }
        }
    }

    // Helper method to safely get an item from its ID
    private static Item getItem(String id) {
        if (id == null) return null;

        if (id.startsWith("minecraft:")) {
            // CORRECTION: BuiltInRegistries.ITEM.get() returns an Optional<Holder.Reference<Item>>.
            // We need to unwrap the Optional and then the Holder to get the actual Item.
            return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id)).map(Holder::value).orElse(null);
        } else {
            // The ID for modded items should not contain the namespace when looking up in the map.
            String path = id.startsWith(OresMod.MODID + ":") ? id.substring(OresMod.MODID.length() + 1) : id;
            Supplier<? extends Item> supplier = ModItems.getItem(path);
            return supplier != null ? supplier.get() : null;
        }
    }

    // --- COOKING RECIPE HELPERS ---
    protected void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                               float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                               float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory)
                    .group(pGroup)
                    .unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, OresMod.MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
