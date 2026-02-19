package masomode.Item;

import masomode.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CustomItemTags {
    public static TagKey<Item> LOOM_CRAFTING;
    public static TagKey<Item> FLETCHING_CRAFTING;
    public static TagKey<Item> SMITHING_TABLE_CRAFTING;
    public static TagKey<Item> ANVIL_CRAFTING;
    public static TagKey<Item> GRINDSTONE_CRAFTING;
    public static TagKey<Item> ENCHANTING_TABLE_CRAFTING;
    public static TagKey<Item> CAULDRON_CRAFTING;

    public static void register() {
        LOOM_CRAFTING = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Main.MOD_ID, "loom_crafting"));
        FLETCHING_CRAFTING = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Main.MOD_ID, "fletching_crafting"));
        SMITHING_TABLE_CRAFTING = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Main.MOD_ID, "smithing_table_crafting"));
        ANVIL_CRAFTING = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Main.MOD_ID, "anvil_crafting"));
        GRINDSTONE_CRAFTING = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Main.MOD_ID, "grindstone_crafting"));
        ENCHANTING_TABLE_CRAFTING = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Main.MOD_ID, "enchanting_table_crafting"));
        CAULDRON_CRAFTING = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Main.MOD_ID, "cauldron_crafting"));
    }
}
