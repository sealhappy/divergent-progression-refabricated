package sealhappy.divergeprog.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import sealhappy.divergeprog.DivergentProgression;

public class ModBlockTags {
    public static final TagKey<Block> INCORRECT_FOR_FLINT_TOOL = of("incorrect_for_flint_tool");

    private static TagKey<Block> of(String name) {
        return TagKey.create(Registries.BLOCK, DivergentProgression.id(name));
    }
}
