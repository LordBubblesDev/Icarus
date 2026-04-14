package dev.cammiescorner.icarus.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.icarus.client.IcarusModels;
import dev.cammiescorner.icarus.client.models.DiscordsWingsModel;
import dev.cammiescorner.icarus.client.models.FeatheredWingsModel;
import dev.cammiescorner.icarus.client.models.FlandresWingsModel;
import dev.cammiescorner.icarus.client.models.LeatherWingsModel;
import dev.cammiescorner.icarus.client.models.LightWingsModel;
import dev.cammiescorner.icarus.client.models.WingEntityModel;
import dev.cammiescorner.icarus.client.models.ZanzasWingsModel;
import dev.cammiescorner.icarus.init.IcarusItems;
import dev.cammiescorner.icarus.item.WingItem;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Method;
import java.util.Map;

public class WingsLayer<S extends HumanoidRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
    private final FeatheredWingsModel featheredWings;
    private final LeatherWingsModel leatherWings;
    private final LightWingsModel lightWings;
    private final FlandresWingsModel flandresWings;
    private final DiscordsWingsModel discordsWings;
    private final ZanzasWingsModel zanzasWings;

    private static final Map<Item, Identifier[]> TEXTURE_LOOKUP = new Reference2ObjectOpenHashMap<>();

    public WingsLayer(RenderLayerParent<S, M> context, EntityModelSet loader) {
        super(context);
        this.featheredWings = new FeatheredWingsModel(loader.bakeLayer(IcarusModels.FEATHERED));
        this.leatherWings = new LeatherWingsModel(loader.bakeLayer(IcarusModels.LEATHER));
        this.lightWings = new LightWingsModel(loader.bakeLayer(IcarusModels.LIGHT));
        this.flandresWings = new FlandresWingsModel(loader.bakeLayer(IcarusModels.FLANDRE));
        this.discordsWings = new DiscordsWingsModel(loader.bakeLayer(IcarusModels.DISCORD));
        this.zanzasWings = new ZanzasWingsModel(loader.bakeLayer(IcarusModels.ZANZA));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        var stack = getRenderedWingStack(state);
        if (!(stack.getItem() instanceof WingItem wingItem)) {
            return;
        }

        var wingModel = switch (wingItem.getWingType()) {
            case FEATHERED, MECHANICAL_FEATHERED -> featheredWings;
            case DRAGON, MECHANICAL_LEATHER -> leatherWings;
            case LIGHT -> lightWings;
            case UNIQUE -> {
                if (stack.is(IcarusItems.FLANDRES_WINGS.get())) {
                    yield flandresWings;
                }
                if (stack.is(IcarusItems.DISCORDS_WINGS.get())) {
                    yield discordsWings;
                }
                if (stack.is(IcarusItems.ZANZAS_WINGS.get())) {
                    yield zanzasWings;
                }
                yield null;
            }
        };
        if (wingModel == null) {
            return;
        }

        var textures = TEXTURE_LOOKUP.computeIfAbsent(wingItem, item -> {
            var baseId = BuiltInRegistries.ITEM.getKey(item).withPrefix("textures/entity/icarus/wings/");
            return new Identifier[] {
                baseId.withSuffix(".png"),
                baseId.withSuffix("_2.png")
            };
        });
        boolean hasSecondLayer = switch (wingItem.getWingType()) {
            case DRAGON, MECHANICAL_FEATHERED, MECHANICAL_LEATHER -> true;
            case FEATHERED, LIGHT, UNIQUE -> false;
        };

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, 0.125D);
        submitNodeCollector.submitModel(
            wingModel,
            state,
            poseStack,
            RenderTypes.entityTranslucent(textures[0]),
            lightCoords,
            LivingEntityRenderer.getOverlayCoords(state, 0.0F),
            state.outlineColor,
            null
        );
        if (hasSecondLayer) {
            submitNodeCollector.submitModel(
                wingModel,
                state,
                poseStack,
                RenderTypes.entityTranslucent(textures[1]),
                lightCoords,
                LivingEntityRenderer.getOverlayCoords(state, 0.0F),
                state.outlineColor,
                null
            );
        }
        poseStack.popPose();
    }

    private ItemStack getRenderedWingStack(S state) {
        if (state.chestEquipment.getItem() instanceof WingItem) {
            return state.chestEquipment;
        }
        try {
            // Trinkets-continued injects this accessor on humanoid render state.
            Method m = state.getClass().getMethod("trinkets$getState");
            Object trinketState = m.invoke(state);
            if (!(trinketState instanceof Iterable<?> iterable)) {
                return ItemStack.EMPTY;
            }

            for (Object tuple : iterable) {
                for (String accessor : new String[] {"getA", "getB", "getLeft", "getRight", "getFirst", "getSecond"}) {
                    try {
                        Method getter = tuple.getClass().getMethod(accessor);
                        Object value = getter.invoke(tuple);
                        if (value instanceof ItemStack stack && stack.getItem() instanceof WingItem) {
                            return stack;
                        }
                    } catch (ReflectiveOperationException ignored) {
                        // try next accessor
                    }
                }
            }
        } catch (Throwable ignored) {
            // Trinkets state not available for this renderer/state.
        }

        return ItemStack.EMPTY;
    }
}
