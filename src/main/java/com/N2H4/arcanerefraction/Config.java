package com.N2H4.arcanerefraction;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;


@Mod.EventBusSubscriber(modid = ArcaneRefractionMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<List<? extends String>> BLOCK_STRINGS_COMMON = BUILDER
            .comment("Common blocks spawnable by ores spawning.")
            .defineListAllowEmpty("spawnable_ores_common", List.of("minecraft:coal_ore","minecraft:iron_ore","minecraft:copper_ore"), Config::validateBlockName);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> BLOCK_STRINGS_UNCOMMON = BUILDER
            .comment("Uncommon blocks spawnable by ores spawning.")
            .defineListAllowEmpty("spawnable_ores_uncommon", List.of("minecraft:gold_ore","minecraft:lapis_ore","minecraft:redstone_ore"), Config::validateBlockName);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> BLOCK_STRINGS_RARE = BUILDER
            .comment("Rare blocks spawnable by ores spawning.")
            .defineListAllowEmpty("spawnable_ores_rare", List.of("minecraft:diamond_ore","minecraft:emerald_ore"), Config::validateBlockName);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> BLOCK_STRINGS_VERY_RARE = BUILDER
            .comment("Very rare blocks spawnable by ores spawning.")
            .defineListAllowEmpty("spawnable_ores_very_rare", List.of("minecraft:ancient_debris"), Config::validateBlockName);

    private static final ModConfigSpec.IntValue LENS_DEPTH = BUILDER.comment("Vertical range of lens").defineInRange("lens_depth", 15, 1, 50);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static List<Block> common_blocks;
    public static List<Block> uncommon_blocks;
    public static List<Block> rare_blocks;
    public static List<Block> very_rare_blocks;
    public static int lens_depth;

    private static boolean validateBlockName(final Object obj)
    {
        return obj instanceof String blockName && BuiltInRegistries.BLOCK.containsKey(new ResourceLocation(blockName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        Set<Block> common_set = BLOCK_STRINGS_COMMON.get().stream()
                .map(blockName -> BuiltInRegistries.BLOCK.get(new ResourceLocation(blockName)))
                .collect(Collectors.toSet());
        common_blocks = new ArrayList<>(common_set);

        Set<Block> uncommon_set = BLOCK_STRINGS_UNCOMMON.get().stream()
                .map(blockName -> BuiltInRegistries.BLOCK.get(new ResourceLocation(blockName)))
                .collect(Collectors.toSet());
        uncommon_blocks = new ArrayList<>(uncommon_set);

        Set<Block> rare_set = BLOCK_STRINGS_RARE.get().stream()
                .map(blockName -> BuiltInRegistries.BLOCK.get(new ResourceLocation(blockName)))
                .collect(Collectors.toSet());
        rare_blocks = new ArrayList<>(rare_set);

        Set<Block> very_rare_set = BLOCK_STRINGS_VERY_RARE.get().stream()
                .map(blockName -> BuiltInRegistries.BLOCK.get(new ResourceLocation(blockName)))
                .collect(Collectors.toSet());
        very_rare_blocks = new ArrayList<>(very_rare_set);

        lens_depth=LENS_DEPTH.get();
    }
}
