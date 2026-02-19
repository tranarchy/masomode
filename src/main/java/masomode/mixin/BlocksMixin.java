package masomode.mixin;

import masomode.mixininterface.IProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(Blocks.class)
public class BlocksMixin {
    @Unique
    private static Block modifyBlock(String stringIdentifier, BlockBehaviour.Properties originalProperties) {
        Block block = null;

        float destroyTime = ((IProperties)originalProperties).getDestroyTime();
        ((IProperties)originalProperties).setDestroyTime(destroyTime * 2.5f);

        if (stringIdentifier.endsWith("_leaves")) {
            originalProperties = originalProperties.noCollision();
            return block;
        } else if (
                stringIdentifier.endsWith("_log") || stringIdentifier.endsWith("_wood") || stringIdentifier.endsWith("_planks") || stringIdentifier.equals("warped_stem") || stringIdentifier.equals("crimson_stem")
                || stringIdentifier.endsWith("lantern") || stringIdentifier.equals("crafting_table")) {
            originalProperties = originalProperties.requiresCorrectToolForDrops();
            return block;
        }

        switch (stringIdentifier) {
            case "dirt":
                block = Blocks.register(
                        ResourceKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(stringIdentifier)),
                        (properties) -> new SandBlock(new ColorRGBA(14406560), properties), BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F * 10f, 0.5f).sound(SoundType.GRAVEL)
                );
                break;
            case "clay":
                block = Blocks.register(
                        ResourceKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(stringIdentifier)),
                        (properties) -> new SandBlock(new ColorRGBA(14406560), properties), BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).instrument(NoteBlockInstrument.FLUTE).strength(0.6F * 10f, 0.6f).sound(SoundType.GRAVEL)
                );
                break;
            case "snow_block":
                block = Blocks.register(
                        ResourceKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(stringIdentifier)),
                        (properties) -> new SandBlock(new ColorRGBA(14406560), properties),   BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(0.2F).sound(SoundType.SNOW)
                );

                break;
            case "cobblestone", "mossy_cobblestone":
                block = Blocks.register(
                        ResourceKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(stringIdentifier)),
                        (properties) -> new SandBlock(new ColorRGBA(14406560), properties), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F * 5f, 6.0F)
                );
                break;
            case "cobbled_deepslate":
                block = Blocks.register(
                        ResourceKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(stringIdentifier)),
                        (properties) -> new SandBlock(new ColorRGBA(14406560), properties), BlockBehaviour.Properties.ofLegacyCopy(Blocks.DEEPSLATE).strength(3.5F * 5f, 6.0F)
                );
                break;
            case "andesite":
                block = Blocks.register(
                        ResourceKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(stringIdentifier)),
                        (properties) -> new SandBlock(new ColorRGBA(14406560), properties), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F * 5f, 6.0F)
                );
                break;
            case "diorite":
                block = Blocks.register(
                        ResourceKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(stringIdentifier)),
                        (properties) -> new SandBlock(new ColorRGBA(14406560), properties),  BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F * 5f, 6.0F)
                );
                break;
            case "granite":
                block = Blocks.register(
                        ResourceKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(stringIdentifier)),
                        (properties) -> new SandBlock(new ColorRGBA(14406560), properties),   BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F * 5f, 6.0F)
                );
                break;
            case "tuff":
                block = Blocks.register(
                        ResourceKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(stringIdentifier)),
                        (properties) -> new SandBlock(new ColorRGBA(14406560), properties),   BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_GRAY).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.TUFF).requiresCorrectToolForDrops().strength(1.5F * 5f, 6.0F)
                );
                break;
            case "torch", "wall_torch":
               originalProperties = originalProperties.lightLevel((blockStatex) -> 0);
               break;
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
