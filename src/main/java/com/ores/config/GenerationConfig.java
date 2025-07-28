package com.ores.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.ores.OresMod;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Charge une liste d'ID d'items et de blocs à générer à partir d'un fichier JSON.
 */
public class GenerationConfig {
    private static final Set<String> GENERATION_LIST = new HashSet<>();
    private static boolean isLoaded = false;

    /**
     * Charge le fichier generation_list.json depuis les ressources.
     */
    public static void load() {
        if (isLoaded) return;

        Gson gson = new Gson();
        String path = "/data/" + OresMod.MODID + "/list.json";
        try (InputStream inputStream = GenerationConfig.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                System.err.println("CRITICAL: Could not find list.json at path: " + path);
                return;
            }

            InputStreamReader reader = new InputStreamReader(inputStream);
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            Type listType = new TypeToken<List<String>>() {}.getType();
            List<String> ids = gson.fromJson(jsonObject.get("generate"), listType);

            if (ids != null) {
                GENERATION_LIST.addAll(ids);
                System.out.println("Loaded " + GENERATION_LIST.size() + " items/blocks to generate from config.");
            }
        } catch (Exception e) {
            System.err.println("CRITICAL: Failed to load or parse generation_list.json");
            e.printStackTrace();
        }
        isLoaded = true;
    }

    /**
     * Vérifie si un ID de ressource est dans la liste de génération.
     * @param id L'ID à vérifier (ex: "iron_ingot").
     * @return true si l'ID doit être généré, false sinon.
     */
    public static boolean shouldGenerate(String id) {
        if (!isLoaded) {
            load();
        }
        return GENERATION_LIST.contains(id);
    }
}
