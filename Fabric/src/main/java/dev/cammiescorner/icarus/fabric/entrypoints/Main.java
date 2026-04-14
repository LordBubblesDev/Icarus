package dev.cammiescorner.icarus.fabric.entrypoints;

import dev.cammiescorner.icarus.api.IcarusPlayerValues;
import dev.cammiescorner.icarus.fabric.IcarusFabricGameRules;
import dev.cammiescorner.icarus.init.IcarusItems;
import dev.cammiescorner.icarus.item.WingItem;
import dev.cammiescorner.icarus.util.IcarusHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        IcarusFabricGameRules.init();
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> IcarusHelper.onServerPlayerJoin(handler.getPlayer()));
        IcarusHelper.configValuesProvider = entity -> {
            if (entity.level().isClientSide()) {
                return IcarusHelper.fallbackValues;
            }
            var base = IcarusHelper.fallbackValues;
            return new IcarusPlayerValues() {
                @Override
                public float wingsSpeed() {
                    if (entity.level() instanceof ServerLevel serverLevel) {
                        return IcarusFabricGameRules.wingsSpeed(serverLevel);
                    }
                    return base.wingsSpeed();
                }

                @Override
                public float maxSlowedMultiplier() {
                    return base.maxSlowedMultiplier();
                }

                @Override
                public boolean armorSlows() {
                    if (entity.level() instanceof ServerLevel serverLevel) {
                        return IcarusFabricGameRules.armorSlows(serverLevel);
                    }
                    return base.armorSlows();
                }

                @Override
                public boolean canLoopDeLoop() {
                    return base.canLoopDeLoop();
                }

                @Override
                public boolean canSlowFall() {
                    return base.canSlowFall();
                }

                @Override
                public float exhaustionAmount() {
                    return base.exhaustionAmount();
                }

                @Override
                public int maxHeightAboveWorld() {
                    return base.maxHeightAboveWorld();
                }

                @Override
                public boolean maxHeightEnabled() {
                    return base.maxHeightEnabled();
                }

                @Override
                public float requiredFoodAmount() {
                    return base.requiredFoodAmount();
                }

                @Override
                public boolean flyingUsesHunger() {
                    if (entity.level() instanceof ServerLevel serverLevel) {
                        return IcarusFabricGameRules.flyingUsesHunger(serverLevel);
                    }
                    return base.flyingUsesHunger();
                }

                @Override
                public boolean flyingReducesWingDurability() {
                    if (entity.level() instanceof ServerLevel serverLevel) {
                        return IcarusFabricGameRules.flyingReducesWingDurability(serverLevel);
                    }
                    return base.flyingReducesWingDurability();
                }
            };
        };

        IcarusHelper.getEquippedWings = entity -> {
            var trinket = getTrinketsWings(entity);
            return !trinket.isEmpty() ? trinket : entity.getItemBySlot(EquipmentSlot.CHEST);
        };
        IcarusHelper.hasWings = entity -> !getTrinketsWings(entity).isEmpty() || entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof WingItem;
        IcarusHelper.equipFunc = (user, stack) -> {
            if (user.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
                ItemStack newStack = stack.copy();
                newStack.setCount(1);
                user.setItemSlot(EquipmentSlot.CHEST, newStack);
                var equippable = stack.get(DataComponents.EQUIPPABLE);
                var soundEvent = equippable != null ? equippable.equipSound() : null;
                if (!stack.isEmpty()) {
                    user.gameEvent(GameEvent.EQUIP);
                    if (soundEvent != null && !user.isSilent()) {
                        user.level().playSeededSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, user.getSoundSource(), 1.0F, 1.0F, user.getRandom().nextLong());
                    }
                }
                stack.shrink(1);
                return true;
            }
            return false;
        };

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> IcarusItems.ITEMS.stream()
            .map(supplier -> supplier.get().getDefaultInstance())
            .forEach(output::accept));

        EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> IcarusHelper.onFallFlyingTick(entity, IcarusHelper.getEquippedWings(entity), tickElytra));
    }

    private static ItemStack getTrinketsWings(LivingEntity entity) {
        // Trinkets-continued (26.1+)
        ItemStack pb4 = getTrinketsWingsPb4(entity);
        if (!pb4.isEmpty()) {
            return pb4;
        }

        // Legacy Trinkets API
        ItemStack legacy = getTrinketsWingsLegacy(entity);
        if (!legacy.isEmpty()) {
            return legacy;
        }

        return ItemStack.EMPTY;
    }

    private static ItemStack getTrinketsWingsPb4(LivingEntity entity) {
        try {
            Class<?> apiClass = Class.forName("eu.pb4.trinkets.api.TrinketsApi");
            Method getAttachment = apiClass.getMethod("getAttachment", LivingEntity.class);
            Object attachment = getAttachment.invoke(null, entity);
            if (attachment == null) {
                return ItemStack.EMPTY;
            }

            Method getEquipped = attachment.getClass().getMethod("getEquipped", Predicate.class);
            @SuppressWarnings("unchecked")
            Object equippedObj = getEquipped.invoke(attachment, (Predicate<ItemStack>) stack -> stack.getItem() instanceof WingItem);
            return firstWingStackFromList(equippedObj);
        } catch (Throwable ignored) {
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack getTrinketsWingsLegacy(LivingEntity entity) {
        try {
            Class<?> apiClass = Class.forName("dev.emi.trinkets.api.TrinketsApi");
            Method getComponent = apiClass.getMethod("getTrinketComponent", LivingEntity.class);
            Object optionalObj = getComponent.invoke(null, entity);
            if (!(optionalObj instanceof Optional<?> optional) || optional.isEmpty()) {
                return ItemStack.EMPTY;
            }

            Object component = optional.get();
            Method getEquipped = component.getClass().getMethod("getEquipped", Predicate.class);
            @SuppressWarnings("unchecked")
            Object equippedObj = getEquipped.invoke(component, (Predicate<ItemStack>) stack -> stack.getItem() instanceof WingItem);
            return firstWingStackFromList(equippedObj);
        } catch (Throwable ignored) {
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack firstWingStackFromList(Object equippedObj) {
        if (!(equippedObj instanceof List<?> equipped) || equipped.isEmpty()) {
            return ItemStack.EMPTY;
        }

        for (Object tuple : new ArrayList<>(equipped)) {
            for (String accessor : new String[] {"getB", "getA", "getRight", "getLeft", "getSecond", "getFirst"}) {
                try {
                    Method m = tuple.getClass().getMethod(accessor);
                    Object result = m.invoke(tuple);
                    if (result instanceof ItemStack stack && stack.getItem() instanceof WingItem) {
                        return stack;
                    }
                } catch (ReflectiveOperationException ignored) {
                    // try next accessor
                }
            }
        }
        return ItemStack.EMPTY;
    }
}
