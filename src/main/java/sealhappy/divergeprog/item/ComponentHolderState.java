package sealhappy.divergeprog.item;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public final class ComponentHolderState {
    private ComponentHolderState() {
    }

    private static final Set<DataComponentType<?>> BLOCKED_BROKEN_COMPONENTS =
            Set.of(
                    DataComponents.ATTRIBUTE_MODIFIERS,
                    DataComponents.FOOD,
                    DataComponents.TOOL
            );

    public static Set<DataComponentType<?>> getBlockedBrokenComponents() {
        return BLOCKED_BROKEN_COMPONENTS;
    }

    public static boolean isItemStackBroken(ItemStack itemStack) {
        return itemStack.isDamageableItem()
                && itemStack.getDamageValue() >= itemStack.getMaxDamage();
    }
}
