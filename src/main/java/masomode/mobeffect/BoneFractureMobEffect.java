package masomode.mobeffect;

import masomode.Main;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BoneFractureMobEffect extends MobEffect {
    public BoneFractureMobEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
        this.addAttributeModifier(Attributes.MAX_HEALTH, Identifier.fromNamespaceAndPath(Main.MOD_ID, "effect.bone_fracture"), -4.0, AttributeModifier.Operation.ADD_VALUE);
    }
}
