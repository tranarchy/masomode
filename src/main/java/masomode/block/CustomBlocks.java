package masomode.block;

import masomode.Main;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public class CustomBlocks {
    public static <GenericBlock extends Block> GenericBlock register(String name, Function<Block.Properties, GenericBlock> blockFactory, Block.Properties settings) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Main.MOD_ID, name));
        GenericBlock block = blockFactory.apply(settings.setId(blockKey));
        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);

        return block;
    }

    private static BlockBehaviour.Properties wallVariant(Block block, boolean bl) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().overrideLootTable(block.getLootTable());
        if (bl) {
            properties = properties.overrideDescription(block.getDescriptionId());
        }

        return properties;
    }

    public static Block LIT_TORCH;
    public static Block LIT_WALL_TORCH;
    public static Block BLUE_HERB;
    public static Block RED_HERB;
    public static Block GREEN_HERB;

    public static void register() {
        LIT_TORCH = register("lit_torch", (properties) -> new LitTorchBlock(ParticleTypes.FLAME, properties), BlockBehaviour.Properties.of().noCollision().instabreak().lightLevel((blockStatex) -> 14).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY));
        LIT_WALL_TORCH = register("lit_wall_torch", (properties) -> new LitWallTorchBlock(ParticleTypes.FLAME, properties), wallVariant(LIT_TORCH, true).noCollision().instabreak().lightLevel((blockStatex) -> 14).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY));
        BLUE_HERB = register("blue_herb", HerbBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollision().instabreak().sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY));
        RED_HERB = register("red_herb", HerbBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollision().instabreak().sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY));
        GREEN_HERB = register("green_herb", HerbBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollision().instabreak().sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY));
    }
}
