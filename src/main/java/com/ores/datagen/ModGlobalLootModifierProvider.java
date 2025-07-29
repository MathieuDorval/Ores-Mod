package com.ores.datagen;

import com.ores.OresMod;
import com.ores.unification.UnificationLootModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, OresMod.MODID);
    }

    @Override
    protected void start() {
        // Ajoute un modificateur unique qui s'applique à toutes les tables de butin.
        // La logique de remplacement est gérée à l'intérieur de la classe du modificateur.
        add("unify_outputs", new UnificationLootModifier(new LootItemCondition[]{}));
    }
}
