package sealhappy.divergeprog.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Unique private long divergeprog$lastWarningMillis = 0L;

    @Unique
    private boolean divergeprog$holdingBrokenTool() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.player.isCreative()) return false;
        ItemStack held = minecraft.player.getMainHandItem();
        return held.isDamageableItem() && held.isBroken();
    }

    @Unique
    private void divergeprog$warn() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        long now = System.currentTimeMillis();
        if (now - divergeprog$lastWarningMillis >= 1500L) {
            minecraft.gui.chatListener().handleOverlay(
                Component.literal("This tool is broken! Repair it before using it."));
            divergeprog$lastWarningMillis = now;
        }
    }

    @Unique
    private boolean divergeprog$isNormalInteraction(BlockHitResult hit) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return false;
        BlockPos pos = hit.getBlockPos();
        BlockState state = minecraft.level.getBlockState(pos);
        Block block = state.getBlock();

        // Containers and workstations (chests, furnaces, looms, anvils, etc.).
        if (state.getMenuProvider(minecraft.level, pos) != null) return true;

        // Interactive blocks that do not necessarily expose a MenuProvider.
        return state.is(BlockTags.DOORS)
            || state.is(BlockTags.TRAPDOORS)
            || state.is(BlockTags.FENCE_GATES)
            || block instanceof ButtonBlock
            || state.is(Blocks.LEVER)
            || state.is(Blocks.LECTERN)
            || state.is(Blocks.ENCHANTING_TABLE)
            || state.is(Blocks.BELL)
            || state.is(Blocks.NOTE_BLOCK)
            || state.is(BlockTags.BEDS);
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    private void divergeprog$preventBrokenRightClick(CallbackInfo ci) {
        if (!divergeprog$holdingBrokenTool()) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.hitResult instanceof BlockHitResult blockHit
                && blockHit.getType() == HitResult.Type.BLOCK) {
            // Leave normal block interactions alone. ItemStackMixin.useOn
            // already blocks broken-tool abilities (stripping/tilling/paths).
            if (!divergeprog$isNormalInteraction(blockHit)) divergeprog$warn();
            return;
        }

        // Prevent using broken items in the air.
        divergeprog$warn();
        ci.cancel();
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void divergeprog$preventBrokenAttack(CallbackInfoReturnable<Boolean> cir) {
        if (divergeprog$holdingBrokenTool()) {
            divergeprog$warn();
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void divergeprog$preventBrokenMining(boolean attacking, CallbackInfo ci) {
        if (attacking && divergeprog$holdingBrokenTool()) {
            divergeprog$warn();
            ci.cancel();
        }
    }
}
