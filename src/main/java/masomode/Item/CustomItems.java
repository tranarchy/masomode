package masomode.Item;

import masomode.Main;
import masomode.block.CustomBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CustomItems {
    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Main.MOD_ID, name));
        GenericItem item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static Item COPPER_DUST;
    public static Item IRON_DUST;
    public static Item DIAMOND_DUST;

    public static Item BLUE_HERB;
    public static Item RED_HERB;
    public static Item GREEN_HERB;
    public static Item MIXED_HERBS_B_R;
    public static Item MIXED_HERBS_G_R;

    public static Item LIT_TORCH;
    public static Item FIRE_STARTER;


    public static void register() {
        COPPER_DUST = register("copper_dust", Item::new, new Item.Properties());
        IRON_DUST = register("iron_dust", Item::new, new Item.Properties());
        DIAMOND_DUST = register("diamond_dust", Item::new, new Item.Properties());

        BLUE_HERB = register("blue_herb", BlueHerbItem::new, BlueHerbItem.properties);
        RED_HERB = register("red_herb", RedHerbItem::new, RedHerbItem.properties);
        GREEN_HERB = register("green_herb", GreenHerbItem::new, GreenHerbItem.properties);
        MIXED_HERBS_B_R = register("mixed_herbs_b_r", MixedHerbsBRItem::new, MixedHerbsBRItem.properties);
        MIXED_HERBS_G_R = register("mixed_herbs_g_r", MixedHerbsGRItem::new, MixedHerbsGRItem.properties);

        FIRE_STARTER = register("fire_starter", FireStarterItem::new, new Item.Properties().durability(5));

        LIT_TORCH = Items.registerBlock(CustomBlocks.LIT_TORCH, (BiFunction<Block, Item.Properties, Item>)((block, properties) -> new StandingAndWallBlockItem(block, CustomBlocks.LIT_WALL_TORCH, Direction.DOWN, properties)));
    }
}
