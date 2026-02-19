package masomode.mixin;

import masomode.utils.Common;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingMenu.class)
public class CraftingMenuMixin {

    @Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V", shift = At.Shift.BEFORE), cancellable = true)
    private static void slotChangedCraftingGrid(
            AbstractContainerMenu abstractContainerMenu,
            ServerLevel serverLevel,
            Player player,
            CraftingContainer craftingContainer,
            ResultContainer resultContainer,
            @Nullable RecipeHolder<CraftingRecipe> recipeHolder,
            CallbackInfo callbackInfo
    ) {
        final int RADIUS = 6;

        boolean hasWorkstationForCrafting = false;
        boolean needsWorkstationForCrafting = false;

        lookForWorkStationLoop:
        for (Block workstation : Common.workstationsForItems.keySet()) {
            if (resultContainer.getItem(0).is(Common.workstationsForItems.get(workstation))) {
                needsWorkstationForCrafting = true;

                for (int x = -RADIUS; x < RADIUS; x++) {
                    for (int y = -RADIUS; y < RADIUS; y++) {
                        for (int z = -RADIUS; z < RADIUS; z++) {
                            if (serverLevel.getBlockState(player.blockPosition().offset(x, y, z)).is(workstation)) {
                                hasWorkstationForCrafting = true;
                                break lookForWorkStationLoop;
                            }
                        }
                    }
                }
            }
        }

        if (!hasWorkstationForCrafting && needsWorkstationForCrafting) {
            resultContainer.setItem(0, ItemStack.EMPTY);
            abstractContainerMenu.setRemoteSlot(0, ItemStack.EMPTY);
            callbackInfo.cancel();
        }
    }
}
