package com.N2H4.arcanerefraction;

import com.N2H4.arcanerefraction.Block.DispersiveAmethysyBlock;
import com.N2H4.arcanerefraction.Block.DispersiveRegolithBlock;
import com.N2H4.arcanerefraction.Block.DispersiveTephraBlock;
import com.N2H4.arcanerefraction.Block.RegolithFilterBlock;
import com.N2H4.arcanerefraction.Block.RegolithFocusBlock;
import com.N2H4.arcanerefraction.Block.TephraFilterBlock;
import com.N2H4.arcanerefraction.Block.TephraFocusBlock;
import com.N2H4.arcanerefraction.Block.AmethystFilterBlock;
import com.N2H4.arcanerefraction.Block.AmethystFocusBlock;
import com.N2H4.arcanerefraction.BlockEntity.AmethystFilterEntity;
import com.N2H4.arcanerefraction.BlockEntity.AmethystFocusEntity;
import com.N2H4.arcanerefraction.BlockEntity.RegolithFilterEntity;
import com.N2H4.arcanerefraction.BlockEntity.RegolithFocusEntity;
import com.N2H4.arcanerefraction.BlockEntity.TephraFilterEntity;
import com.N2H4.arcanerefraction.BlockEntity.TephraFocusEntity;
import com.N2H4.arcanerefraction.Menu.AmethystFilterMenu;
import com.N2H4.arcanerefraction.Menu.AmethystFocusMenu;
import com.N2H4.arcanerefraction.Menu.RegolithFilterMenu;
import com.N2H4.arcanerefraction.Menu.RegolithFocusMenu;
import com.N2H4.arcanerefraction.Menu.TephraFilterMenu;
import com.N2H4.arcanerefraction.Menu.TephraFocusMenu;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(ArcaneRefractionMod.MODID)
public class ArcaneRefractionMod
{
    public static final String MODID = "arcanerefraction";
    private static final Logger LOGGER = LogUtils.getLogger();

