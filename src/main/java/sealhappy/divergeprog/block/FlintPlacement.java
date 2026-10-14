package sealhappy.divergeprog.block;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class FlintPlacement {
    private FlintPlacement() {}

    public static void initialize() {
        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.is(Items.FLINT)) return InteractionResult.PASS;

            BlockPos clicked = hit.getBlockPos();
            BlockState clickedState = world.getBlockState(clicked);
            if (clickedState.is(ModBlocks.FLINT_PEBBLES)) {
                int count = clickedState.getValue(PebblesBlock.PEBBLES);
                if (count >= 4) return InteractionResult.PASS;
                if (!world.isClientSide()) {
                    world.setBlock(clicked, clickedState.setValue(PebblesBlock.PEBBLES, count + 1), 3);
                    if (!player.isCreative()) stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }

            if (hit.getDirection() != Direction.UP || !isNaturalGround(clickedState)) {
                return InteractionResult.PASS;
            }
            BlockPos target = clicked.above();
            if (!world.getBlockState(target).isAir()) return InteractionResult.PASS;
            if (!ModBlocks.FLINT_PEBBLES.defaultBlockState().canSurvive(world, target)) {
                return InteractionResult.PASS;
            }
            if (!world.isClientSide()) {
                world.setBlock(target, ModBlocks.FLINT_PEBBLES.defaultBlockState()
                        .setValue(PebblesBlock.PEBBLES, 1), 3);
                if (!player.isCreative()) stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        });
    }

    private static boolean isNaturalGround(BlockState ground) {
        return ground.is(Blocks.GRASS_BLOCK)
                || ground.is(Blocks.DIRT)
                || ground.is(Blocks.COARSE_DIRT)
                || ground.is(Blocks.PODZOL)
                || ground.is(Blocks.ROOTED_DIRT)
                || ground.is(Blocks.MYCELIUM)
                || ground.is(Blocks.STONE)
                || ground.is(Blocks.ANDESITE)
                || ground.is(Blocks.DIORITE)
                || ground.is(Blocks.GRANITE)
                || ground.is(Blocks.GRAVEL)
                || ground.is(Blocks.SAND)
                || ground.is(Blocks.RED_SAND)
                || ground.is(Blocks.MUD);
    }
}
