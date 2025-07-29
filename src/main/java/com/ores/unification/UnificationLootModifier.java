package com.ores.unification;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * A global loot modifier that iterates through generated loot and replaces
 * items based on the UnificationRegistry's replacement map.
 */
public class UnificationLootModifier extends LootModifier {
    public static final Supplier<MapCodec<UnificationLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, UnificationLootModifier::new))
    );

    public UnificationLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Get the replacement map from our unification registry
        var replacementMap = UnificationRegistry.getReplacementMap();
        if (replacementMap.isEmpty()) {
            return generatedLoot; // No replacements needed
        }

        // Iterate through each item stack in the loot
        for (int i = 0; i < generatedLoot.size(); i++) {
            ItemStack stack = generatedLoot.get(i);
            Item originalItem = stack.getItem();

            // Check if this item is in our replacement map
            if (replacementMap.containsKey(originalItem)) {
                Item replacementItem = replacementMap.get(originalItem);
                // Create a new stack with our unified item, preserving the count and components (NBT)
                ItemStack newStack = new ItemStack(replacementItem, stack.getCount());
                newStack.applyComponents(stack.getComponents());
                generatedLoot.set(i, newStack);
            }
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
