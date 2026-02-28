package masomode.mixin;

import masomode.item.CustomItems;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin extends AbstractContainerMenu {

    protected EnchantmentMenuMixin(@Nullable MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    @Override
    protected Slot addSlot(Slot slot) {

       if (slot.x == 35 && slot.y == 47) {
           return super.addSlot(new Slot(slot.container, 1, 35, 47) {

               @Override
               public boolean mayPlace(ItemStack itemStack) {
                   return itemStack.is(CustomItems.CERULEAN_PEARL);
               }

               // no icon for now
               /*@Override
               public Identifier getNoItemIcon() {
                   return EnchantmentMenu.EMPTY_SLOT_LAPIS_LAZULI;
               }*/
           });
       } else {
           return super.addSlot(slot);
       }
    }
}
