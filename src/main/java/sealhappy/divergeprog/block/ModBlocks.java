package sealhappy.divergeprog.block;

import sealhappy.divergeprog.DivergentProgression;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;   // ResourceLocation if your 26.3 still uses that name
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final Block FLINT_PEBBLES = register("flint_pebbles", false,
            key -> new PebblesBlock(BlockBehaviour.Properties.of()
                    .setId(key)
                    .sound(SoundType.STONE)
                    .destroyTime(0.0f)
                    .noCollision()));

    private static Block register(String name, boolean hasItem,
                                  java.util.function.Function<ResourceKey<Block>, Block> factory) {
        Identifier id = DivergentProgression.id(name);
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
        Block block = Registry.register(BuiltInRegistries.BLOCK, blockKey, factory.apply(blockKey));

        if (hasItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
            Registry.register(BuiltInRegistries.ITEM, itemKey,
                    new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
        }
        return block;
    }

    public static void initialize() {
    }
}
