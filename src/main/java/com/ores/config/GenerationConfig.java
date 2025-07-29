package com.ores.config;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.ores.OresMod;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Loads a list of item and block IDs to generate from mod resources
 * and an external TOML config file, merging the lists.
 */
public class GenerationConfig {
    private static final Set<String> GENERATION_LIST = new HashSet<>();
    private static boolean isLoaded = false;

    /**
     * Loads and merges configurations from internal resources and the external config file.
     */
    public static void load() {
        if (isLoaded) return;

        // Step 1: Load from mod resources (internal JSON files)
        loadFromResources();

        // Step 2: Load from the external config file (in the /config directory)
        loadFromConfigFile();

        System.out.println("Loaded a total of " + GENERATION_LIST.size() + " items/blocks to generate from all configs.");
        isLoaded = true;
    }

    /**
     * Loads and merges all generation_list.json files found in the resources.
     */
    private static void loadFromResources() {
        Gson gson = new Gson();
        String path = "data/" + OresMod.MODID + "/generation_list.json";
        try {
            Enumeration<URL> resources = GenerationConfig.class.getClassLoader().getResources(path);

            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                System.out.println("Found internal generation_list.json at: " + url);
                try (InputStream inputStream = url.openStream()) {
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

                    Type listType = new TypeToken<List<String>>() {}.getType();
                    List<String> ids = gson.fromJson(jsonObject.get("generate"), listType);

                    if (ids != null) {
                        GENERATION_LIST.addAll(ids);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load or parse one or more internal generation_list.json files.");
            e.printStackTrace();
        }
    }

    /**
     * Loads the list from an ores-generate.toml file in the /config directory.
     * If the file does not exist, it is created with a default structure.
     */
    private static void loadFromConfigFile() {
        File configFile = new File("config/ores-generate.toml");

        // If the file doesn't exist, create it
        if (!configFile.exists()) {
            createDefaultConfigFile(configFile);
        }

        // Read the file using FileConfig for the TOML format
        System.out.println("Loading external config file: " + configFile.getAbsolutePath());
        try (FileConfig config = FileConfig.of(configFile)) {
            config.load();
            List<String> ids = config.get("generate");
            if (ids != null) {
                GENERATION_LIST.addAll(ids);
                System.out.println("Added " + ids.size() + " items/blocks from external config file.");
            }
        } catch (Exception e) {
            System.err.println("CRITICAL: Failed to load or parse external config file: " + configFile.getName());
            e.printStackTrace();
        }
    }

    /**
     * Creates a default TOML configuration file with comments.
     * @param configFile The file to create.
     */
    private static void createDefaultConfigFile(File configFile) {
        System.out.println("Config file not found. Creating default ores-generate.toml...");
        try {
            // Ensure the /config directory exists
            configFile.getParentFile().mkdirs();

            // Create the file with default content
            try (java.io.FileWriter writer = new java.io.FileWriter(configFile)) {
                writer.write("# List of item and block IDs that the Ores mod should generate.\n");
                writer.write("# Ores are always generated and do not need to be listed here.\n");
                writer.write("# Vanilla items essential for mod compatibility (like iron_ingot, diamond_block) are also handled automatically.\n");
                writer.write("# Likewise for items from mods using ores-mods (link)\n");
                writer.write("# This list is for enabling extra items for your modpack, like compressed blocks, diamond nugget etc.\n");
                writer.write("#\n");
                writer.write("# Example:\n");
                writer.write("# generate = [\"compressed_coal_block\", \"double_compressed_coal_block\", \"diamond_nugget\", \"raw_diamond\"]\n");
                writer.write("# List for variants here (link)\n");
                writer.write("\n");
                writer.write("generate = []\n");
            }
            System.out.println("Default config file created at: " + configFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("ERROR: Could not create default config file.");
            e.printStackTrace();
        }
    }


    /**
     * Checks if a resource ID is in the generation list.
     * @param id The ID to check (e.g., "iron_ingot").
     * @return true if the ID should be generated, false otherwise.
     */
    public static boolean shouldGenerate(String id) {
        if (!isLoaded) {
            load();
        }
        return GENERATION_LIST.contains(id);
    }
}
