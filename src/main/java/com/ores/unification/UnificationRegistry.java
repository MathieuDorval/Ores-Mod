package com.ores.unification;

import com.ores.OresMod;
import com.ores.registries.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Scans all registered items to build a map for unifying items from other mods.
 * This class now contains the logic that will be manually registered to the event bus
 * in the main mod class.
 */
public class UnificationRegistry {

    // A map of items to be replaced (key) with our mod's item (value).
    private static final Map<Item, Item> REPLACEMENT_MAP = new HashMap<>();
    private static boolean isInitialized = false;

    /**
     * This method is now called directly by an event listener in the main mod class.
     * It fires after all mods have registered their items.
     * @param event The registration event.
     */
    public static void onRegisterItems(final RegisterEvent event) {
        // We only want to run this logic for the ITEM registry
        if (event.getRegistryKey().equals(BuiltInRegistries.ITEM.key()) && !isInitialized) {
            System.out.println("Ores Unification: Scanning for items to unify...");

            // Iterate through all items registered in the game
            for (Item item : BuiltInRegistries.ITEM) {
                ResourceLocation loc = BuiltInRegistries.ITEM.getKey(item);
                String namespace = loc.getNamespace();
                String path = loc.getPath();

                // Skip items from Minecraft or our own mod
                if (namespace.equals("minecraft") || namespace.equals(OresMod.MODID)) {
                    continue;
                }

                // Check if our mod has an item with the same path
                Supplier<? extends Item> ourItemSupplier = ModItems.getItem(path);
                if (ourItemSupplier != null) {
                    Item ourItem = ourItemSupplier.get();
                    // If found, add it to the replacement map
                    REPLACEMENT_MAP.put(item, ourItem);
                    System.out.println("  -> Mapping " + loc + " to " + BuiltInRegistries.ITEM.getKey(ourItem));
                }
            }
            isInitialized = true;
            System.out.println("Ores Unification: Scan complete. Found " + REPLACEMENT_MAP.size() + " items to unify.");
        }
    }

    public static Map<Item, Item> getReplacementMap() {
        return REPLACEMENT_MAP;
    }
}
