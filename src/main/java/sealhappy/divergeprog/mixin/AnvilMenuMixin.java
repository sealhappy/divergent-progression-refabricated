package sealhappy.divergeprog.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {
    @Shadow @Final private DataSlot cost;
    @Shadow private int repairItemCountCost;
    @Shadow private String itemName;

    @Unique
    private boolean divergeprog$isMaterialRepair() {
        AnvilMenu menu = (AnvilMenu) (Object) this;
        ItemStack input = menu.getSlot(0).getItem();
        ItemStack material = menu.getSlot(1).getItem();
        ItemStack result = menu.getSlot(2).getItem();

        if (input.isEmpty() || material.isEmpty() || result.isEmpty()
                || repairItemCountCost <= 0 || !input.isDamageableItem()
                || !input.isValidRepairItem(material)) {
            return false;
        }

        // Keep renaming separate from free material repairs.
        return itemName == null || itemName.isBlank()
                || itemName.equals(input.getHoverName().getString());
    }

    @Inject(method = "createResult", at = @At("TAIL"))
    private void divergeprog$freeMaterialRepair(CallbackInfo ci) {
        if (divergeprog$isMaterialRepair()) {
            cost.set(0);
        }
    }

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void divergeprog$allowFreeRepair(Player player, boolean hasStack,
                                             CallbackInfoReturnable<Boolean> cir) {
        if (hasStack && divergeprog$isMaterialRepair()) {
            cir.setReturnValue(true);
        }
    }

    // Vanilla's anvil wear roll lives in this synthetic method (verified
    // against the Minecraft 26.3 AnvilMenu bytecode).
    @ModifyConstant(
            method = "lambda$onTake$0",
            constant = @Constant(floatValue = 0.12F)
    )
    private static float divergeprog$reduceAnvilWearChance(float vanillaChance) {
        return 0.10F;
    }

    // Vanilla onTake deducts the current DataSlot cost. Zeroing it in
    // createResult also ensures no levels are deducted on collection.
}
