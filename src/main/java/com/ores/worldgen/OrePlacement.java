package com.ores.worldgen;

import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;

/**
 * Classe utilitaire contenant des méthodes pour créer des listes de modificateurs de placement.
 * C'est une simplification pour éviter de répéter le même code.
 */
public class OrePlacement {

    /**
     * Crée une liste de modificateurs pour un minerai commun.
     * @param count Le nombre de filons par chunk.
     * @param heightPlacement Le modificateur de hauteur.
     * @return Une liste de PlacementModifier.
     */
    public static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightPlacement) {
        return List.of(CountPlacement.of(count), InSquarePlacement.spread(), heightPlacement, BiomeFilter.biome());
    }

    /**
     * Crée une liste de modificateurs pour un minerai rare.
     * @param chance La chance (1 sur 'chance') qu'un filon apparaisse dans un chunk.
     * @param heightPlacement Le modificateur de hauteur.
     * @return Une liste de PlacementModifier.
     */
    public static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightPlacement) {
        return List.of(RarityFilter.onAverageOnceEvery(chance), InSquarePlacement.spread(), heightPlacement, BiomeFilter.biome());
    }
}
