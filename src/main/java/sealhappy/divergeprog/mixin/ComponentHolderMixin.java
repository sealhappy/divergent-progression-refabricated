package sealhappy.divergeprog.mixin;

import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static sealhappy.divergeprog.item.ComponentHolderState.*;

@Mixin(DataComponentHolder.class)
public interface ComponentHolderMixin {

    @Shadow
    DataComponentMap getComponents();

    @SuppressWarnings({"ConstantValue", "unchecked"})
    @Inject(
            method = "get",
            at = @At("HEAD"),
            cancellable = true
    )
    default <T> void getForItemStack(
            DataComponentType<? extends T> type,
            CallbackInfoReturnable<T> cir
    ) {
        if (!((Object) this instanceof ItemStack itemStack)) return;

        boolean isEnchantments = type == DataComponents.ENCHANTMENTS;

        if ((getBlockedBrokenComponents().contains(type) || isEnchantments)
                && isItemStackBroken(itemStack)) {

            if (isEnchantments
                    && getComponents().has(DataComponents.ENCHANTMENTS)) {
                cir.setReturnValue((T) ItemEnchantments.EMPTY);
                return;
            }

            cir.setReturnValue(null);
        }
    }

    @SuppressWarnings("ConstantValue")
    @Inject(
            method = "getOrDefault",
            at = @At("HEAD"),
            cancellable = true
    )
    default <T> void getOrDefaultForItemStack(
            DataComponentType<? extends T> type,
            T fallback,
            CallbackInfoReturnable<T> cir
    ) {
        if (!((Object) this instanceof ItemStack itemStack)) return;

        if ((getBlockedBrokenComponents().contains(type)
                || type == DataComponents.ENCHANTMENTS)
                && isItemStackBroken(itemStack)) {
            cir.setReturnValue(fallback);
        }
    }

    @SuppressWarnings("ConstantValue")
    @Inject(
            method = "has",
            at = @At("HEAD"),
            cancellable = true
    )
    default void hasForItemStack(
            DataComponentType<?> type,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!((Object) this instanceof ItemStack itemStack)) return;

        if (getBlockedBrokenComponents().contains(type)
                && isItemStackBroken(itemStack)) {
            cir.setReturnValue(false);
        }
    }
}
