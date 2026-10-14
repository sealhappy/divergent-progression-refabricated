package sealhappy.divergeprog.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
    private static boolean divergeprog$broken() {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isCreative()) return false;
        ItemStack stack = player.getMainHandItem();
        return stack.isDamageableItem() && stack.isBroken();
    }

    @Inject(method = "startDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void divergeprog$preventStart(BlockPos pos, Direction face, CallbackInfoReturnable<Boolean> cir) {
        if (divergeprog$broken()) cir.setReturnValue(false);
    }

    @Inject(method = "continueDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void divergeprog$preventContinue(BlockPos pos, Direction face, CallbackInfoReturnable<Boolean> cir) {
        if (divergeprog$broken()) cir.setReturnValue(false);
    }

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    private void divergeprog$preventDestroy(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (divergeprog$broken()) cir.setReturnValue(false);
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void divergeprog$preventAttack(Player player, Entity target, CallbackInfo ci) {
        if (divergeprog$broken()) ci.cancel();
    }
}
