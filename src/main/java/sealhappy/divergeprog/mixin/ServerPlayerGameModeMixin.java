package sealhappy.divergeprog.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {
    @Shadow protected ServerPlayer player;

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    private void divergeprog$preventBrokenMining(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (player.isCreative()) return;
        ItemStack stack = player.getMainHandItem();
        if (stack.isDamageableItem() && stack.isBroken()) cir.setReturnValue(false);
    }
}
