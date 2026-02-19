package masomode.mixin;

import masomode.mixininterface.IProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockBehaviour.Properties.class)
public class PropertiesMixin implements IProperties {
    @Shadow
    float destroyTime;

    @Override
    public float getDestroyTime() {
        return destroyTime;
    }

    @Override
    public void setDestroyTime(float destroyTime) {
        this.destroyTime = destroyTime;
    }
}
