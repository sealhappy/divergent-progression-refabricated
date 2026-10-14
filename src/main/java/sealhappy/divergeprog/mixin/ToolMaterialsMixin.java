
package sealhappy.divergeprog.mixin;

import net.minecraft.world.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ToolMaterial.class)
public abstract class ToolMaterialsMixin {

    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(intValue = 59)
    )
    private static int divergentProgression$modifyWoodDurability(int original) {
        return 96;
    }

    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(intValue = 131)
    )
    private static int divergentProgression$modifyStoneDurability(int original) {
        return 192;
    }

    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(intValue = 250)
    )
    private static int divergentProgression$modifyIronDurability(int original) {
        return 480;
    }

    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(intValue = 32)
    )
    private static int divergentProgression$modifyGoldDurability(int original) {
        return 128;
    }

    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(intValue = 1561)
    )
    private static int divergentProgression$modifyDiamondDurability(int original) {
        return 1600;
    }

    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(intValue = 2031)
    )
    private static int divergentProgression$modifyNetheriteDurability(int original) {
        return 2048;
    }
}
