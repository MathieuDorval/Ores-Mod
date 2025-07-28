package com.ores.datagen;

import com.ores.OresMod;
import com.ores.core.Materials;
import com.ores.core.Variants;
import com.ores.registries.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, OresMod.MODID);

    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        for (Materials material : Materials.values()) {
            for (Variants variant : material.getVariants()) {
                String itemId = variant.getFormattedId(material.getId());
                Supplier<? extends Item> itemSupplier = ModItems.getItem(itemId);

                if (itemSupplier == null) {
                    continue;
                }

                boolean isBeaconPayment = false;
                var category = variant.getCategory();

                // Gère les items simples
                if (category == Variants.Category.ITEM) {
                    Variants.ItemProps varProps = Objects.requireNonNull(variant.getItemProps());
                    if (material.getItemProps().beacon() && varProps.beacon()) {
                        isBeaconPayment = true;
                    }
                }
                // Gère les BlockItems
                else if (category == Variants.Category.BLOCK || category == Variants.Category.FALLING_BLOCK) {
                    Variants.BlockProps varProps = Objects.requireNonNull(variant.getBlockProps());
                    if (material.getItemProps().beacon() && varProps.beacon()) {
                        isBeaconPayment = true;
                    }
                }

                // Applique le tag si la condition est remplie
                if (isBeaconPayment) {
                    this.tag(ItemTags.BEACON_PAYMENT_ITEMS).add(itemSupplier.get());
                }
            }
        }
    }
}
