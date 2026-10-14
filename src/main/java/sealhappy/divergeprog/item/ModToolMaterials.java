package sealhappy.divergeprog.item;

import sealhappy.divergeprog.DivergentProgression;
import sealhappy.divergeprog.block.ModBlockTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public final class ModToolMaterials {
    private ModToolMaterials() {
    }

    public static final TagKey<Item> REPAIRS_FLINT_TOOLS =
            TagKey.create(
                    Registries.ITEM,
                    DivergentProgression.id("repairs_flint_tools")
            );

    public static final ToolMaterial FLINT = new ToolMaterial(
            ModBlockTags.INCORRECT_FOR_FLINT_TOOL,
            96,
            2.0F,
            0.0F,
            15,
            REPAIRS_FLINT_TOOLS
    );
}