    //REGISTERS
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<MenuType<?>> MENUS=DeferredRegister.create(BuiltInRegistries.MENU, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MODID);
    //BLOCKS
    //amethyst
    public static final DeferredBlock<Block> AMETHYST_FOCUS_BLOCK = BLOCKS.register("amethyst_focus", AmethystFocusBlock::new);
    public static final DeferredBlock<Block> DISPERSIVE_AMETHYST_BLOCK = BLOCKS.register("dispersive_amethyst", DispersiveAmethysyBlock::new);
    public static final DeferredBlock<Block> AMETHYST_FILTER_BLOCK = BLOCKS.register("amethyst_filter", AmethystFilterBlock::new);
    //regolith
    public static final DeferredBlock<Block> REGOLITH_FOCUS_BLOCK = BLOCKS.register("regolith_focus", RegolithFocusBlock::new);
    public static final DeferredBlock<Block> DISPERSIVE_REGOLITH_BLOCK = BLOCKS.register("dispersive_regolith", DispersiveRegolithBlock::new);
    public static final DeferredBlock<Block> REGOLITH_FILTER_BLOCK = BLOCKS.register("regolith_filter", RegolithFilterBlock::new);
    //tephra
    public static final DeferredBlock<Block> TEPHRA_FOCUS_BLOCK = BLOCKS.register("tephra_focus", TephraFocusBlock::new);
    public static final DeferredBlock<Block> DISPERSIVE_TEPHRA_BLOCK = BLOCKS.register("dispersive_tephra", DispersiveTephraBlock::new);
    public static final DeferredBlock<Block> TEPHRA_FILTER_BLOCK = BLOCKS.register("tephra_filter", TephraFilterBlock::new);
    //ITEMS
    //amethyst
    public static final DeferredItem<BlockItem> AMETHYST_FOCUS_ITEM = ITEMS.registerSimpleBlockItem("amethyst_focus", AMETHYST_FOCUS_BLOCK);
    public static final DeferredItem<BlockItem> DISPERSIVE_AMETHYST_ITEM = ITEMS.registerSimpleBlockItem("dispersive_amethyst", DISPERSIVE_AMETHYST_BLOCK);
    public static final DeferredItem<BlockItem> AMETHYST_FILTER_ITEM = ITEMS.registerSimpleBlockItem("amethyst_filter", AMETHYST_FILTER_BLOCK);
    //regolith
    public static final DeferredItem<BlockItem> REGOLITH_FOCUS_ITEM = ITEMS.registerSimpleBlockItem("regolith_focus", REGOLITH_FOCUS_BLOCK);
    public static final DeferredItem<BlockItem> DISPERSIVE_REGOLITH_ITEM = ITEMS.registerSimpleBlockItem("dispersive_regolith", DISPERSIVE_REGOLITH_BLOCK);
    public static final DeferredItem<BlockItem> REGOLITH_FILTER_ITEM = ITEMS.registerSimpleBlockItem("regolith_filter", REGOLITH_FILTER_BLOCK);
    //tephra
    public static final DeferredItem<BlockItem> TEPHRA_FOCUS_ITEM = ITEMS.registerSimpleBlockItem("tephra_focus", TEPHRA_FOCUS_BLOCK);
    public static final DeferredItem<BlockItem> DISPERSIVE_TEPHRA_ITEM = ITEMS.registerSimpleBlockItem("dispersive_tephra", DISPERSIVE_TEPHRA_BLOCK);
    public static final DeferredItem<BlockItem> TEPHRA_FILTER_ITEM = ITEMS.registerSimpleBlockItem("tephra_filter", TEPHRA_FILTER_BLOCK);
    //coatings
    public static final DeferredItem<Item> HONEYCOMB_COATING = ITEMS.registerSimpleItem("honeycomb_coating");
    public static final DeferredItem<Item> BLACKSTONE_COATING = ITEMS.registerSimpleItem("blackstone_coating");
    public static final DeferredItem<Item> CRYING_OBSIDIAN_COATING = ITEMS.registerSimpleItem("crying_obsidian_coating");
    public static final DeferredItem<Item> FROGLIGHT_COATING = ITEMS.registerSimpleItem("froglight_coating");
    public static final DeferredItem<Item> SCULK_COATING = ITEMS.registerSimpleItem("sculk_coating");
    public static final DeferredItem<Item> FIRE_CORAL_COATING = ITEMS.registerSimpleItem("fire_coral_coating");
    public static final DeferredItem<Item> COPPER_COATING = ITEMS.registerSimpleItem("copper_coating");
    public static final DeferredItem<Item> PURPUR_COATING = ITEMS.registerSimpleItem("purpur_coating");
    //MENUS
    public static final DeferredHolder<MenuType<?>,MenuType<AmethystFocusMenu>> AMETHYST_FOCUS_MENU = MENUS.register("amethyst_focus_menu",() -> IMenuTypeExtension.create(AmethystFocusMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<AmethystFilterMenu>> AMETHYST_FILTER_MENU = MENUS.register("amethyst_filter_menu",() -> IMenuTypeExtension.create(AmethystFilterMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<RegolithFocusMenu>> REGOLITH_FOCUS_MENU = MENUS.register("regolith_focus_menu",() -> IMenuTypeExtension.create(RegolithFocusMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<RegolithFilterMenu>> REGOLITH_FILTER_MENU = MENUS.register("regolith_filter_menu",() -> IMenuTypeExtension.create(RegolithFilterMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<TephraFocusMenu>> TEPHRA_FOCUS_MENU = MENUS.register("tephra_focus_menu",() -> IMenuTypeExtension.create(TephraFocusMenu::new));
    public static final DeferredHolder<MenuType<?>,MenuType<TephraFilterMenu>> TEPHRA_FILTER_MENU = MENUS.register("tephra_filter_menu",() -> IMenuTypeExtension.create(TephraFilterMenu::new));
    //BLOCK ENTITIES
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<AmethystFocusEntity>> AMETHYST_FOCUS_ENTITY = BLOCK_ENTITY_REGISTER.register("amethyst_focus_entity",() -> BlockEntityType.Builder.of(AmethystFocusEntity::new, AMETHYST_FOCUS_BLOCK.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<AmethystFilterEntity>> AMETHYST_FILTER_ENTITY = BLOCK_ENTITY_REGISTER.register("amethyst_filter_entity",() -> BlockEntityType.Builder.of(AmethystFilterEntity::new, AMETHYST_FILTER_BLOCK.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<RegolithFocusEntity>> REGOLITH_FOCUS_ENTITY = BLOCK_ENTITY_REGISTER.register("regolith_focus_entity",() -> BlockEntityType.Builder.of(RegolithFocusEntity::new, REGOLITH_FOCUS_BLOCK.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<RegolithFilterEntity>> REGOLITH_FILTER_ENTITY = BLOCK_ENTITY_REGISTER.register("regolith_filter_entity",() -> BlockEntityType.Builder.of(RegolithFilterEntity::new, REGOLITH_FILTER_BLOCK.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TephraFocusEntity>> TEPHRA_FOCUS_ENTITY = BLOCK_ENTITY_REGISTER.register("tephra_focus_entity",() -> BlockEntityType.Builder.of(TephraFocusEntity::new, TEPHRA_FOCUS_BLOCK.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TephraFilterEntity>> TEPHRA_FILTER_ENTITY = BLOCK_ENTITY_REGISTER.register("tephra_filter_entity",() -> BlockEntityType.Builder.of(TephraFilterEntity::new, TEPHRA_FILTER_BLOCK.get()).build(null));
    //OTHER
    public static final TagKey<Item> LENS_COATING_TAG = ItemTags.create(new ResourceLocation("arcanerefraction", "lens_coating"));
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES=DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, MODID);
    public static final DeferredHolder<ParticleType<?>,ParticleType<SimpleParticleType>> RAY_PARTICLE=PARTICLE_TYPES.register("ray_particle", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>,ParticleType<SimpleParticleType>> REGOLITH_PARTICLE=PARTICLE_TYPES.register("regolith_particle", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>,ParticleType<SimpleParticleType>> TEPHRA_PARTICLE=PARTICLE_TYPES.register("tephra_particle", () -> new SimpleParticleType(true));
    public static final DeferredHolder<SoundEvent,SoundEvent> LENS_SOUND = SOUND_EVENTS.register("lens_sound", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "lens_sound")));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.arcanerefraction"))
            .icon(() -> new ItemStack(DISPERSIVE_AMETHYST_ITEM.get()))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .displayItems((parameters, output) -> {
                output.accept(AMETHYST_FOCUS_ITEM.get());
                output.accept(DISPERSIVE_AMETHYST_ITEM.get());
                output.accept(AMETHYST_FILTER_ITEM.get());
                output.accept(REGOLITH_FOCUS_ITEM.get());
                output.accept(DISPERSIVE_REGOLITH_ITEM.get());
                output.accept(REGOLITH_FILTER_ITEM.get());
                output.accept(TEPHRA_FOCUS_ITEM.get());
                output.accept(DISPERSIVE_TEPHRA_ITEM.get());
                output.accept(TEPHRA_FILTER_ITEM.get());
                output.accept(HONEYCOMB_COATING.get());
                output.accept(BLACKSTONE_COATING.get());
                output.accept(CRYING_OBSIDIAN_COATING.get());
                output.accept(FROGLIGHT_COATING.get());
                output.accept(SCULK_COATING.get());
                output.accept(FIRE_CORAL_COATING.get());
                output.accept(COPPER_COATING.get());
                output.accept(PURPUR_COATING.get());
            }).build());


    public ArcaneRefractionMod(IEventBus modEventBus)
    {
        //modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        MENUS.register(modEventBus);
        BLOCK_ENTITY_REGISTER.register(modEventBus);
        PARTICLE_TYPES.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
        ItemTags.create(new ResourceLocation("arcanerefraction", "lens_coating"));

        NeoForge.EVENT_BUS.register(this);

        //modEventBus.addListener(this::addCreative);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    /*private void commonSetup(final FMLCommonSetupEvent event)
    {
        Config.common_blocks.forEach((block) -> LOGGER.info("Common >> {}", block.toString()));
        Config.uncommon_blocks.forEach((block) -> LOGGER.info("uncommon >> {}", block.toString()));
        Config.rare_blocks.forEach((block) -> LOGGER.info("rare >> {}", block.toString()));
        Config.very_rare_blocks.forEach((block) -> LOGGER.info("veryrare >> {}", block.toString()));
    }*/

    /*private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(EXAMPLE_BLOCK_ITEM);
    }*/

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
