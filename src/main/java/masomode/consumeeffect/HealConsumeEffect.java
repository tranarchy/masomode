package masomode.consumeeffect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

public class HealConsumeEffect implements ConsumeEffect {
    private final int health;

    public HealConsumeEffect(int health) {
        this.health = health;
    }

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return Type.APPLY_EFFECTS;
    }

    @Override
    public boolean apply(Level level, ItemStack itemStack, LivingEntity livingEntity) {
        livingEntity.heal(health);
        return true;
    }
}
