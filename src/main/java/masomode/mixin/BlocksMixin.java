package masomode.mixin;

import masomode.mixininterface.IProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Mixin(Blocks.class)
public class BlocksMixin {

    @Unique
    private static Block modifyBlock(String stringIdentifier, BlockBehaviour.Properties originalProperties) {
        Block block = null;

        List<String> gravityBlocks = Arrays.asList(
                "dirt", "coarse_dirt", "clay", "snow_block", "cobblestone", "mossy_cobblestone", "cobbled_deepslate", "andesite", "diorite", "granite", "tuff"
        );

        float destroyTime = ((IProperties)originalProperties).getDestroyTime();
        ((IProperties)originalProperties).setDestroyTime(destroyTime * 2.5f);

        if (stringIdentifier.endsWith("_leaves")) {
            originalProperties = originalProperties.noCollision();
        } else if (
                stringIdentifier.endsWith("_log") || stringIdentifier.endsWith("_wood") || stringIdentifier.endsWith("_planks") || stringIdentifier.endsWith("_stairs") || stringIdentifier.endsWith("_slab") || stringIdentifier.equals("warped_stem") || stringIdentifier.equals("crimson_stem")
                || stringIdentifier.endsWith("lantern") || stringIdentifier.equals("crafting_table")) {
            originalProperties = originalProperties.requiresCorrectToolForDrops();
        } else if (stringIdentifier.equals("torch") || stringIdentifier.equals("wall_torch")) {
            originalProperties = originalProperties.lightLevel((blockStatex) -> 0);
        } else if (gravityBlocks.contains(stringIdentifier) || stringIdentifier.endsWith("_wool")) {
            block = Blocks.register(
                    ResourceKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(stringIdentifier)),
                    (properties) -> new SandBlock(new ColorRGBA(14406560), properties), originalProperties
            );
        }

        return block;
    }

    @Inject(method = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", at = @At(value = "HEAD"), cancellable = true)
    private static void register(String string, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties, CallbackInfoReturnable<Block> callbackInfoReturnable) {
        Block block = modifyBlock(string, properties);

        if (block != null) {
            callbackInfoReturnable.setReturnValue(block);
        }
    }

    @Inject(method = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;", at = @At(value = "HEAD"), cancellable = true)
    private static void register(String string, BlockBehaviour.Properties properties, CallbackInfoReturnable<Block> callbackInfoReturnable) {
        Block block = modifyBlock(string, properties);

        if (block != null) {
            callbackInfoReturnable.setReturnValue(block);
        }
    }
}
