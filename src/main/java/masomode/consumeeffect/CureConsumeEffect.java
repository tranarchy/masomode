package masomode.consumeeffect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class CureConsumeEffect implements ConsumeEffect {
    private final List<Holder<MobEffect>> mobEffectList = new ArrayList<>();

    public CureConsumeEffect(List<Holder<MobEffect>> mobEffectList) {
        this.mobEffectList.addAll(mobEffectList);
    }

    public CureConsumeEffect(Holder<MobEffect> mobEffect) {
        this.mobEffectList.add(mobEffect);
    }

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return Type.APPLY_EFFECTS;
    }

    @Override
    public boolean apply(Level level, ItemStack itemStack, LivingEntity livingEntity) {
        for (Holder<MobEffect> mobEffect : this.mobEffectList) {
            if (livingEntity.hasEffect(mobEffect)) {
                livingEntity.removeEffect(mobEffect);
            }
        }
        return true;
    }
}
