package com.ores.registries;

import com.ores.OresMod;
import com.ores.worldgen.OreVeinConfiguration;
import com.ores.worldgen.OreVeinFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {

    // Crée un DeferredRegister pour les features. C'est la manière moderne (Forge/NeoForge)
    // de gérer les enregistrements pour qu'ils se fassent au bon moment.
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, OresMod.MODID);

    // Enregistre notre feature de veine de minerai géante.
    // Utilise DeferredHolder qui est le type de retour correct pour la méthode register.
    public static final DeferredHolder<Feature<?>, Feature<OreVeinConfiguration>> GIANT_ORE_VEIN =
            FEATURES.register("giant_ore_vein", () -> new OreVeinFeature(OreVeinConfiguration.CODEC));

}
