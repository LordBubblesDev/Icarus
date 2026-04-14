package dev.cammiescorner.icarus.init;

import dev.cammiescorner.icarus.Icarus;
import dev.cammiescorner.icarus.item.WingItem;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class IcarusItems {

    public static final RegistryHandler<CreativeModeTab> CREATIVE_TABS = RegistryHandler.create(Registries.CREATIVE_MODE_TAB, Icarus.MODID);
    public static final RegistryHandler<Item> ITEMS = RegistryHandler.create(Registries.ITEM, Icarus.MODID);

    public static final RegistrySupplier<WingItem> WHITE_FEATHERED_WINGS = wing("white_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> ORANGE_FEATHERED_WINGS = wing("orange_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> MAGENTA_FEATHERED_WINGS = wing("magenta_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> LIGHT_BLUE_FEATHERED_WINGS = wing("light_blue_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> YELLOW_FEATHERED_WINGS = wing("yellow_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> LIME_FEATHERED_WINGS = wing("lime_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> PINK_FEATHERED_WINGS = wing("pink_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> GRAY_FEATHERED_WINGS = wing("gray_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> LIGHT_GRAY_FEATHERED_WINGS = wing("light_gray_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> CYAN_FEATHERED_WINGS = wing("cyan_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> PURPLE_FEATHERED_WINGS = wing("purple_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> BLUE_FEATHERED_WINGS = wing("blue_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> BROWN_FEATHERED_WINGS = wing("brown_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> GREEN_FEATHERED_WINGS = wing("green_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> RED_FEATHERED_WINGS = wing("red_feathered_wings", WingItem.WingType.FEATHERED);
    public static final RegistrySupplier<WingItem> BLACK_FEATHERED_WINGS = wing("black_feathered_wings", WingItem.WingType.FEATHERED);

    public static final RegistrySupplier<WingItem> WHITE_DRAGON_WINGS = wing("white_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> ORANGE_DRAGON_WINGS = wing("orange_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> MAGENTA_DRAGON_WINGS = wing("magenta_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> LIGHT_BLUE_DRAGON_WINGS = wing("light_blue_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> YELLOW_DRAGON_WINGS = wing("yellow_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> LIME_DRAGON_WINGS = wing("lime_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> PINK_DRAGON_WINGS = wing("pink_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> GRAY_DRAGON_WINGS = wing("gray_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> LIGHT_GRAY_DRAGON_WINGS = wing("light_gray_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> CYAN_DRAGON_WINGS = wing("cyan_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> PURPLE_DRAGON_WINGS = wing("purple_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> BLUE_DRAGON_WINGS = wing("blue_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> BROWN_DRAGON_WINGS = wing("brown_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> GREEN_DRAGON_WINGS = wing("green_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> RED_DRAGON_WINGS = wing("red_dragon_wings", WingItem.WingType.DRAGON);
    public static final RegistrySupplier<WingItem> BLACK_DRAGON_WINGS = wing("black_dragon_wings", WingItem.WingType.DRAGON);

    public static final RegistrySupplier<WingItem> WHITE_MECHANICAL_FEATHERED_WINGS = wing("white_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> ORANGE_MECHANICAL_FEATHERED_WINGS = wing("orange_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> MAGENTA_MECHANICAL_FEATHERED_WINGS = wing("magenta_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> LIGHT_BLUE_MECHANICAL_FEATHERED_WINGS = wing("light_blue_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> YELLOW_MECHANICAL_FEATHERED_WINGS = wing("yellow_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> LIME_MECHANICAL_FEATHERED_WINGS = wing("lime_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> PINK_MECHANICAL_FEATHERED_WINGS = wing("pink_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> GRAY_MECHANICAL_FEATHERED_WINGS = wing("gray_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> LIGHT_GRAY_MECHANICAL_FEATHERED_WINGS = wing("light_gray_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> CYAN_MECHANICAL_FEATHERED_WINGS = wing("cyan_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> PURPLE_MECHANICAL_FEATHERED_WINGS = wing("purple_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> BLUE_MECHANICAL_FEATHERED_WINGS = wing("blue_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> BROWN_MECHANICAL_FEATHERED_WINGS = wing("brown_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> GREEN_MECHANICAL_FEATHERED_WINGS = wing("green_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> RED_MECHANICAL_FEATHERED_WINGS = wing("red_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);
    public static final RegistrySupplier<WingItem> BLACK_MECHANICAL_FEATHERED_WINGS = wing("black_mechanical_feathered_wings", WingItem.WingType.MECHANICAL_FEATHERED);

    public static final RegistrySupplier<WingItem> WHITE_MECHANICAL_LEATHER_WINGS = wing("white_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> ORANGE_MECHANICAL_LEATHER_WINGS = wing("orange_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> MAGENTA_MECHANICAL_LEATHER_WINGS = wing("magenta_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> LIGHT_BLUE_MECHANICAL_LEATHER_WINGS = wing("light_blue_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> YELLOW_MECHANICAL_LEATHER_WINGS = wing("yellow_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> LIME_MECHANICAL_LEATHER_WINGS = wing("lime_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> PINK_MECHANICAL_LEATHER_WINGS = wing("pink_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> GRAY_MECHANICAL_LEATHER_WINGS = wing("gray_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> LIGHT_GRAY_MECHANICAL_LEATHER_WINGS = wing("light_gray_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> CYAN_MECHANICAL_LEATHER_WINGS = wing("cyan_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> PURPLE_MECHANICAL_LEATHER_WINGS = wing("purple_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> BLUE_MECHANICAL_LEATHER_WINGS = wing("blue_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> BROWN_MECHANICAL_LEATHER_WINGS = wing("brown_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> GREEN_MECHANICAL_LEATHER_WINGS = wing("green_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> RED_MECHANICAL_LEATHER_WINGS = wing("red_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);
    public static final RegistrySupplier<WingItem> BLACK_MECHANICAL_LEATHER_WINGS = wing("black_mechanical_leather_wings", WingItem.WingType.MECHANICAL_LEATHER);

    public static final RegistrySupplier<WingItem> WHITE_LIGHT_WINGS = wing("white_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> ORANGE_LIGHT_WINGS = wing("orange_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> MAGENTA_LIGHT_WINGS = wing("magenta_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> LIGHT_BLUE_LIGHT_WINGS = wing("light_blue_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> YELLOW_LIGHT_WINGS = wing("yellow_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> LIME_LIGHT_WINGS = wing("lime_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> PINK_LIGHT_WINGS = wing("pink_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> GRAY_LIGHT_WINGS = wing("gray_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> LIGHT_GRAY_LIGHT_WINGS = wing("light_gray_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> CYAN_LIGHT_WINGS = wing("cyan_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> PURPLE_LIGHT_WINGS = wing("purple_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> BLUE_LIGHT_WINGS = wing("blue_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> BROWN_LIGHT_WINGS = wing("brown_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> GREEN_LIGHT_WINGS = wing("green_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> RED_LIGHT_WINGS = wing("red_light_wings", WingItem.WingType.LIGHT);
    public static final RegistrySupplier<WingItem> BLACK_LIGHT_WINGS = wing("black_light_wings", WingItem.WingType.LIGHT);

    public static final RegistrySupplier<WingItem> FLANDRES_WINGS = wing("flandres_wings", WingItem.WingType.UNIQUE);
    public static final RegistrySupplier<WingItem> DISCORDS_WINGS = wing("discords_wings", WingItem.WingType.UNIQUE);
    public static final RegistrySupplier<WingItem> ZANZAS_WINGS = wing("zanzas_wings", WingItem.WingType.UNIQUE);

    private static RegistrySupplier<WingItem> wing(String name, WingItem.WingType wingType) {
        return ITEMS.register(name, () -> new WingItem(Icarus.id(name), wingType));
    }

}
