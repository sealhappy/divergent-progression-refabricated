package sealhappy.divergeprog.worldgen;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import sealhappy.divergeprog.block.ModBlocks;
import sealhappy.divergeprog.block.PebblesBlock;

public final class FlintPebbleClusterFeature implements Feature {
    public static final MapCodec<FlintPebbleClusterFeature> CODEC =
            MapCodec.unit(new FlintPebbleClusterFeature());

    @Override
    public MapCodec<FlintPebbleClusterFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel world, ChunkGenerator generator,
                         RandomSource random, BlockPos origin) {
        int target = 8 + random.nextInt(5);
        final int radius = 10;
        int placed = 0;

        for (int attempt = 0; attempt < 200 && placed < target; attempt++) {
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;
            if (dx * dx + dz * dz > radius * radius) continue;

            for (int dy = 3; dy >= -3; dy--) {
                BlockPos pos = origin.offset(dx, dy, dz);
                BlockPos below = pos.below();
                if (!world.getBlockState(pos).isAir()) continue;
                BlockState ground = world.getBlockState(below);
                if (!ground.isFaceSturdy(world, below, Direction.UP)) continue;
                // Only naturally occurring terrain: excludes ice, snow blocks,
                // logs, leaves, and constructed surfaces.
                boolean naturalGround =
                        ground.is(Blocks.GRASS_BLOCK)
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
                if (!naturalGround) continue;
                if (world.getBlockState(below).is(ModBlocks.FLINT_PEBBLES)) continue;
                world.setBlock(pos, ModBlocks.FLINT_PEBBLES.defaultBlockState()
                        .setValue(PebblesBlock.PEBBLES, 1 + random.nextInt(4)), 2);
                placed++;
                break;
            }
        }
        return placed > 0;
    }
}
