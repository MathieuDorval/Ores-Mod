package com.ores.datagen;

import com.ores.OresMod;
import com.ores.core.Materials;
import com.ores.core.Variants;
import com.ores.registries.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
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
            return "ORES Recipes";
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
            for (Variants variant : Variants.values()) {
                if (variant.getCategory() == Variants.Category.ORE || variant.getCategory() == Variants.Category.FALLING_ORE) {
                    Item oreItem = getItem(variant.getFormattedId(material.getId()));
                    if (oreItem != null) {
                        materialSmeltables.add(oreItem);
                    }
                }
            }

            if (!materialSmeltables.isEmpty()) {
                oreSmelting(output, materialSmeltables, RecipeCategory.MISC, baseItem, 0.7f, 200, material.getId());
                oreBlasting(output, materialSmeltables, RecipeCategory.MISC, baseItem, 0.7f, 200, material.getId());

            }
            if (storageBlock != null && rawStorageBlock != null) {
                oreSmelting(output, List.of(rawStorageBlock), RecipeCategory.BUILDING_BLOCKS, storageBlock, 0.7f, 1800, material.getId());
                oreBlasting(output, List.of(rawStorageBlock), RecipeCategory.BUILDING_BLOCKS, storageBlock, 0.7f, 900, material.getId());
            }

            // --- COMPRESSION / DECOMPRESSION ---
            if (nuggetItem != null) {
                nineBlockStorageRecipes(RecipeCategory.MISC, nuggetItem, RecipeCategory.MISC, baseItem,
                        OresMod.MODID + ":" + getItemName(baseItem) + "_from_" + getItemName(nuggetItem), null,
                        OresMod.MODID + ":" + getItemName(nuggetItem) + "_from_" + getItemName(baseItem), null);
            }
            if (storageBlock != null) {
                nineBlockStorageRecipes(RecipeCategory.MISC, baseItem, RecipeCategory.BUILDING_BLOCKS, storageBlock,
                        OresMod.MODID + ":" + getItemName(storageBlock) + "_from_" + getItemName(baseItem), null,
                        OresMod.MODID + ":" + getItemName(baseItem) + "_from_" + getItemName(storageBlock), null);
            }
            if (rawStorageBlock != null && rawItem != null) {
                nineBlockStorageRecipes(RecipeCategory.MISC, rawItem, RecipeCategory.BUILDING_BLOCKS, rawStorageBlock,
                        OresMod.MODID + ":" + getItemName(rawStorageBlock) + "_from_" + getItemName(rawItem), null,
                        OresMod.MODID + ":" + getItemName(rawItem) + "_from_" + getItemName(rawStorageBlock), null);
            }
        }
    }

    // Méthode utilitaire pour récupérer un item de manière sûre
    private static Item getItem(String id) {
        if (id == null) return null;
        Supplier<? extends Item> supplier = ModItems.getItem(id);
        return supplier != null ? supplier.get() : null;
    }

    // --- MÉTHODES D'ASSISTANCE POUR LES RECETTES DE CUISSON ---
    // Ces méthodes surchargent celles de la classe parente pour garantir que le nom de la recette inclut notre MODID.
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
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, OresMod.MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
