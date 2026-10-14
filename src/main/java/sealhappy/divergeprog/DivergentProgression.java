package sealhappy.divergeprog;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import sealhappy.divergeprog.block.ModBlocks;
import sealhappy.divergeprog.block.FlintPlacement;
import sealhappy.divergeprog.item.ModItems;
import sealhappy.divergeprog.worldgen.ModFeatures;

public class DivergentProgression implements ModInitializer {

    public static final String MOD_ID = "divergeprog";

    @Override
    public void onInitialize() {
        // Register blocks
        ModBlocks.initialize();

        // Register items
        ModItems.initialize();

        // Register world generation
        ModFeatures.initialize();

        // Enable flint placement and stacking (1-4)
        FlintPlacement.initialize();

        // CompatMods.initialize();
    }

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }
}
