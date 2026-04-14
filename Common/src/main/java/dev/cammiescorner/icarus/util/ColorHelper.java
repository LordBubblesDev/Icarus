package dev.cammiescorner.icarus.util;


import net.minecraft.util.ARGB;
import net.minecraft.world.item.DyeColor;

public class ColorHelper {
    public static int asARGB(DyeColor color) {
        var rgb = color.getTextureDiffuseColor();
        return ARGB.color(255, rgb);
    }
}
