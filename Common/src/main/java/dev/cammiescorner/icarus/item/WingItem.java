package dev.cammiescorner.icarus.item;

import dev.cammiescorner.icarus.IcarusConfig;
import dev.cammiescorner.icarus.init.IcarusItemTags;
import dev.cammiescorner.icarus.util.IcarusHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class WingItem extends Item {
    private final WingType wingType;

    public WingItem(Identifier id, WingType wingType) {
        super(new Item.Properties()
            .setId(net.minecraft.resources.ResourceKey.create(Registries.ITEM, id))
            .durability(IcarusConfig.wingsDurability)
            .equippable(EquipmentSlot.CHEST)
            .component(DataComponents.GLIDER, Unit.INSTANCE)
            .rarity(wingType.rarity()));
        this.wingType = wingType;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (IcarusHelper.equipFunc.test(player, stack)) {
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        return super.use(level, player, hand);
    }

    public boolean isUsable(LivingEntity entity, ItemStack stack) {
        return IcarusHelper.getConfigValues(entity).wingsDurability() <= 0 || stack.getDamageValue() < stack.getMaxDamage() - 1;
    }

    public boolean onFlightTick(LivingEntity entity, ItemStack wings, int ticks) {
        var cfg = IcarusHelper.getConfigValues(entity);
        if (cfg.wingsDurability() > 0 && wings.is(IcarusItemTags.MELTS) && !(entity instanceof Player player && player.isCreative())) {
            if (!cfg.flyingReducesWingDurability()) {
                return this.isUsable(entity, wings);
            }
            if (ticks % 20 == 0 || (cfg.maxHeightEnabled() && entity.getY() > entity.level().getHeight() + cfg.maxHeightAboveWorld() && ticks % 2 == 0)) {
                // Avoid forcing chest-slot break callbacks when the wing item is equipped in a Trinkets slot.
                if (entity.level() instanceof ServerLevel serverLevel && entity instanceof ServerPlayer serverPlayer) {
                    wings.hurtAndBreak(1, serverLevel, serverPlayer, item -> {});
                } else {
                    ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
                    if (chest == wings || ItemStack.isSameItemSameComponents(chest, wings)) {
                        wings.hurtAndBreak(1, entity, EquipmentSlot.CHEST);
                    }
                }
            }
        }

        return this.isUsable(entity, wings);
    }

    public boolean isValidRepairItem(ItemStack current, ItemStack repair) {
        return repair.is(IcarusItemTags.WING_REPAIR_ITEMS);
    }

    public WingType getWingType() {
        return this.wingType;
    }

    public enum WingType {
        FEATHERED, DRAGON, MECHANICAL_FEATHERED, MECHANICAL_LEATHER, LIGHT, UNIQUE;

        public Rarity rarity() {
            return this == UNIQUE ? Rarity.EPIC : Rarity.RARE;
        }
    }
}
