package com.ores.registries;

import com.ores.OresMod;
import com.ores.core.Variants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OresMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEMS_TAB = CREATIVE_MODE_TABS.register("items_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.items_tab"))
            .icon(() -> Registry.ITEMS_TO_REGISTER.stream()
                    .findFirst()
                    .map(entry -> new ItemStack(ModItems.getItem(entry.ID()).get()))
                    .orElse(new ItemStack(Items.IRON_INGOT)))
            .displayItems((parameters, output) -> {
                Registry.ITEMS_TO_REGISTER.forEach(entry -> output.accept(ModItems.getItem(entry.ID()).get()));
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS_TAB = CREATIVE_MODE_TABS.register("blocks_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.blocks_tab"))
            .icon(() -> Registry.BLOCKS_TO_REGISTER.stream()
                    .filter(entry -> entry.category() == Variants.Category.BLOCK || entry.category() == Variants.Category.FALLING_BLOCK)
                    .findFirst()
                    .map(entry -> new ItemStack(ModItems.getItem(entry.ID()).get()))
                    .orElse(new ItemStack(Items.IRON_BLOCK)))
            .displayItems((parameters, output) -> {
                Registry.BLOCKS_TO_REGISTER.forEach(entry -> {
                    if (entry.category() == Variants.Category.BLOCK || entry.category() == Variants.Category.FALLING_BLOCK) {
                        output.accept(ModItems.getItem(entry.ID()).get());
                    }
                });
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ORES_TAB = CREATIVE_MODE_TABS.register("ores_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.ores_tab"))
            .icon(() -> Registry.BLOCKS_TO_REGISTER.stream()
                    .filter(entry -> entry.category() == Variants.Category.ORE || entry.category() == Variants.Category.FALLING_ORE)
                    .findFirst()
                    .map(entry -> new ItemStack(ModItems.getItem(entry.ID()).get()))
                    .orElse(new ItemStack(Items.IRON_ORE)))
            .displayItems((parameters, output) -> {
                Registry.BLOCKS_TO_REGISTER.forEach(entry -> {
                    if (entry.category() == Variants.Category.ORE || entry.category() == Variants.Category.FALLING_ORE) {
                        output.accept(ModItems.getItem(entry.ID()).get());
                    }
                });
            }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
