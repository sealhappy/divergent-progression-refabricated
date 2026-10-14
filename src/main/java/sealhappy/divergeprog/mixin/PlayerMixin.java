package sealhappy.divergeprog.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {
    private boolean divergeprog$isHoldingBrokenItem() {
        ItemStack stack = ((Player) (Object) this).getMainHandItem();
        return stack.isDamageableItem() && stack.isBroken();
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void divergeprog$preventBrokenAttack(Entity target, CallbackInfo ci) {
        if (divergeprog$isHoldingBrokenItem()) ci.cancel();
    }

    @Inject(method = "isSweepAttack", at = @At("HEAD"), cancellable = true)
    private void divergeprog$preventBrokenSweep(boolean first, boolean second, boolean third,
                                                CallbackInfoReturnable<Boolean> cir) {
        if (divergeprog$isHoldingBrokenItem()) cir.setReturnValue(false);
    }
}
