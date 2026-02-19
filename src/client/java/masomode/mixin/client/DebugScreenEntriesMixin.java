package masomode.mixin.client;

import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(DebugScreenEntries.class)
public class DebugScreenEntriesMixin {

    @Unique
    private static final List<String> allowList = Arrays.asList("fps", "tps", "memory", "system_specs", "game_version");

    @Inject(method = "getEntry", at = @At("HEAD"), cancellable = true)
    private static void getEntry(Identifier identifier, CallbackInfoReturnable<DebugScreenEntry> callbackInfoReturnable) {
        if (!allowList.contains(identifier.getPath())) {
            callbackInfoReturnable.setReturnValue(null);
        }
    }
}
