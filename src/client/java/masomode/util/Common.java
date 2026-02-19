package masomode.util;

import masomode.MainClient;
import masomode.mixininterface.ILevelRenderer;
import net.minecraft.world.level.MoonPhase;

public class Common {
    public static boolean isBloodMoon() {
        return MainClient.mc.level.isDarkOutside() && ((ILevelRenderer)MainClient.mc.levelRenderer).getLevelRenderState().skyRenderState.moonPhase == MoonPhase.NEW_MOON;
    }
}
