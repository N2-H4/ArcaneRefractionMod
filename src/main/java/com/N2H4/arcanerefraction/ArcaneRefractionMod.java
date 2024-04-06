package com.N2H4.arcanerefraction;

import com.N2H4.arcanerefraction.Block.DispersiveAmethysyBlock;
import com.N2H4.arcanerefraction.Block.AmethystFocusBlock;
import com.N2H4.arcanerefraction.BlockEntity.AmethystFocusEntity;
import com.N2H4.arcanerefraction.Menu.AmethystFocusMenu;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
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

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<MenuType<?>> MENUS=DeferredRegister.create(BuiltInRegistries.MENU, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredBlock<Block> AMETHYST_FOCUS_BLOCK = BLOCKS.register("amethyst_focus", AmethystFocusBlock::new);
    public static final DeferredBlock<Block> DISPERSIVE_AMETHYST_BLOCK = BLOCKS.register("dispersive_amethyst", DispersiveAmethysyBlock::new);
    public static final DeferredItem<BlockItem> AMETHYST_FOCUS_ITEM = ITEMS.registerSimpleBlockItem("amethyst_focus", AMETHYST_FOCUS_BLOCK);
    public static final DeferredItem<BlockItem> DISPERSIVE_AMETHYST_ITEM = ITEMS.registerSimpleBlockItem("dispersive_amethyst", DISPERSIVE_AMETHYST_BLOCK);
    public static final DeferredHolder<MenuType<?>,MenuType<AmethystFocusMenu>> AMETHYST_FOCUS_MENU = MENUS.register("amethyst_focus_menu",() -> IMenuTypeExtension.create(AmethystFocusMenu::new));
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<AmethystFocusEntity>> AMETHYST_FOCUS_ENTITY = BLOCK_ENTITY_REGISTER.register("amethyst_focus_entity",() -> BlockEntityType.Builder.of(AmethystFocusEntity::new, AMETHYST_FOCUS_BLOCK.get()).build(null));
    public static final TagKey<Item> LENS_COATING_TAG = ItemTags.create(new ResourceLocation("arcanerefraction", "lens_coating"));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.arcanerefraction"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .displayItems((parameters, output) -> {
                output.accept(AMETHYST_FOCUS_ITEM.get());
                output.accept(DISPERSIVE_AMETHYST_ITEM.get());
            }).build());


    public ArcaneRefractionMod(IEventBus modEventBus)
    {
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        MENUS.register(modEventBus);
        BLOCK_ENTITY_REGISTER.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        //modEventBus.addListener(this::addCreative);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

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
