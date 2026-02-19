package masomode.utils;

import masomode.Item.CustomItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.MoonPhase;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Random;

public class Common {
    public static HashMap<Block, TagKey<Item>> workstationsForItems = new HashMap<Block, TagKey<Item>>() {{
        put(Blocks.LOOM, CustomItemTags.LOOM_CRAFTING);
        put(Blocks.FLETCHING_TABLE, CustomItemTags.FLETCHING_CRAFTING);
        put(Blocks.WATER_CAULDRON, CustomItemTags.CAULDRON_CRAFTING);
        put(Blocks.SMITHING_TABLE, CustomItemTags.SMITHING_TABLE_CRAFTING);
        put(Blocks.ANVIL, CustomItemTags.ANVIL_CRAFTING);
        put(Blocks.CHIPPED_ANVIL, CustomItemTags.ANVIL_CRAFTING);
        put(Blocks.DAMAGED_ANVIL, CustomItemTags.ANVIL_CRAFTING);
        put(Blocks.GRINDSTONE, CustomItemTags.GRINDSTONE_CRAFTING);
        put(Blocks.ENCHANTING_TABLE, CustomItemTags.ENCHANTING_TABLE_CRAFTING);
    }};

    private static boolean isFlammable(LevelReader levelReader, BlockPos blockPos) {
        return levelReader.isInsideBuildHeight(blockPos.getY()) && !levelReader.hasChunkAt(blockPos) ? false : levelReader.getBlockState(blockPos).ignitedByLava();
    }

    private static boolean hasFlammableNeighbours(LevelReader levelReader, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            if (isFlammable(levelReader, blockPos.relative(direction))) {
                return true;
            }
        }

        return false;
    }

    public static void setNeighborBlocksOnFire(ServerLevel serverLevel, BlockPos blockPos) {
        if (serverLevel.canSpreadFireAround(blockPos)) {
            Random random = new Random();

            int i = random.nextInt(3);
            if (i > 0) {
                BlockPos blockPos2 = blockPos;

                for (int j = 0; j < i; j++) {
                    blockPos2 = blockPos2.offset(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
                    if (!serverLevel.isLoaded(blockPos2)) {
                        return;
                    }

                    BlockState blockState = serverLevel.getBlockState(blockPos2);
                    if (blockState.isAir()) {
                        if (hasFlammableNeighbours(serverLevel, blockPos2)) {
                            serverLevel.setBlockAndUpdate(blockPos2, BaseFireBlock.getState(serverLevel, blockPos2));
                            return;
                        }
                    } else if (blockState.blocksMotion()) {
                        return;
                    }
                }
            } else {
                for (int k = 0; k < 3; k++) {
                    BlockPos blockPos3 = blockPos.offset(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
                    if (!serverLevel.isLoaded(blockPos3)) {
                        return;
                    }

                    if (serverLevel.isEmptyBlock(blockPos3.above()) && isFlammable(serverLevel, blockPos3)) {
                        serverLevel.setBlockAndUpdate(blockPos3.above(), BaseFireBlock.getState(serverLevel, blockPos3));
                    }
                }
            }
        }
    }

    public static boolean isBloodMoon(Level level) {
         return level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, BlockPos.ZERO) == MoonPhase.NEW_MOON && level.isDarkOutside();
    }
}
