package masomode.mixin.client;

import masomode.mixininterface.ILevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.LevelRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
@Mixin(LevelRenderer.class)
public class LevelRendererMixin implements ILevelRenderer {
    @Shadow
    @Final
    private LevelRenderState levelRenderState;

    @Override
    public LevelRenderState getLevelRenderState() {
        return levelRenderState;
    }
}
