package sealhappy.divergeprog.mixin;

import sealhappy.divergeprog.DivergentProgression;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Unique
    private static final Component BROKEN_TEXT =
            Component.translatable(
                    Util.makeDescriptionId(
                            "item",
                            DivergentProgression.id("tooltip.broken")
                    )
            ).withStyle(ChatFormatting.RED);

    @Shadow
    public abstract boolean isBroken();

    @Shadow
    public abstract int getDamageValue();

    @Shadow
    public abstract int getMaxDamage();

    @Shadow
    public abstract void setDamageValue(int value);

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract ItemStack copy();


    /*
     * Prevent tools, armor, and shields from being consumed when broken.
     *
     * Minecraft 26.3 performs the durability update in applyDamage().
     * For these items, preserve the durability update and break callback,
     * but skip the normal stack shrink.
     */
    @Inject(
            method = "applyDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void damageRestrictDecrement(
            int newDamage,
            @Nullable ServerPlayer player,
            Consumer<ItemStack> onBreak,
            CallbackInfo ci
    ) {
        Item item = getItem();

        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
        boolean noDestroy =
                path.endsWith("_sword")
                || path.endsWith("_pickaxe")
                || path.endsWith("_axe")
                || path.endsWith("_shovel")
                || path.endsWith("_hoe")
                || path.endsWith("_spear")
                || path.endsWith("_helmet")
                || path.endsWith("_chestplate")
                || path.endsWith("_leggings")
                || path.endsWith("_boots")
                || item instanceof ShieldItem;

        if (!noDestroy) {
            return;
        }

        int previousDamage = getDamageValue();

        setDamageValue(newDamage);

        if (isBroken() && previousDamage < getMaxDamage()) {
            onBreak.accept(copy());
        }

        ci.cancel();
    }


    /*
     * Add the broken tooltip after the item's name and before its
     * other tooltip details.
     */
    @Inject(
            method = "addDetailsToTooltip",
            at = @At("HEAD")
    )
    private void getBrokenTooltip(
            TooltipContext context,
            TooltipDisplay display,
            @Nullable Player player,
            TooltipFlag flag,
            Consumer<Component> tooltip,
            CallbackInfo ci
    ) {
        if (isBroken()) {
            tooltip.accept(BROKEN_TEXT);
        }
    }


    @Inject(
            method = "useOn",
            at = @At("HEAD"),
            cancellable = true
    )
    private void useOnIfNotBroken(
            UseOnContext context,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (isBroken()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }


    @Inject(
            method = "getDestroySpeed",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getDestroySpeedIfNotBroken(
            BlockState state,
            CallbackInfoReturnable<Float> cir
    ) {
        if (isBroken()) {
            cir.setReturnValue(1.0F);
        }
    }


    @Inject(
            method = "use",
            at = @At("HEAD"),
            cancellable = true
    )
    private void useIfNotBroken(
            Level level,
            Player player,
            InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (isBroken()) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }


    @Inject(
            method = "finishUsingItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void finishUsingIfNotBroken(
            Level level,
            LivingEntity user,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        if (isBroken()) {
            cir.setReturnValue((ItemStack) (Object) this);
        }
    }


    @Inject(
            method = "hurtEnemy",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hurtEnemyIfNotBroken(
            LivingEntity target,
            LivingEntity attacker,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (isBroken()) {
            cir.setReturnValue(false);
        }
    }


    @Inject(
            method = "postHurtEnemy",
            at = @At("HEAD"),
            cancellable = true
    )
    private void postHurtEnemyIfNotBroken(
            LivingEntity target,
            LivingEntity attacker,
            CallbackInfo ci
    ) {
        if (isBroken()) {
            ci.cancel();
        }
    }


    @Inject(
            method = "mineBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void mineBlockIfNotBroken(
            Level level,
            BlockState state,
            BlockPos pos,
            Player miner,
            CallbackInfo ci
    ) {
        if (isBroken()) {
            ci.cancel();
        }
    }


    @Inject(
            method = "isCorrectToolForDrops",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isCorrectToolForDropsIfNotBroken(
            BlockState state,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (isBroken()) {
            cir.setReturnValue(false);
        }
    }


    @Inject(
            method = "getUseAnimation",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getUseAnimationIfNotBroken(
            CallbackInfoReturnable<ItemUseAnimation> cir
    ) {
        if (isBroken()) {
            cir.setReturnValue(ItemUseAnimation.NONE);
        }
    }


    @Inject(
            method = "releaseUsing",
            at = @At("HEAD"),
            cancellable = true
    )
    private void releaseUsingIfNotBroken(
            Level level,
            LivingEntity user,
            int remainingTime,
            CallbackInfo ci
    ) {
        if (isBroken()) {
            ci.cancel();
        }
    }


    @Inject(
            method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void forEachModifierIfNotBroken(
            EquipmentSlot slot,
            BiConsumer<Holder<Attribute>, AttributeModifier> consumer,
            CallbackInfo ci
    ) {
        if (isBroken()) {
            ci.cancel();
        }
    }


    @Inject(
            method = "isEnchantable",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isEnchantableIfNotBroken(
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (isBroken()) {
            cir.setReturnValue(false);
        }
    }
}
