package sealhappy.divergeprog.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import sealhappy.divergeprog.DivergentProgression;

public final class ModFeatures {
    private ModFeatures() {}

    public static final ResourceKey<PlacedFeature> FLINT_PEBBLES_KEY =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    DivergentProgression.id("flint_pebbles"));

    public static void initialize() {
        Registry.register(
                BuiltInRegistries.FEATURE_TYPE,
                DivergentProgression.id("flint_pebble_cluster"),
                FlintPebbleClusterFeature.CODEC
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                FLINT_PEBBLES_KEY
        );
    }
}
